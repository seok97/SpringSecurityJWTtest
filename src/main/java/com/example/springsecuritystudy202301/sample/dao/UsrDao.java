package com.example.springsecuritystudy202301.sample.dao;

import com.example.springsecuritystudy202301.sample.dto.req.UsrReqDto;
import com.example.springsecuritystudy202301.sample.dto.res.UsrResDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsrDao {
    UsrResDto getUsrById(String usrId);

    List<String> getRoles(String usrId);

    Integer getUsrByIdCount(String usrId);

    void insertUsr(UsrReqDto reqDto);
}
