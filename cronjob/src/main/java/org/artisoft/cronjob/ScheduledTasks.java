package org.artisoft.cronjob;

import com.google.gson.Gson;
import com.sparkpost.Client;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.BasicConfigurator;
import org.artisoft.dal.dao.notification.NotificationQueueDao;
import org.artisoft.domain.Notification.MobileNotifcationList;
import org.artisoft.domain.Notification.PushNotificationData;
import org.artisoft.domain.Notification.PushSendObject;
import org.artisoft.domain.Notification.SendNotificationByMailList;
import org.artisoft.dal.repository.utils.CommonMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Component
public class ScheduledTasks {

    @Autowired
    NotificationQueueDao notificationQueueDao;

    @Scheduled(fixedRate = 20000)
    public void reportCurrentTime() throws AddressException, MessagingException, IOException {
        // System.out.println("reportCurrentTime");
       // BasicConfigurator.configure();
        sendMail();
        pushMobile();

    }

    public void pushMobile() {
        long notquid = 0;
        try {



            for (MobileNotifcationList c : notificationQueueDao.getAllActiveMobileList()) {

                if(c.getDeviceId()!=null) {
                    notquid = c.getNotifQueId();
                    CloseableHttpClient client = HttpClients.createDefault();
                    HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/fcm/send");

                    PushNotificationData pn = new PushNotificationData();
                    pn.setBody(c.getContent());
                    pn.setTitle(c.getTitle());
                    pn.setPriority("high");
                    pn.setSound("default");
                    pn.setContent_available(true);

                    PushSendObject ps = new PushSendObject();
                    ps.setNotification(pn);
                    ps.setData(pn);
                    ps.setTo(c.getDeviceId());
                    Gson gson = new Gson();
                    String json = gson.toJson(ps);


                    StringEntity entity = new StringEntity(json);
                    httpPost.setEntity(entity);
                    httpPost.setHeader("Accept", "application/json");
                    httpPost.setHeader("Content-type", "application/json");
                      CloseableHttpResponse response = client.execute(httpPost);
                    //  assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
                    client.close();

                    notificationQueueDao.NotifQueStatusUpdate(c.getNotifQueId(), 1);
                }
            }


        } catch (Exception e) {
            notificationQueueDao.NotifQueStatusUpdate(notquid, 2);
         //   e.printStackTrace();
        }
            }


    public void sendMail() throws AddressException, MessagingException, IOException {

        long notquid = 0;
        try {
            for (SendNotificationByMailList c : notificationQueueDao.getQueueForMailList()) {
//            Message msg = new MimeMessage(CommonMethods.GetSession());
//
//            notquid=c.getNotifQueId();
//             msg.setFrom(new InternetAddress("asadov.tech@gmail.com", false));
//            System.out.println(c.getEmail());
//            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(c.getEmail()));
//            msg.setSubject(c.getTitle());
//                        // msg.setContent("you have new task content1 ", "text/html");
//            msg.setSentDate(new Date());
//
//
//            String messageContex = c.getContent();
//
//            MimeBodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setContent(messageContex, "text/html");
//
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(messageBodyPart);
//            MimeBodyPart attachPart = new MimeBodyPart();
//
//                        // attachPart.attachFile("/var/tmp/image19.png");
//                         //    multipart.addBodyPart(attachPart);
//            msg.setContent(multipart);
//
//            Transport.send(msg);

//c.setEmail("asadov.tech@gmail.com");
                // String API_KEY = "dbe35e6af6943d8020c95a1bb18fba6ecdf8ef1a";
                String API_KEY = "8dea8879d581a8224fe3051ef9c05a019242779d";

                Client client = new Client(API_KEY);
                client.setFromEmail("noreply@popmuster.com");
                client.sendMessage(
                        "noreply@popmuster.com",
                        c.getEmail(),
                        c.getTitle(),
                        "",
                        c.getContent());

                notificationQueueDao.NotifQueStatusUpdate(c.getNotifQueId(), 1);

            }


        } catch (Exception e) {
            notificationQueueDao.NotifQueStatusUpdate(notquid, 2);
         //   e.printStackTrace();
        }

    }

}
