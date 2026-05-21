package com.FYP.Fleet.Dto.Request;

import com.FYP.Fleet.Enums.ExpenseType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExpenseRequestDto {
    @NotNull
    private long tripId;
    @NotNull
    private ExpenseType expenseType;
    private String note;
    @NotNull
    private LocalDate date;
    @NotNull
    private Long amount;

}
