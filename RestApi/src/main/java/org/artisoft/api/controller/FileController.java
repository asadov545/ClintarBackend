package org.artisoft.api.controller;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.api.exception.FileStorageException;
import org.artisoft.api.property.FileStorageProperties;
import org.artisoft.dal.dao.FileDao;
import org.artisoft.domain.UploadedFiles;
import org.artisoft.domain.ModTask.payload.UploadFileRequest;
import org.springframework.core.io.Resource;
import org.artisoft.api.service.FileStorageService;
import org.artisoft.domain.ModTask.payload.UploadFileResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "api/file")
@CrossOrigin
public class FileController {
    private static final Logger logger = LogManager.getLogger(FileController.class);

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileDao fileDao;

    private List<UploadedFiles> ProcessUploadFile(MultipartFile[] files, UploadFileRequest requestInfo) throws IOException {
        List<UploadedFiles> resultList = new ArrayList<>();

        String tomcatRootFolder = Common.GetTomcatRootFolder();
        tomcatRootFolder = URLDecoder.decode(tomcatRootFolder, "UTF-8");
        logger.warn("tomcatRootFolder = " + tomcatRootFolder);

        Path baseDirectory =  new File(tomcatRootFolder).toPath();
        baseDirectory = Paths.get(baseDirectory.toString(), "webapps");

        Path relativePath = GetUploadRelativePathForRequest(requestInfo).normalize();
        Path fullPath = baseDirectory.resolve(relativePath).normalize();

        fullPath = Paths.get(baseDirectory.toString(), relativePath.toString());
        Files.createDirectories(fullPath);

        logger.warn("baseDirectory = " + baseDirectory);
        logger.warn("relativePath = " + relativePath);
        logger.warn("fullPath = " + fullPath);

        for (MultipartFile file : files) {
            String fileName = fileStorageService.storeFile(file, fullPath);

            UploadedFiles fileResponse = new UploadedFiles();
            fileResponse.setOriginalFilemame(file.getOriginalFilename());
            fileResponse.setFilename(fileName);
            fileResponse.setBasedir(baseDirectory.toString());
            fileResponse.setRelativePath(relativePath.toString());
            fileResponse.setSize(file.getSize());
            if (requestInfo.getUploadKey() == null || requestInfo.getUploadKey().isEmpty()) {
                requestInfo.setUploadKey(Common.randomAlphaNumeric(15));
            }
            fileResponse.setKey(requestInfo.getUploadKey());
            fileResponse.setType(file.getContentType());

            resultList.add(fileResponse);
        }

        return resultList;
    }

    private Path GetUploadRelativePathForRequest(UploadFileRequest requestInfo) {
        Path relativePath;

        if (requestInfo.getUploadType().equals("TASK_PHOTO")) {
            //uploadDir = Paths.get(fileStorageProperties.getTaskPhotoDir()).toAbsolutePath().normalize();
            relativePath = Paths.get(fileStorageProperties.getTaskPhotoDir());
            String todaysFoldername = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
            return relativePath.resolve(todaysFoldername);
        } else {
            relativePath = Paths.get(fileStorageProperties.getUploadDir());
        }

        try {
            return relativePath;
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @PostMapping("/UploadFile")
    public ResponseEntity<Response<UploadFileResponse>> uploadFile(@RequestParam("file") MultipartFile file, UploadFileRequest requestInfo) {
        try {
            logger.debug("Debugging log");
            logger.info("Info log");
            logger.warn("Hey, This is a warning!");
            logger.error("Oops! We have an Error. OK");
            logger.fatal("Damn! Fatal error. Please fix me.");
            String path = FileController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();
            List<UploadedFiles> resultList = ProcessUploadFile(new MultipartFile[]{file}, requestInfo);

            List<Long> result = fileDao.insertAll(resultList);
            Response<UploadFileResponse> response = new Response<>();
            UploadFileResponse responseResult = new UploadFileResponse();
            responseResult.setUploadKey(requestInfo.getUploadKey());
            responseResult.setUploadFileIds(result);
            HttpStatus httpStatus = (result == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, responseResult), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<UploadFileResponse>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @PostMapping("/UploadFiles")
    public ResponseEntity<Response<UploadFileResponse>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, UploadFileRequest requestInfo) {
        try {
            String path = FileController.class.getProtectionDomain().getCodeSource().getLocation().getPath();

            String pathh = System.getProperty("catalina.base");;

            List<UploadedFiles> resultList = ProcessUploadFile(files, requestInfo);

            List<Long> result = fileDao.insertAll(resultList);
            Response<UploadFileResponse> response = new Response<>();

            UploadFileResponse responseResult = new UploadFileResponse();
            responseResult.setUploadKey(requestInfo.getUploadKey());
            responseResult.setUploadFileIds(result);
            HttpStatus httpStatus = (result == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, responseResult), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<UploadFileResponse>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
