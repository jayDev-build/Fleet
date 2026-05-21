package com.FYP.Fleet.Dto.MiniResponseDto;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverIdPairResponseDto {
    private Long driverId;
    private String driverName;
}
