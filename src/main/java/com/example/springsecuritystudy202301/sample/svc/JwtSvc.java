package com.example.springsecuritystudy202301.sample.svc;

import com.example.springsecuritystudy202301.sample.dto.req.UsrReqDto;
import com.example.springsecuritystudy202301.sample.dto.res.UsrResDto;

public interface JwtSvc {
    UsrResDto login(UsrReqDto reqDto);

    String signup(UsrReqDto reqDto) throws Exception;
}
