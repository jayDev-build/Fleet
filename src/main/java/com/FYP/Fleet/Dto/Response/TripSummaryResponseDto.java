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
public class TripSummaryResponseDto {
    private Long freightPrice;
    private Long dieselExpense;
    private Long tollExpense;
    private Long driverExpense;
    private Long otherExpense;
    private Long totalExpense;
    private Long ownerRate;
    private Long profit;
}
