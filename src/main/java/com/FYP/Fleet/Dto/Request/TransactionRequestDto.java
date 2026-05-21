package com.FYP.Fleet.Dto.Request;

import com.FYP.Fleet.Enums.Method;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {
    private Long amount;
    private LocalDate date;
    private Long ownerId;
    private String note;
    private Method method;

}
