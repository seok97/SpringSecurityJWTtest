package com.example.springsecuritystudy202301.sample.dao;

import com.example.springsecuritystudy202301.sample.dto.res.RoleUsrMapgResDto;

public interface RoleUsrMapgDao {
    RoleUsrMapgResDto getRoleUsrMapgByUsrId(String usrId);
}
