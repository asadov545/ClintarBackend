package org.artisoft.api.controller;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.repository.UserRepository;
import org.artisoft.domain.ChangePassword;
import org.artisoft.domain.Notification.Jobs;
import org.artisoft.domain.User;
import org.artisoft.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "api/settings")
@CrossOrigin
public class SettingsController {
    @Autowired
    UserRepository users;

    @Autowired
    HttpServletRequest request;
    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<Response<Boolean>> ChangePassword(@RequestParam("pswNew") String pswNew, @RequestParam("pswOld") String pswOld ) {
        try {
            ChangePassword changePassword = new ChangePassword();
            User data = Common.GetLoggedUser();
            long userId = data == null ? 0 : data.getUserId();
            changePassword.setUserId(userId);
            changePassword.setPswNew(pswNew);
            changePassword.setPswOld(pswOld);
           // User userInfo = null;

         //   userInfo = this.users.getUserSecurityInfoByUserName(data.getUsername());

         //   request.setAttribute("userInfo", userInfo);

            boolean isSuccess = false;
            Authentication authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getUsername(),pswOld));


            if (authResult.isAuthenticated()) {
                isSuccess = users.changePasword(changePassword);
            }
            Response<Boolean> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, isSuccess), HttpStatus.OK);
        } catch (Exception e) {
            //return new ResponseEntity<>(response.generateResponse(httpStatus, isSuccess), HttpStatus.OK);
           return new ResponseEntity<>(new Response<Boolean>().generateResponse(HttpStatus.NOT_MODIFIED, false),HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/reset/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Boolean>> reset(@PathVariable int id){
        try {

            ChangePassword changePassword = new ChangePassword();
            changePassword.setUserId(id);
            changePassword.setPswNew("Clintar555");
            boolean isSuccess = users.changePasword(changePassword);
            Response<Boolean> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<Boolean>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

}
