package com.example.springsecuritystudy202301;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
@ServletComponentScan
public class SpringSecurityStudy202301Application {

    // http://localhost:9090/swh/api/swagger-ui/index.html
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityStudy202301Application.class, args);
    }
}
