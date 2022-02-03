package org.artisoft.api.controller.ModTask;

import org.apache.commons.io.FileUtils;
import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModTask.TaskLocationDao;
import org.artisoft.domain.ModTask.tasks.TaskLocation;
import org.artisoft.domain.ModTask.tasks.TaskLocationDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/task/tasklocation")
@CrossOrigin
public class TaskLocationController
{
    @Autowired
    private TaskLocationDao taskLocationDao;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<TaskLocation>> getTaskLocationById(@PathVariable("id") int id){
        try {
            TaskLocation taskLocation = taskLocationDao.getById(id);
            Response<TaskLocation> response = new Response<>();
            HttpStatus httpStatus = (taskLocation == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, taskLocation), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<TaskLocation>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/filterData", method = RequestMethod.POST)
    public ResponseEntity<Response<List<TaskLocation>>> filterData(@RequestBody HashMap<String, String > filterData){

        try {

            List<TaskLocation> taskLocationList =  taskLocationDao.filterData(filterData);
            Response<List<TaskLocation>> response = new Response<>();
            HttpStatus httpStatus = (taskLocationList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, taskLocationList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<TaskLocation>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<TaskLocation>> InsertUpdate(@RequestBody TaskLocation taskLocation){
        try {
            boolean isSuccess;
            if (taskLocation.getLocId() == 0) {
               isSuccess = taskLocationDao.insert(taskLocation) > 0;
            }
            else
                isSuccess = taskLocationDao.update(taskLocation);
            if(isSuccess){
                taskLocation = taskLocationDao.getById(taskLocation.getLocId());
            }
            Response<TaskLocation> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, taskLocation), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<TaskLocation>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<TaskLocation>> delete(@PathVariable int id){
        try {
            boolean isSuccess = taskLocationDao.delete(id);
            Response<TaskLocation> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<TaskLocation>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


}
