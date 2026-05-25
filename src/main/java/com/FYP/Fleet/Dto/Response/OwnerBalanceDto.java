package com.FYP.Fleet.Dto.Response;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerBalanceDto {
    private Long ownerId;
    private String ownerName;
    private String ownerPhone;
    private Long totalPaid;
    private Long totalRent;
    private Long amountToPay;
    private String status; // PAYABLE, RECEIVABLE, SETTLED
    private List<OwnerBalanceTripSummaryResponseDto> trips;
}