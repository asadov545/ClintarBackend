package org.artisoft.api.controller.ModTask;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModTask.TaskMessagesDao;
import org.artisoft.domain.ModTask.tasks.TaskMessages;
import org.artisoft.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/task/taskmessages")
@CrossOrigin
public class TaskMessagesController {

    @Autowired
    private TaskMessagesDao taskMessagesDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<TaskMessages>> getTaskMessagesById(@PathVariable("id") int id){
        try {
            TaskMessages taskMessages = taskMessagesDao.getById(id);
            Response<TaskMessages> response = new Response<>();
            HttpStatus httpStatus = (taskMessages == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, taskMessages), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<TaskMessages>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }



    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<TaskMessages>> InsertUpdate(@RequestBody TaskMessages taskMessages){
        try {
            boolean isSuccess;
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            taskMessages.setUserId(userId);

            if (taskMessages.getTmId() == 0)
                isSuccess = taskMessagesDao.insert(taskMessages) > 0;
            else
                isSuccess = taskMessagesDao.update(taskMessages);
            if(isSuccess){
                taskMessages = taskMessagesDao.getById(taskMessages.getTmId());
            }
            Response<TaskMessages> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, taskMessages), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<TaskMessages>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/taskmessagelist", method = RequestMethod.POST)
    public ResponseEntity<Response<List<TaskMessages>>> getAllTaskMessageByTaskId(@RequestParam("taskId") long taskId){

        try {
            List<TaskMessages> taskMessagesList =  taskMessagesDao.getAllTaskMessageByTaskId(taskId);
            Response<List<TaskMessages>> response = new Response<>();
            HttpStatus httpStatus = (taskMessagesList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, taskMessagesList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<TaskMessages>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }
}
