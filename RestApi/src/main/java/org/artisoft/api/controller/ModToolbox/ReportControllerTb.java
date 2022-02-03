package org.artisoft.api.controller.ModToolbox;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.artisoft.api.common.Common;
import org.artisoft.api.common.Report.ReportManager;
import org.artisoft.api.common.Response;
import org.artisoft.dal.repository.ModToolbox.ProjectRepository;
import org.artisoft.domain.ModToolbox.report.DownloadRequestProject;
import org.artisoft.domain.Report.DownloadRequest;
import org.artisoft.domain.Report.DownloadRequestResponse;
import org.artisoft.domain.Report.DownloadSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequestMapping(value = "api/tb/reports")
@CrossOrigin
public class ReportControllerTb {
    @Autowired
    private ServletContext servletContext;

    @Autowired
    HttpServletRequest servletRequest;

    @Autowired
    ReportManager reportManager;
    @Autowired
    ProjectRepository projectRepository;

    @RequestMapping(value = "/request/projectDownload", method = RequestMethod.POST)
    public ResponseEntity<Response<DownloadRequestResponse>> getProjectDownloadToken(@RequestBody DownloadRequestProject downloadRequest) {
        try {
            downloadRequest.setDownloadModule(DownloadRequest.DownloadModule.TOOLBOX_PROJECT);
            DownloadRequestResponse requestResponse = reportManager.getDownloadToken(downloadRequest);

            Response<DownloadRequestResponse> response = new Response<>();
            HttpStatus httpStatus = HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, requestResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<DownloadRequestResponse>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/download/{key}", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> DownloadReport(@PathVariable("key") String key) throws FileNotFoundException, SQLException, ClassNotFoundException, JRException, UnsupportedEncodingException, MalformedURLException {
        DownloadSession downloadSession = ReportManager.getDownloadSessions().get(key);
        if (downloadSession == null) {
            return ResponseEntity.notFound().build();
        }

        if (!downloadSession.IsValid()) {
            return ResponseEntity.badRequest().build();
        }

        if (downloadSession.getDownloadRequest().getDownloadModule() == DownloadRequest.DownloadModule.TOOLBOX_PROJECT) {
            return GetDownloadFileResponseForProject(downloadSession);
        }

        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<ByteArrayResource> GetDownloadFileResponseForProject(DownloadSession downloadSession) throws UnsupportedEncodingException, SQLException, ClassNotFoundException, JRException, MalformedURLException {
        DownloadRequestProject downloadRequest = (DownloadRequestProject) downloadSession.getDownloadRequest();
        String resourceFolder = getClass().getResource("/").getPath();
        resourceFolder = URLDecoder.decode(resourceFolder, "UTF-8");
        File baseDirectory = new File(new File(resourceFolder), ReportManager.getReportsFolder());
        long catId=projectRepository.getCategoryByPrjId(downloadRequest.getProjectId());

        String reportFilename="";
        if (catId==36){
             reportFilename = "ToolboxProject_HR_1_1.jasper";
        }else {
             reportFilename = "ToolboxProject.jasper";
        }
        File sourceFile = new File(baseDirectory, reportFilename);

        String sourceFileName = sourceFile.getPath();
        HashMap parameters = new HashMap();
        Connection jasperCon = reportManager.getConnection();

        String projectId =  Long.toString(downloadRequest.getProjectId());

       // parameters.put("serverBaseURL", Common.GetUrlBase(servletRequest));
        parameters.put("PRJ_ID", projectId);
        // Addition work 21.Nov.2018
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, jasperCon);
        byte[] pdfData = null;
        ByteArrayResource resource = null;
        String fileName;
        MediaType mediaType;

        if (downloadRequest.getExportType() == DownloadRequest.ExportType.PDF) {
            fileName = "filename.pdf";
            mediaType = ReportManager.getMediaTypeForFileName(this.servletContext, fileName);
            pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
            resource = new ByteArrayResource(pdfData);
        } else if (downloadRequest.getExportType() == DownloadRequest.ExportType.Excel) {
            fileName = "filename.xlsx";
            mediaType = ReportManager.getMediaTypeForFileName(this.servletContext, fileName);
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            exporter.exportReport();
            resource = new ByteArrayResource(outputStream.toByteArray());
        } else if (downloadRequest.getExportType() == DownloadRequest.ExportType.Html) {
            fileName = "filename.html";
            mediaType = ReportManager.getMediaTypeForFileName(this.servletContext, fileName);
            HtmlExporter htmlExporter = new HtmlExporter();
            htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
            htmlExporter.exportReport();

            resource = new ByteArrayResource(outputStream.toByteArray());
        } else {
            return ResponseEntity.unprocessableEntity().body(new ByteArrayResource("Məlum olmayan fayl endirim tipi.".getBytes()));
        }
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=" + fileName)
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(resource.contentLength()) //
                .body(resource);
    }
}
