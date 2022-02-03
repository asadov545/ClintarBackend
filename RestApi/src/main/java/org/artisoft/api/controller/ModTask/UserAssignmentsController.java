package org.artisoft.api.controller.ModTask;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModTask.UserAssignmentsDao;
import org.artisoft.domain.ModTask.UserAssignments;
import org.artisoft.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/task/userassignments")
@CrossOrigin
public class UserAssignmentsController {

    @Autowired
    private UserAssignmentsDao userAssignmentsDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<UserAssignments>> getUserAssignmentsById(@PathVariable("id") int id){
        try {
            UserAssignments userAssignments = userAssignmentsDao.getById(id);
            Response<UserAssignments> response = new Response<>();
            HttpStatus httpStatus = (userAssignments == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, userAssignments), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<UserAssignments>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<UserAssignments>> delete(@PathVariable int id){
        try {
            boolean isSuccess = userAssignmentsDao.delete(id);
            Response<UserAssignments> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<UserAssignments>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<UserAssignments>> InsertUpdate(@RequestBody UserAssignments userAssignments){
        try {
            boolean isSuccess;
            if (userAssignments.getUserAssignmentId() == 0)
                isSuccess = userAssignmentsDao.insert(userAssignments) > 0;
            else
                isSuccess = userAssignmentsDao.update(userAssignments);
            if(isSuccess){
                userAssignments = userAssignmentsDao.getById(userAssignments.getUserAssignmentId());
            }
            Response<UserAssignments> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, userAssignments), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<UserAssignments>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/filterData", method = RequestMethod.POST)
    public ResponseEntity<Response<List<UserAssignments>>> filterData(@RequestBody HashMap<String, String > filterData){

        try {
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            List<UserAssignments> userAssignmentsList =  userAssignmentsDao.filterData2(filterData,userId);
            Response<List<UserAssignments>> response = new Response<>();
            HttpStatus httpStatus = (userAssignmentsList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, userAssignmentsList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<UserAssignments>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }



    }



}
