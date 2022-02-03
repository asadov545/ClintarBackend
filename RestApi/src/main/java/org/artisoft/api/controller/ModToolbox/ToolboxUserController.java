package org.artisoft.api.controller.ModToolbox;


import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModToolbox.ToolboxUserDao;
import org.artisoft.dal.dao.UserDao;
import org.artisoft.domain.ModToolbox.UserCompdata;
import org.artisoft.domain.User;
import org.artisoft.domain.Users;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/tb/users")
@CrossOrigin
public class ToolboxUserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ToolboxUserDao toolboxUserDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<Users>> getUsersById(@PathVariable("id") int id) {
        try {
            Users users = toolboxUserDao.getById(id);
            Response<Users> response = new Response<>();
            HttpStatus httpStatus = (users == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, users), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Users>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Users>> delete(@PathVariable int id) {
        try {
            boolean isSuccess = userDao.delete(id);
            Response<Users> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Users>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<Users>> InsertUpdate(@RequestBody Users users) {
        try {
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            Users us=new Users();
            us=toolboxUserDao.getById(userId);

            users.setMainprj(us.getMainprj());
            boolean isSuccess;
            if (users.getUserId() == 0){

                isSuccess = userDao.insert(users) > 0;
              // userDao.sendMailNewUsers(users);
                }
            else
                isSuccess = userDao.update(users);
            if (isSuccess) {
                users = userDao.getById(users.getUserId());

            }

            Response<Users> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, users), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Users>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }



    @RequestMapping(value = "/filterData", method = RequestMethod.POST)
    public ResponseEntity<Response<List<Users>>> filterData(@RequestBody HashMap<String, String> filterData) {

        try {
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            List<Users> usersList = toolboxUserDao.filterDataByUserId(filterData,userId);
            Response<List<Users>> response = new Response<>();
            HttpStatus httpStatus = (usersList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, usersList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<Users>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/ac", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ValueLabel<Long, String>>>> getUserList(@RequestParam("label") String label) {
        try {
            List<ValueLabel<Long, String>> valueLabels = userDao.filterUserList(label,2L);
            Response<List<ValueLabel<Long, String>>> response = new Response<>();
            HttpStatus httpStatus = (valueLabels == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, valueLabels), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<ValueLabel<Long, String>>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getUserListByCustomerId/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<Users>>> getUserListByCustomerId(@PathVariable int id) {

        try {
            List<Users> usersList = userDao.getUserListByCustomerId(id);
            Response<List<Users>> response = new Response<>();
            HttpStatus httpStatus = (usersList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, usersList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<Users>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/compData", method = RequestMethod.GET)
    public ResponseEntity<Response<UserCompdata>> getCompData(){

        try {
            UserCompdata userCompdata =  userDao.getUsersCompData();
            Response<UserCompdata> response = new Response<>();
            HttpStatus httpStatus = (userCompdata == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, userCompdata), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<UserCompdata>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

}
