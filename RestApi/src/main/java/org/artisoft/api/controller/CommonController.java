package org.artisoft.api.controller;


import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.CommonListDao;
import org.artisoft.domain.CommonList;
import org.artisoft.domain.User;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/common")
@CrossOrigin
public class CommonController {

    @Autowired
    private CommonListDao commonListDao;

 //   gettaskpriority,gettaskstatus


    @RequestMapping(value = "/gettaskstatus", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ValueLabel<Long, String>>>> getTaskStatusList(){
        try {
            List<ValueLabel<Long, String>> valueLabels =  commonListDao.getTaskStatusList();
            Response<List<ValueLabel<Long, String>>> response = new Response<>();
            HttpStatus httpStatus = (valueLabels == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, valueLabels), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<ValueLabel<Long, String>>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/gettaskpriority", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ValueLabel<Long, String>>>> getTaskPriorityList(){
        try {
            List<ValueLabel<Long, String>> valueLabels =  commonListDao.getTaskPriorityList();
            Response<List<ValueLabel<Long, String>>> response = new Response<>();
            HttpStatus httpStatus = (valueLabels == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, valueLabels), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<ValueLabel<Long, String>>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<Response<CommonList>> getCommonList(){

        try {
            
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            CommonList commonList =  commonListDao.getCommonList(userId);
            Response<CommonList> response = new Response<>();
            HttpStatus httpStatus = (commonList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, commonList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<CommonList>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


}
