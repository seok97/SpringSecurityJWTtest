package com.example.springsecuritystudy202301.sample.svc.impl;

import com.example.springsecuritystudy202301.common.jwt.JwtTokenProvider;
import com.example.springsecuritystudy202301.sample.dao.UsrDao;
import com.example.springsecuritystudy202301.sample.dto.req.UsrReqDto;
import com.example.springsecuritystudy202301.sample.dto.res.UsrResDto;
import com.example.springsecuritystudy202301.sample.svc.JwtSvc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class JwtSvcImpl implements JwtSvc {

    @Value("${jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    UsrDao usrDao;

    @Override
    public String signup(UsrReqDto reqDto) throws Exception{
        if(usrDao.getUsrByIdCount(reqDto.getUsrId()) > 0) {
            throw new Exception();
        }

        reqDto.setPassword(passwordEncoder.encode(reqDto.getPassword()));
        usrDao.insertUsr(reqDto);

        return reqDto.getPassword();
    }

    @Override
    public UsrResDto login(UsrReqDto reqDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(reqDto.getUsrId(), reqDto.getPassword());

        //authenticationManagerBuilder가 호출되면서 CustomUserDetailService가 로드됨.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> roleList = usrDao.getRoles(reqDto.getUsrId());
        log.info("사용자 권한 : {}", roleList);
        reqDto.setRoleList(roleList);

        String jwtToken = tokenProvider.createToken(reqDto, tokenValidityInSeconds);

        UsrResDto result = new UsrResDto();
        result.setRoleList(roleList);
        result.setAccessToken(tokenProvider.createAccessToken(reqDto));
        result.setRefreshToken(tokenProvider.createRefreshToken(reqDto));
        result.setJwtToken(jwtToken);

        return result;
    }
}
