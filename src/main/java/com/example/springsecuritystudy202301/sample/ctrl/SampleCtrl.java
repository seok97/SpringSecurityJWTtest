package com.example.springsecuritystudy202301.sample.ctrl;

import com.example.springsecuritystudy202301.common.jwt.JwtFilter;
import com.example.springsecuritystudy202301.sample.dto.req.UsrReqDto;
import com.example.springsecuritystudy202301.sample.dto.res.UsrResDto;
import com.example.springsecuritystudy202301.sample.svc.JwtSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Tag(name = "00. 로그인")
@RequestMapping("/test")
public class SampleCtrl {

    @Autowired
    JwtSvc jwtSvc;

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/signin")
    public ResponseEntity<UsrResDto> login(
            @RequestBody UsrReqDto reqDto) {
        UsrResDto resDto = jwtSvc.login(reqDto);
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + reqDto);
        return new ResponseEntity<>(resDto, httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "회원가입", description = "로그인")
    @PostMapping("/signup")
    public String sign(
            @RequestBody UsrReqDto reqDto) throws Exception {
        return jwtSvc.signup(reqDto);
    }

    @Operation(summary = "운영자 권한 체크", description = "운영자 권한 체크")
    @GetMapping("/admin")
    public String login() {
        return "운영자 입니다.";
    }
}
