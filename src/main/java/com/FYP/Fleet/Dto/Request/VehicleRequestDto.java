package com.FYP.Fleet.Dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VehicleRequestDto {

    @NotNull
    private String vehicleNumber;

    private Long ownerId;
}
