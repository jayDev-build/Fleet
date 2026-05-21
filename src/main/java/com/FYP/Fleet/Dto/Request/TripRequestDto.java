package com.FYP.Fleet.Dto.Request;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TripRequestDto {

    private long driverId;
    private String vehicleNumber;
    private String source;
    private String destination;
    private Long freightPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long ownerRate;
    private Long ownerAdvance;
}
