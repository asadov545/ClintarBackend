package org.artisoft.api.controller;

import org.artisoft.api.security.JwtTokenProvider;
import org.artisoft.dal.repository.UserRepository;
import org.artisoft.domain.Auth.LoginStatusInfo;
import org.artisoft.domain.Auth.UserAuthInfo;
import org.artisoft.domain.AuthenticationRequest;
import org.artisoft.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @Autowired
    HttpServletRequest request;

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {
        Map<Object, Object> response = new HashMap<>();
        response.put("code", 1);
        response.put("data", "ok");

        User userInfo = null;
        try {
            userInfo = this.users.getUserSecurityInfoByUserName(data.getUsername());

            request.setAttribute("userInfo", userInfo);
            //base64:type254:JDJhJDEwJDBZZnQubnI1YWRlUjcwb0QwQnd4bC50VU5jQ0FsNk5oc2JTY0NMM1paMHN5S0VyWERz

            if(userInfo == null) {
                //throw new UsernameNotFoundException("Username " + data.getUsername() + " not found");
            }

            //String psw =  BCrypt.hashpw("shamil123", BCrypt.gensalt());

            Authentication authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword()));
            User authUserInfo = (User) authResult.getPrincipal();


            if(userInfo.getLastFailedAttemptCount() > 5 && !data.isCaptchaProvided()) {
                response.put("code", 0);
                response.put("isCaptchaRequired", (userInfo != null && userInfo.isCaptchaRequired()));
                response.put("data", "Captcha not provided!");
                return ok(response);
            }

            //userInfo.setPassword(BCrypt.hashpw("shamil123", BCrypt.gensalt()));

            UserAuthInfo userAuthInfo = this.users.getUserSessionInfoById(userInfo.getUserId());
            String token = jwtTokenProvider.CreateSessionToken(userInfo);

            Map<Object, Object> model = new HashMap<>();
            model.put("userInfo", userAuthInfo);
            model.put("token", token);
            response.put("data", model);

            this.users.SaveLoginInfo(new LoginStatusInfo(data.getUsername(), 1, request.getRemoteAddr(), 0));
            return ok(response);
        }
        catch (UsernameNotFoundException ex1) {
            this.users.SaveLoginInfo(new LoginStatusInfo(data.getUsername(), 0, request.getRemoteAddr(), 0));
            response.put("code", -1);
            response.put("isCaptchaRequired", (userInfo != null && userInfo.isCaptchaRequired()));
            response.put("data", "Username or password is incorrect!");
            return ok(response);
        }
        catch (BadCredentialsException ex2) {
            this.users.SaveLoginInfo(new LoginStatusInfo(data.getUsername(), 0, request.getRemoteAddr(), 0));
            response.put("code", -2);
            response.put("isCaptchaRequired", (userInfo != null && userInfo.isCaptchaRequired()));
            response.put("data", "Username or password is incorrect!");
            return ok(response);
        }
        catch (AuthenticationException ex3) {
            this.users.SaveLoginInfo(new LoginStatusInfo(data.getUsername(), 0, request.getRemoteAddr(), 0));
            response.put("code", -3);
            response.put("data", ex3.getMessage());
            response.put("isCaptchaRequired", (userInfo != null && userInfo.isCaptchaRequired()));
            return ok(response);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity logout() {
        Map<Object, Object> response = new HashMap<>();
        response.put("code", 1);
        response.put("data", "ok");
        try {

            return ok(response);
        }
        catch (Exception ex) {
            response.put("code", -1);
            response.put("data", ex.getMessage());
            return ok(response);
        }
    }
}
