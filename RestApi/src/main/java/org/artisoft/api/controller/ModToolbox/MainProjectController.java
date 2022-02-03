package org.artisoft.api.controller.ModToolbox;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModToolbox.MainProjectDao;
import org.artisoft.domain.ModToolbox.project.MainProject;
import org.artisoft.domain.User;
import org.artisoft.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/tb/mainproject")
@CrossOrigin
public class MainProjectController
{
    @Autowired
    private MainProjectDao mainProjectDao;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<MainProject>> getMainProjectById(@PathVariable("id") int id){
        try {
            MainProject mainProject = mainProjectDao.getById(id);
            Response<MainProject> response = new Response<>();
            HttpStatus httpStatus = (mainProject == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, mainProject), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<MainProject>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }



    @RequestMapping(value = "/getMainProjectList", method = RequestMethod.GET)
    public ResponseEntity<Response<List<MainProject>>> GetMainProjectList() {

        User userInfo = Common.GetLoggedUser();
        long userId = userInfo == null ? 0 : userInfo.getUserId();
        List<MainProject> resultList = mainProjectDao.filterData2(userId);

        try {
            Response<List<MainProject>> response = new Response<>();
            HttpStatus httpStatus = (resultList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, resultList), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new Response<List<MainProject>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<MainProject>> InsertUpdate(@RequestBody MainProject mainProject){
        try {
            boolean isSuccess;
            if (mainProject.getMainPrjId() == 0) {
                User userInfo = Common.GetLoggedUser();
                long userId = userInfo == null ? 0 : userInfo.getUserId();
                mainProject.setCreateUserId(userId);
                isSuccess = mainProjectDao.insert(mainProject) > 0;
            }
            else
                isSuccess = mainProjectDao.update(mainProject);
            if(isSuccess){
                mainProject = mainProjectDao.getById(mainProject.getMainPrjId());
            }
            Response<MainProject> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, mainProject), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<MainProject>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<MainProject>> delete(@PathVariable int id){
        try {
            boolean isSuccess = mainProjectDao.delete(id);
            Response<MainProject> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<MainProject>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/deletePrjUser/{mainPrjId}/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Boolean>> deletePrjUser(@PathVariable int mainPrjId,@PathVariable int userId){
        try {
            boolean isSuccess = mainProjectDao.deleteMainProjectUser(mainPrjId,userId);
            Response<Boolean> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<Boolean>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }



}
