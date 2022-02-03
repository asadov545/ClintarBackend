package org.artisoft.api.controller.ModTask;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModTask.InsightDao;
import org.artisoft.domain.ModTask.Insight.InsightCombine;
import org.artisoft.domain.ModTask.Insight.InsightTaskStatusList;
import org.artisoft.domain.User;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/task/insight")
@CrossOrigin
public class InsightController
{


    @Autowired
    private InsightDao insightDao;

    @RequestMapping(value = "/insightCombine", method = RequestMethod.GET)
    public ResponseEntity<Response<InsightCombine>> getInsightCombine(){

        try {
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            InsightCombine insightCombine =  insightDao.getInsightCombine(userId);
            Response<InsightCombine> response = new Response<>();
            HttpStatus httpStatus = (insightCombine == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, insightCombine), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<InsightCombine>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/insightTaskStatusList", method = RequestMethod.GET)
    public ResponseEntity<Response<List<InsightTaskStatusList>>> getinsightTaskStatusList(){

        try {

            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            List<InsightTaskStatusList> insightTaskStatusLists = insightDao.getTaskStatusList(userId);
            Response<List<InsightTaskStatusList>> response = new Response<>();
            HttpStatus httpStatus = (insightTaskStatusLists == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, insightTaskStatusLists), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<InsightTaskStatusList>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


}
