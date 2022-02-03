package org.artisoft.api.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Component
public class MyApplicationListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private static final Logger LOG = Logger.getLogger(MyApplicationListener.class);

    @Autowired
    HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Object userName = event.getAuthentication().getPrincipal();

        Object credentials = event.getAuthentication().getCredentials();

        org.artisoft.domain.User userInfo = (org.artisoft.domain.User)request.getAttribute("userInfo");

        //LOG.debug("Failed login using USERNAME [" + userName + "]");
    }
}