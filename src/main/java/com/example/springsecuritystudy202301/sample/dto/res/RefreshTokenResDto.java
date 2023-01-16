package com.example.springsecuritystudy202301.sample.dto.res;

import lombok.Data;

@Data
public class RefreshTokenResDto {
    private String keyName;
    private String value;
}