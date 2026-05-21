package com.FYP.Fleet.Dto.Response;

import com.FYP.Fleet.Enums.ExpenseType;
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
public class ExpenseResponseDto {
    private long expenseId;
    private long tripId;
    private long driverId;
    private long userId;
    private String userName;
    private Long amount;
    private LocalDate date;
    private String source;
    private String destination;
    private String note;
    private ExpenseType expenseType;
}
