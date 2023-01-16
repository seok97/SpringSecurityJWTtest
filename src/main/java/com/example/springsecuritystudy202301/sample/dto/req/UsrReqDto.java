package com.example.springsecuritystudy202301.sample.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UsrReqDto {
    @Schema(description = "", example = "test01")
    private String usrId;

    @Schema(description = "", example = "1234")
    private String password;

    @JsonIgnore
    private List<String> roleList;
}
