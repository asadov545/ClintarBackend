package org.artisoft.api.controller.ModTask;


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
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModTask.ReportDao;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.domain.Report.DownloadRequestResponse;
import org.artisoft.domain.ModTask.reports.*;
import org.artisoft.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping(value = "api/task/reports")
@CrossOrigin
public class ReportController {

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private PrivilegesDao privilegesDao;

    @Autowired
    HttpServletRequest servletRequest;

    private HashMap<String, DownloadSession> downloadSessions = new HashMap<>();
    private static final String REPORTS_FOLDER = "reports";
    private  User userInfo;

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public ResponseEntity<Response<List<TaskResultData>>> filterData(@RequestBody TaskSearchInfo taskSearchInfo) {
        try {
            userInfo= Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            taskSearchInfo.setUserId(userId);
            List<TaskResultData> taskResultDataList = reportDao.filterTaskList(taskSearchInfo);
            Response<List<TaskResultData>> response = new Response<>();
            HttpStatus httpStatus = (taskResultDataList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, taskResultDataList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<TaskResultData>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/userCust", method = RequestMethod.POST)
    public ResponseEntity<Response<List<UserCustResultData>>> filterDataUserCust(@RequestBody UserCustSearchİnfo userCustSearchİnfo) {
        try {
            List<UserCustResultData> userCustResultDataList = reportDao.filterUserCustList(userCustSearchİnfo);
            Response<List<UserCustResultData>> response = new Response<>();
            HttpStatus httpStatus = (userCustResultDataList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, userCustResultDataList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<UserCustResultData>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/request/taskDownload", method = RequestMethod.POST)
    public ResponseEntity<Response<DownloadRequestResponse>> getDownloadToken(@RequestBody DownloadRequestTask downloadRequest) {
        try {

//            // Get logged UserId
//            User userInfo = Common.GetLoggedUser();
//            long userId = userInfo == null ? 0 : userInfo.getUserId();

            DownloadSession reportDownloadSession = new DownloadSession();
            reportDownloadSession.setDownloadRequestTask(downloadRequest);
            reportDownloadSession.setDownloadType(DownloadSession.DownloadType.TASK);

            // Set new request info
            String key = Common.randomAlphaNumeric(50);
            synchronized (downloadSessions) {
                while (downloadSessions.containsKey(key)) {
                    key = Common.randomAlphaNumeric(50);
                }
                downloadSessions.put(key, reportDownloadSession);
            }

            DownloadRequestResponse downloadResponse = new DownloadRequestResponse();
            downloadResponse.setToken(key);

            Response<DownloadRequestResponse> response = new Response<>();
            HttpStatus httpStatus = HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, downloadResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<DownloadRequestResponse>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/request/userDownload", method = RequestMethod.POST)
    public ResponseEntity<Response<DownloadRequestResponse>> getDownloadTokenTaskUsers(@RequestBody DownloadRequestTaskUsers downloadRequestTaskUsers) {
        try {
            DownloadSession reportDownloadSession = new DownloadSession();
            reportDownloadSession.setDownloadRequestTaskUsers(downloadRequestTaskUsers);
            reportDownloadSession.setDownloadType(DownloadSession.DownloadType.USER_CUSTOMER);

            // Set new request info
            String key = Common.randomAlphaNumeric(50);
            synchronized (downloadSessions) {
                while (downloadSessions.containsKey(key)) {
                    key = Common.randomAlphaNumeric(50);
                }
                downloadSessions.put(key, reportDownloadSession);
            }

            DownloadRequestResponse downloadResponse = new DownloadRequestResponse();
            downloadResponse.setToken(key);

            Response<DownloadRequestResponse> response = new Response<>();
            HttpStatus httpStatus = HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, downloadResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<DownloadRequestResponse>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/download/{key}", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> DownloadReport(@PathVariable("key") String key) throws FileNotFoundException, SQLException, ClassNotFoundException, JRException, UnsupportedEncodingException, MalformedURLException {
        DownloadSession downloadSession = downloadSessions.get(key);
        if (downloadSession == null) {
            return ResponseEntity.notFound().build();
        }

        if (!downloadSession.IsValid()) {
            return ResponseEntity.badRequest().build();
        }

        if (downloadSession.getDownloadType() == DownloadSession.DownloadType.TASK) {
            return GetDownloadFileResponseForTask(downloadSession);
        }
        if (downloadSession.getDownloadType() == DownloadSession.DownloadType.USER_CUSTOMER) {
            return GetDownloadFileResponseForTaskUsers(downloadSession);
        }



        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<ByteArrayResource> GetDownloadFileResponseForTask(DownloadSession downloadSession) throws UnsupportedEncodingException, SQLException, ClassNotFoundException, JRException, MalformedURLException {
        DownloadRequestTask downloadRequest = downloadSession.getDownloadRequestTask();
        String resourceFolder = getClass().getResource("/").getPath();
        resourceFolder = URLDecoder.decode(resourceFolder, "UTF-8");
        File baseDirectory = new File(new File(resourceFolder), REPORTS_FOLDER);

        // String reportFilename = downloadRequest.getType() == "TaskReportSimple.jasper" ? "" : "TaskReportDetailed.jasper";

        String reportFilename = "";
        switch (downloadRequest.getType()) {
            case "1" :
                reportFilename = "TaskReportSimple.jasper";
                break;
            case "2" :
                reportFilename = "TaskReportDetailed.jasper";
                break;
            case "3" :
                reportFilename = "TaskReportMedium.jasper";
                break;
            case "4" :
                reportFilename = "TaskReportLarge.jasper";
                break;
            case "5" :
                reportFilename = "TaskReportWithOutImage.jasper";
                break;
            case "6" :
                reportFilename = "TaskReportJustImage.jasper";
                break;
            case "7" :
                reportFilename = "TaskReportMessage.jasper";
                break;
            case "8" :
                reportFilename = "TaskReportActivity.jasper";
                break;

        }


        //String reportFilename = downloadRequest.getType() == "TaskReportDetailed.jasper" ? "" : "TaskReportDetailed.jasper";
        File sourceFile = new File(baseDirectory, reportFilename);

        String sourceFileName = sourceFile.getPath();
        HashMap parameters = new HashMap();
        Connection jasperCon = getConnection();

        TaskSearchInfo searchInfo = downloadRequest.getTaskSearchInfo();

        parameters.put("serverBaseURL", Common.GetUrlBase(servletRequest));
        String taskTitle = searchInfo.getTitle();
        if (taskTitle != null && taskTitle.trim().length() > 0) taskTitle = "%" + taskTitle.trim() + "%";

        // Title
        parameters.put("title", taskTitle);

        // Task status
        parameters.put("taskStatusId", searchInfo.getTaskStatusId());

        // Begin date
        parameters.put("beginDateFrom", searchInfo.getBeginDate() == null ? 0 : searchInfo.getBeginDate().getFrom());
        parameters.put("beginDateTo", searchInfo.getBeginDate() == null ? 0 : searchInfo.getBeginDate().getTo());

        // Due date
        parameters.put("dueDateFrom", searchInfo.getDueDate() == null ? 0 : searchInfo.getDueDate().getFrom());
        parameters.put("dueDateTo", searchInfo.getDueDate() == null ? 0 : searchInfo.getDueDate().getTo());

        // Task priority
        parameters.put("priorityId", searchInfo.getPriorityId());

        parameters.put("taskId",searchInfo.getTaskId());
        // AssignedIds
        String assignList = "";
        for (int i = 0; searchInfo.getAssigneeIds() != null && i < searchInfo.getAssigneeIds().size(); i++) {
            if (assignList.trim().length() > 0) assignList = assignList + ",";
            assignList += searchInfo.getAssigneeIds().get(i).toString();
        }
        if (assignList.length() == 0) assignList = "-1";
        parameters.put("pAssignIds", assignList);


        // ----------------------------------------------------------------------
       // User userInfo = Common.GetLoggedUser();

        long userId = userInfo == null ? 0: userInfo.getUserId();

        if ((privilegesDao.checkPrivStatus(userId, 33) != 1) || (privilegesDao.checkPrivStatus(userId, 39) != 1)) {

            //cond.append("  where (T.CREATE_USER_ID=" + taskSearchInfo.getUserId() + " OR (EXISTS (SELECT 1 FROM TASK_ASSIGNEES AS TA WHERE (T.TASK_ID=TA.TASK_ID AND  TA.USER_ID=" + taskSearchInfo.getUserId() + "))))");
        }
        else
        {
            userId=0;
        }

        parameters.put("userId",userId);


        //---------------------------------------------------------------------

        // Addition work 21.Nov.2018
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, jasperCon);
        byte[] pdfData = null;
        ByteArrayResource resource = null;
        String fileName;
        MediaType mediaType;

        if (downloadRequest.getFileType().equals("PDF")) {
            fileName = "filename.pdf";
            mediaType = getMediaTypeForFileName(this.servletContext, fileName);
            pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
            resource = new ByteArrayResource(pdfData);
        } else if (downloadRequest.getFileType().equals("XLS")) {
            fileName = "filename.xlsx";
            mediaType = getMediaTypeForFileName(this.servletContext, fileName);
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            exporter.exportReport();
            resource = new ByteArrayResource(outputStream.toByteArray());
        } else if (downloadRequest.getFileType().equals("HTML")) {
            fileName = "filename.html";
            mediaType = getMediaTypeForFileName(this.servletContext, fileName);
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

    private ResponseEntity<ByteArrayResource> GetDownloadFileResponseForTaskUsers(DownloadSession downloadSession) throws UnsupportedEncodingException, SQLException, ClassNotFoundException, JRException, MalformedURLException {
        DownloadRequestTaskUsers downloadRequestTaskUsers = downloadSession.getDownloadRequestTaskUsers();
        String resourceFolder = getClass().getResource("/").getPath();
        resourceFolder = URLDecoder.decode(resourceFolder, "UTF-8");
        File baseDirectory = new File(new File(resourceFolder), REPORTS_FOLDER);

        // String reportFilename = downloadRequest.getType() == "TaskReportSimple.jasper" ? "" : "TaskReportDetailed.jasper";
        String reportFilename = "users.jasper";

        //String reportFilename = downloadRequest.getType() == "TaskReportDetailed.jasper" ? "" : "TaskReportDetailed.jasper";
        File sourceFile = new File(baseDirectory, reportFilename);

        String sourceFileName = sourceFile.getPath();
        HashMap parameters = new HashMap();
        Connection jasperCon = getConnection();

        UserCustSearchİnfo searchInfo = downloadRequestTaskUsers.getUserCustSearchİnfo();

        parameters.put("serverBaseURL", Common.GetUrlBase(servletRequest));


        String username = searchInfo.getUsername();
        if (username != null && username.trim().length() > 0) username = "%" + username.trim() + "%";
        // username
        parameters.put("username", username);

        String fullname = searchInfo.getFullname();
        if (fullname != null && fullname.trim().length() > 0) fullname = "%" + fullname.trim() + "%";
        // fullname
        parameters.put("fullname", fullname);

        String email = searchInfo.getEmail();
        if (email != null && email.trim().length() > 0) email = "%" + email.trim() + "%";
        // email
        parameters.put("email", email);


        // Addition work 21.Nov.2018
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, jasperCon);
        byte[] pdfData = null;
        ByteArrayResource resource = null;
        String fileName;
        MediaType mediaType;

        if (downloadRequestTaskUsers.getFileType().equals("PDF")) {
            fileName = "filename.pdf";
            mediaType = getMediaTypeForFileName(this.servletContext, fileName);
            pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
            resource = new ByteArrayResource(pdfData);
        } else if (downloadRequestTaskUsers.getFileType().equals("XLS")) {
            fileName = "filename.xlsx";
            mediaType = getMediaTypeForFileName(this.servletContext, fileName);
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            exporter.exportReport();
            resource = new ByteArrayResource(outputStream.toByteArray());
        } else if (downloadRequestTaskUsers.getFileType().equals("HTML")) {
            fileName = "filename.html";
            mediaType = getMediaTypeForFileName(this.servletContext, fileName);
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

    public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        // application/pdf
        // application/xml
        // image/gif, ...
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @Autowired
    private Environment env;

    protected Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(env.getProperty("spring.datasource.driver-class-name"));
        String url = env.getProperty("spring.datasource.url");
        Properties props = new Properties();
        props.setProperty("user", env.getProperty("spring.datasource.username"));
        props.setProperty("password", env.getProperty("spring.datasource.password"));
        return DriverManager.getConnection(url, props);
    }
}
