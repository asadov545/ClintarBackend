package org.artisoft.api.controller.ModTask;

import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModTask.TaskActivitiesDao;
import org.artisoft.domain.ModTask.tasks.TaskActivities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/task/taskactivities")
@CrossOrigin
public class TaskActivitiesController {

    @Autowired
    private TaskActivitiesDao taskActivitiesDao;

    @RequestMapping(value = "/taskactivitylist", method = RequestMethod.POST)
    public ResponseEntity<Response<List<TaskActivities>>> getAllTaskActivityByTaskId(@RequestParam("taskId") long taskId){

        try {
            List<TaskActivities> taskActivitiesList =  taskActivitiesDao.getAllTaskActivitiesByTaskId(taskId);
            Response<List<TaskActivities>> response = new Response<>();
            HttpStatus httpStatus = (taskActivitiesList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, taskActivitiesList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<TaskActivities>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }
}
