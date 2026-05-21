package com.FYP.Fleet.Dto.Response;

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
public class OwnerBalanceTripSummaryResponseDto {

    private Long tripId;
    private String source;
    private String destination;
    private Long rate;
    private Boolean settled;
    private String vehicleNumber;
    private Long advance;
}
