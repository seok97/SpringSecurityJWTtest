package com.example.springsecuritystudy202301.sample.ctrl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@CrossOrigin
@RestController
@Tag(name = "03. 쿠키")
@RequestMapping("/cookie")
public class CookieCtrl {

    @Operation(summary = "쿠키생성")
    @PostMapping("/create")
    @Parameter(name = "cookieTime", required = false)
    public void setCookie(
            HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("createCookieTest", "쿠키생성테스트");
        response.addCookie(cookie);
    }

    @Operation(summary = "쿠키값삭제")
    @PostMapping("/del")
    public void delCookie(
            HttpServletRequest request, HttpServletResponse response
            , @RequestParam(required = false) Integer cookieTime) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("createCookieTest")){
                log.info("cookie name : {} , value : {}" , cookie.getName(), cookie.getValue());
                cookie.setMaxAge(cookieTime);
                cookie.setValue(cookieTime + "초뒤삭제되는쿠키");
            }
        }
    }

    @Operation(summary = "쿠키확인")
    @PostMapping("/get")
    public void getCookie(
            HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            log.info("cookie name : {} , value : {}" , cookie.getName(), cookie.getValue());
        }
    }

}
