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
public class OwnerResponseDto {
    private Long ownerId;
    private String name;
    private String phone;
    private List<String> vehicleNumbers; // list of truck numbers they own
    private Long totalFreightEarned;
    private Long totalExpenses;
    private Long amountToReceive; // positive = you owe them, negative = they owe you
}