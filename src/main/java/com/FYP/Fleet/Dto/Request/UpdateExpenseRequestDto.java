package com.FYP.Fleet.Dto.Request;

import com.FYP.Fleet.Enums.ExpenseType;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateExpenseRequestDto {

    private Long expenseId;
    private Long amount;
    private ExpenseType expenseType;
    private String note;
    private LocalDate date;


}
