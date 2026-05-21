package com.FYP.Fleet.Dto.MiniResponseDto;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class VehicleNumberPairResponseDto {
    private Long vehicleId;
    private String vehicleNumber;
}
