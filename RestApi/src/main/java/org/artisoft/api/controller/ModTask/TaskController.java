package org.artisoft.api.controller.ModTask;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.CommonListDao;
import org.artisoft.dal.dao.ModTask.TaskAssigneesDao;
import org.artisoft.dal.dao.ModTask.TaskDao;
import org.artisoft.domain.ModTask.tasks.*;
import org.artisoft.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/task/tasks")
@CrossOrigin
public class TaskController {
    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TaskAssigneesDao taskAssigneesDao;

    @Autowired
    private CommonListDao commonListDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<Task>> getTaskById(@PathVariable("id") int id) {
        try {
            Task task = taskDao.getById(id);
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            StatusInfo statusInfo = new StatusInfo();
            statusInfo = taskDao.getTaskInfo(task.getTaskId(), userId);
            task.setStatusInfo(statusInfo);
            Response<Task> response = new Response<>();
            HttpStatus httpStatus = (task == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, task), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Task>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getAddEditTaskInitials", method = RequestMethod.GET)
    public ResponseEntity<Response<AddEditTaskInitials>> GetAddEditTaskInitialData() {
        User userInfo = Common.GetLoggedUser();
        long userId = userInfo == null ? 0 : userInfo.getUserId();

        AddEditTaskInitials initials = new AddEditTaskInitials();
        initials.setTaskAssignUsers(commonListDao.getTaskAssignUsers(userId));

        try {
            Response<AddEditTaskInitials> response = new Response<>();
            HttpStatus httpStatus = (initials == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, initials), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<AddEditTaskInitials>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Task>> delete(@PathVariable int id) {
        try {
            boolean isSuccess = taskDao.delete(id);
            Response<Task> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Task>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<Task>> InsertUpdate(@RequestBody Task task) {
        try {
            boolean isSuccess;
            if (task.getTaskId() == 0) {
                User userInfo = Common.GetLoggedUser();
                long userId = userInfo == null ? 0 : userInfo.getUserId();
                task.setCreateUserId(userId);
                isSuccess = taskDao.insert(task) > 0;
            } else
                isSuccess = taskDao.update(task);
            if (isSuccess) {
                task = taskDao.getById(task.getTaskId());
            }
            Response<Task> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, task), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Task>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/filterData", method = RequestMethod.POST)
    public ResponseEntity<Response<List<Task>>> filterData(@RequestBody HashMap<String, String> filterData) {

        try {
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            List<Task> taskList = taskDao.filterData2(filterData, userId);
            Response<List<Task>> response = new Response<>();
            HttpStatus httpStatus = (taskList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, taskList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<Task>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/deleteassignee/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<TaskAssignees>> deleteAssignees(@PathVariable int id) {
        try {
            boolean isSuccess = taskAssigneesDao.delete(id);
            Response<TaskAssignees> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<TaskAssignees>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/addassignee", method = RequestMethod.POST)
    public ResponseEntity<Response<List<TaskAssignees>>> addAssignee(@RequestBody TaskAssineeList taskAssineeList) {
        try {
            boolean isSuccess;
            TaskAssignees taskAssignees = new TaskAssignees();
            taskAssignees.setUserIdList(taskAssineeList.getUserIdList());
            taskAssignees.setTaskId(taskAssineeList.getTaskId());
            taskAssignees.setAssignTypeId(taskAssineeList.getAssignTypeId());

         //   isSuccess = taskAssigneesDao.insert(taskAssignees) > 0;
            List<TaskAssignees> taskAssigneesList = null;
          //  if (isSuccess) {
                taskAssigneesList =  taskAssigneesDao.insertNew(taskAssignees);
          //  }
            Response<List<TaskAssignees>> response = new Response<>();
            HttpStatus httpStatus = (taskAssigneesList!=null) ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, taskAssigneesList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<TaskAssignees>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/ChangeTaskStatus", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> ChangeTaskStatus(@RequestBody TaskStatus taskStatus) {
        try {

            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            taskStatus.setUserId(userId);
            String res = taskDao.changeTaskStatus(taskStatus);
            Response<String> response = new Response<>();
            HttpStatus httpStatus = (res != null) ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, res), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<String>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/UpdateTaskMarkup", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> UpdateTaskMarkup(@RequestBody TaskMarkup taskMarkup) {
        try {
            String res = taskDao.updateMarkup(taskMarkup);
            Response<String> response = new Response<>();
            HttpStatus httpStatus = (res != null) ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, res), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<String>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

}
