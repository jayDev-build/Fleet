package com.FYP.Fleet.Dto.Request;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LoginRequestDto {

    private String username;
    private String password;
}
