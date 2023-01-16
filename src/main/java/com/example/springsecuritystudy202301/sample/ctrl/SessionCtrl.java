package com.example.springsecuritystudy202301.sample.ctrl;

import com.example.springsecuritystudy202301.sample.dto.req.UsrReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@Tag(name = "01. 세션")
@RequestMapping("/session")
public class SessionCtrl {

    private final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    Environment env;

    @Operation(summary = "세션에 값 세팅", description = "set session")
    @PostMapping("/set-session")
    public void setSession(
            HttpServletRequest httpServletRequest
            , @RequestBody UsrReqDto swhJwtReqDto) {
        logger.info("input userId : {}", swhJwtReqDto.getUsrId());
        logger.info("input password : {}", swhJwtReqDto.getPassword());
        httpServletRequest.getSession().setAttribute("userId", swhJwtReqDto.getUsrId());
        httpServletRequest.getSession().setAttribute("password", swhJwtReqDto.getPassword());
    }

    @Operation(summary = "세션 저장 및 읽기", description = "session info")
    @GetMapping("/get-session")
    public ResponseEntity<Map<String, Object>> getSession(HttpServletRequest httpServletRequest, @RequestParam(required = false) String req){
        logger.info("Session id : {}", httpServletRequest.getSession().getId());

        httpServletRequest.getSession().setAttribute("testSessionAttribute", req);

        String sessionVal = (String) httpServletRequest.getSession().getAttribute("testSessionAttribute");
        if(sessionVal != null) logger.info("getSessionAttribute : {}", sessionVal);

        logger.info("##################################### session timeout: {}", httpServletRequest.getSession().getMaxInactiveInterval());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "세션 삭제", description = "delete session")
    @GetMapping("/del")
    public void delSession(
            HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("testSessionAttribute"); // name값에 해당하는 정보 삭제
        httpServletRequest.getSession().invalidate(); // 세션 삭제
    }

    @Operation(summary = "세션 삭제", description = "delete session attr")
    @GetMapping("/delAttr")
    public void delSessionAttr(
            HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("testSessionAttribute"); // name값에 해당하는 정보 삭제
        httpServletRequest.getSession().invalidate(); // 세션 삭제
    }

    @Operation(summary = "세션 만료시간 설정")
    @GetMapping("/set-session-timeout")
    public ResponseEntity<Map<String, Object>> setSessionTimeout(HttpServletRequest httpServletRequest, @RequestParam(required = false) Integer Timesec){
        logger.info("##################################### session timeout: {}", httpServletRequest.getSession().getMaxInactiveInterval());
        httpServletRequest.getSession().setMaxInactiveInterval(Timesec);
        logger.info("##################################### session timeout: {}", httpServletRequest.getSession().getMaxInactiveInterval());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
