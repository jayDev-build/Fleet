package com.FYP.Fleet.Controllers;

import com.FYP.Fleet.Dto.Request.TransactionRequestDto;
import com.FYP.Fleet.Dto.Response.TransactionResponseDto;
import com.FYP.Fleet.Models.SecurityUser;
import com.FYP.Fleet.Service.TransactionService;
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
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/")
    public ResponseEntity<TransactionResponseDto> createTransactions(@RequestBody TransactionRequestDto transactionRequestDto,
                                                                     @AuthenticationPrincipal SecurityUser securityUser) throws UserPrincipalNotFoundException {
        TransactionResponseDto transactionResponseDto = transactionService.createTransaction(transactionRequestDto, securityUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDto);
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByOwnerId(@PathVariable Long ownerId,
                                                                                 @AuthenticationPrincipal SecurityUser securityUser) throws UserPrincipalNotFoundException {
        List<TransactionResponseDto> transactionResponseDtoList = transactionService.getTransactionByUserIdAndOwnerId(securityUser.getId(), ownerId);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDtoList);
    }
}
