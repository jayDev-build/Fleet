package com.FYP.Fleet.Dto.Request;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DriverRequestDto {

    private String name;
    private String phone;
}

