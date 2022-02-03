package org.artisoft.api.controller;


import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.notification.JobsDao;
import org.artisoft.domain.ModTask.Branches;
import org.artisoft.domain.ModTask.permissions.RolePrivsCompdata;
import org.artisoft.domain.Notification.GetJobs;
import org.artisoft.domain.Notification.Jobs;
import org.artisoft.domain.Notification.JobsCompdata;
import org.artisoft.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/tb/jobs")
@CrossOrigin
public class JobsController {

    @Autowired
    private JobsDao jobsDao;

    @RequestMapping(value = "/compData", method = RequestMethod.GET)
    public ResponseEntity<Response<JobsCompdata>> getCompData(){

        try {
            JobsCompdata jobsCompdata =  jobsDao.getJobsCompData();
            Response<JobsCompdata> response = new Response<>();
            HttpStatus httpStatus = (jobsCompdata == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, jobsCompdata), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<JobsCompdata>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/filterData", method = RequestMethod.POST)
    public ResponseEntity<Response<List<GetJobs>>> filterData(@RequestBody HashMap<String, String> filterData) {
        try {
            List<GetJobs> getJobsList = jobsDao.getAllJobs(filterData);
            Response<List<GetJobs>> response = new Response<>();
            HttpStatus httpStatus = (getJobsList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, getJobsList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<GetJobs>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<Jobs>> InsertUpdate(@RequestBody Jobs jobs){
        try {
            boolean isSuccess;

                isSuccess = jobsDao.insert(jobs) > 0;

//            if(isSuccess){
//                jobs = jobsDao.getById(jobs.getBranchesId());
//            }
            Response<Jobs> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus,jobs), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Jobs>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }



    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Jobs>> delete(@PathVariable int id){
        try {
            boolean isSuccess = jobsDao.delete(id);
            Response<Jobs> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<Jobs>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

}
