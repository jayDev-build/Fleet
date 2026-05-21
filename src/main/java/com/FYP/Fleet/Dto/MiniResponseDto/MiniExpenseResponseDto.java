package com.FYP.Fleet.Dto.MiniResponseDto;

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
public class MiniExpenseResponseDto {
    private long expenseId;
    private Long amount;
    private LocalDate date;
    private String note;
    private ExpenseType expenseType;
}
