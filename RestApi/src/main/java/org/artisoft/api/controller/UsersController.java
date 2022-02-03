package org.artisoft.api.controller;


import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.UserDao;
import org.artisoft.domain.ModToolbox.UserCompdata;
import org.artisoft.domain.Notification.JobsCompdata;
import org.artisoft.domain.Users;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/users")
@CrossOrigin
public class UsersController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<Users>> getUsersById(@PathVariable("id") int id) {
        try {
            Users users = userDao.getById(id);
            Response<Users> response = new Response<>();
            HttpStatus httpStatus = (users == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, users), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Users>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Users>> delete(@PathVariable int id) {
        try {
            boolean isSuccess = userDao.delete(id);
            Response<Users> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Users>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<Users>> InsertUpdate(@RequestBody Users users) {
        try {
            boolean isSuccess;
            if (users.getUserId() == 0){
                isSuccess = userDao.insert(users) > 0;
              // userDao.sendMailNewUsers(users);
                }
            else
                isSuccess = userDao.update(users);
            if (isSuccess) {
                users = userDao.getById(users.getUserId());

            }

            Response<Users> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, users), HttpStatus.OK);
        } catch (Exception e) {
            Response<Users> response2 = new Response<>();
            response2.setCode(400);
            response2.setMessage("User already  exists");
            response2.setSuccess(false);
            response2.setData(null);
            return new ResponseEntity<>(response2, HttpStatus.OK);
        }
    }



    @RequestMapping(value = "/filterData", method = RequestMethod.POST)
    public ResponseEntity<Response<List<Users>>> filterData(@RequestBody HashMap<String, String> filterData) {

        try {
            List<Users> usersList = userDao.filterDataByModId(filterData,1l);
            Response<List<Users>> response = new Response<>();
            HttpStatus httpStatus = (usersList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, usersList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<Users>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/ac", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ValueLabel<Long, String>>>> getUserList(@RequestParam("label") String label) {
        try {
            List<ValueLabel<Long, String>> valueLabels = userDao.filterUserList(label,1L);
            Response<List<ValueLabel<Long, String>>> response = new Response<>();
            HttpStatus httpStatus = (valueLabels == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, valueLabels), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<ValueLabel<Long, String>>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }



}
