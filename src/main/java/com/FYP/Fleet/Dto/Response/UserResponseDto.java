package com.FYP.Fleet.Dto.Response;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDto {

    private long userId;
    private String name;
    private String phone;
    private String companyName;
    private String city;
    private String state;
    private List<String> vehicleList;
    private List<String> driverList;
    private List<Long> tripList;


}
