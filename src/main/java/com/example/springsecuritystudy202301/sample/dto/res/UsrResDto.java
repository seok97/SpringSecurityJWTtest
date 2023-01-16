package com.example.springsecuritystudy202301.sample.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UsrResDto {
    @Schema(description = "", example = "test01")
    private String usrId;

    @Schema(description = "", example = "1234")
    private String pswd;

    @JsonIgnore
    private List<String> roleList;

    private String accessToken;

    private String refreshToken;

    private String jwtToken;
}
