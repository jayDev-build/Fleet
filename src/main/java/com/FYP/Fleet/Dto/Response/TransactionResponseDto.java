package com.FYP.Fleet.Dto.Response;

import com.FYP.Fleet.Enums.Method;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {

    private Long transactionId;
    private Long amount;
    private String note;
    private LocalDate date;
    private Long ownerId;
    private String ownerName;
    private Method method;
    private LocalDateTime recordDateTime;

}
