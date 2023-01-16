package com.example.springsecuritystudy202301.common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Configuration
public class SessionConfig implements HttpSessionListener {

    private final Logger log = LogManager.getLogger(getClass());

    @Override
    public void sessionCreated(HttpSessionEvent se) {
//        se.getSession().setMaxInactiveInterval(2);
        log.info("##################################### listener create session id: {}", se.getSession().getId());
        log.info("##################################### listener create session timeout: {}", se.getSession().getMaxInactiveInterval());
//        sessions.put(se.getSession().getId(), se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.info("##################################### listener session invalid: {}", se.getSession().getId());
    }

}
