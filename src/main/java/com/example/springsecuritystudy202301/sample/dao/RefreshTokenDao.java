package com.example.springsecuritystudy202301.sample.dao;

import com.example.springsecuritystudy202301.sample.dto.res.RefreshTokenResDto;

public interface RefreshTokenDao {
    RefreshTokenResDto getRefreshTokenByKeyName(String keyName);
}