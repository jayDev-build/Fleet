package com.FYP.Fleet.Controllers;

import com.FYP.Fleet.Dto.MiniResponseDto.MiniExpenseResponseDto;
import com.FYP.Fleet.Dto.Request.ExpenseRequestDto;
import com.FYP.Fleet.Dto.Request.UpdateExpenseRequestDto;
import com.FYP.Fleet.Dto.Response.ExpenseResponseDto;
import com.FYP.Fleet.Models.SecurityUser;
import com.FYP.Fleet.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @PostMapping("/")
    public ResponseEntity<MiniExpenseResponseDto> createExpense(@RequestBody ExpenseRequestDto expenseRequestDto,
                                                                @AuthenticationPrincipal SecurityUser securityUser){
        MiniExpenseResponseDto response = expenseService.createExpense(expenseRequestDto, securityUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> getExpenseById(@PathVariable long expenseId){
        ExpenseResponseDto response = expenseService.getExpenseResponseById(expenseId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<MiniExpenseResponseDto>> getTripAllExpense(@PathVariable long tripId){
        List<MiniExpenseResponseDto> expenseList = expenseService.getTripAllExpense(tripId);
        return ResponseEntity.status(HttpStatus.OK).body(expenseList);
    }

    @PatchMapping("/trip/{expenseId}")
    public ResponseEntity.BodyBuilder updateExpense(@PathVariable long expenseId,
                                                    @AuthenticationPrincipal SecurityUser securityUser,
                                                    @RequestBody UpdateExpenseRequestDto updateExpenseRequestDto
    ) throws Exception {
        expenseService.updateExpense(expenseId, securityUser.getId(), updateExpenseRequestDto);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity.BodyBuilder deleteExpense(@PathVariable Long expenseId, @AuthenticationPrincipal SecurityUser securityUser){
        expenseService.deleteExpenseByIdAndUserId(expenseId, securityUser.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }

}
