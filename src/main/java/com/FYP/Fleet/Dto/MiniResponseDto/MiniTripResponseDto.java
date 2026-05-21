package com.FYP.Fleet.Dto.MiniResponseDto;

import com.FYP.Fleet.Enums.Status;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MiniTripResponseDto {
    private Long id;
    private String vehicleNumber;
    private String driverName;
    private String source;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long freightPrice;
    private Long totalExpense;
    private Long ownerRate;
    private Long profit;
    private Status status;
}
