package com.FYP.Fleet.Dto.Response;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBalanceResponseDto {
    private String username;
    private Integer totalTrips;
    private Integer totalActiveTrips;
    private Long totalFreightEarned;
    private Long bookedProfit;
    private Long estimatedProfit;
//    private List<MiniTripResponseDto> trips;
}
