package org.artisoft.api.controller;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.notification.NotificationQueueDao;
import org.artisoft.domain.Notification.MobileNotifcationList;
import org.artisoft.domain.Notification.NotificationQueue;
import org.artisoft.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/notificationqueue")
@CrossOrigin
public class NotificationQueueController {

    @Autowired
    private NotificationQueueDao notificationQueueDao;

    @RequestMapping(value = "/notifacationqueuelist", method = RequestMethod.GET)
    public ResponseEntity<Response<List<MobileNotifcationList>>> notifacationqueuelist() {

        try {

            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();

            List<MobileNotifcationList> mobileNotifcationLists = notificationQueueDao.getQueueForMobileList(userId);
            Response<List<MobileNotifcationList>> response = new Response<>();
            HttpStatus httpStatus = (mobileNotifcationLists == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, mobileNotifcationLists), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<MobileNotifcationList>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<NotificationQueue>> delete(@PathVariable int id){
        try {
            boolean isSuccess = notificationQueueDao.delete(id);
            Response<NotificationQueue> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<NotificationQueue>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/updateDeviceId", method = RequestMethod.POST)
    public ResponseEntity<Response<Boolean>> updateDeviceId(@RequestBody String deviceId) {

        try {
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            boolean isSuccess = notificationQueueDao.updateDeviceId(userId,deviceId);
            Response<Boolean> response = new Response<>();
            HttpStatus httpStatus =  isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, isSuccess), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Boolean>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

}
