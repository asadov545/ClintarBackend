package org.artisoft.api.controller.ModTask;

import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModTask.BranchesDao;
import org.artisoft.domain.ModTask.Branches;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/task/branches")
@CrossOrigin
public class BranchesController {

    @Autowired
    private BranchesDao branchesDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<Branches>> getBranchesById(@PathVariable("id") int id){
        try {
            Branches branches = branchesDao.getById(id);
            Response<Branches> response = new Response<>();
            HttpStatus httpStatus = (branches == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, branches), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<Branches>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Branches>> delete(@PathVariable int id){
        try {
            boolean isSuccess = branchesDao.delete(id);
            Response<Branches> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<Branches>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<Branches>> InsertUpdate(@RequestBody Branches branches){
        try {
            boolean isSuccess;
            if (branches.getBranchesId() == 0)
                isSuccess = branchesDao.insert(branches) > 0;
            else
                isSuccess = branchesDao.update(branches);
            if(isSuccess){
                branches = branchesDao.getById(branches.getBranchesId());
            }
            Response<Branches> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, branches), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Branches>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/filterData", method = RequestMethod.POST)
    public ResponseEntity<Response<List<Branches>>> filterData(@RequestBody HashMap<String, String > filterData){

        try {
            List<Branches> branchesList =  branchesDao.filterData(filterData);
            Response<List<Branches>> response = new Response<>();
            HttpStatus httpStatus = (branchesList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, branchesList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<Branches>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/ac", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ValueLabel<Long, String>>>> getBranchesList(@RequestParam("label") String label){
        try {
            List<ValueLabel<Long, String>> valueLabels =  branchesDao.filterBranchesList(label);
            Response<List<ValueLabel<Long, String>>> response = new Response<>();
            HttpStatus httpStatus = (valueLabels == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, valueLabels), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<ValueLabel<Long, String>>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

}
