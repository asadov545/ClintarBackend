package org.artisoft.api.common.Report;

import org.artisoft.api.common.Common;
import org.artisoft.domain.Report.DownloadRequest;
import org.artisoft.domain.Report.DownloadRequestResponse;
import org.artisoft.domain.Report.DownloadSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

@ComponentScan
@Component
public class ReportManager {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    HttpServletRequest servletRequest;

    private static HashMap<String, DownloadSession> downloadSessions = new HashMap<>();
    private static final String REPORTS_FOLDER = "reports";

    public static HashMap<String, DownloadSession> getDownloadSessions() {
        return downloadSessions;
    }

    public static String getReportsFolder() {
        return REPORTS_FOLDER;
    }

    public DownloadRequestResponse getDownloadToken(@RequestBody DownloadRequest downloadRequest) {
        try {

//            // Get logged UserId
//            User userInfo = Common.GetLoggedUser();
//            long userId = userInfo == null ? 0 : userInfo.getUserId();

            DownloadSession reportDownloadSession = new DownloadSession();
            reportDownloadSession.setDownloadRequest(downloadRequest);

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

            return downloadResponse;
        } catch (Exception e) {
            return null;
        }
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

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(env.getProperty("spring.datasource.driver-class-name"));
        String url = env.getProperty("spring.datasource.url");
        Properties props = new Properties();
        props.setProperty("user", env.getProperty("spring.datasource.username"));
        props.setProperty("password", env.getProperty("spring.datasource.password"));
        return DriverManager.getConnection(url, props);
    }
}
