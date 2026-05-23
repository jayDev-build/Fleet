package com.FYP.Fleet.Service;

import com.FYP.Fleet.Dto.Request.TransactionRequestDto;
import com.FYP.Fleet.Dto.Response.TransactionResponseDto;
import com.FYP.Fleet.Models.Owner;
import com.FYP.Fleet.Models.Transactions;
import com.FYP.Fleet.Models.User;
import com.FYP.Fleet.Repository.OwnerRepository;
import com.FYP.Fleet.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final OwnerService ownerService;
    private final UserService userService;
    private final WhatsAppSmsSender whatsAppSmsSenderService;

    @Autowired
    TransactionService(TransactionRepository transactionRepository, OwnerService ownerService, UserService userService, WhatsAppSmsSender whatsAppSmsSenderService){
        this.transactionRepository = transactionRepository;
        this.ownerService = ownerService;
        this.userService = userService;
        this.whatsAppSmsSenderService = whatsAppSmsSenderService;
    }

    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto transactionRequestDto, Long userId) throws UserPrincipalNotFoundException {
        Owner owner = ownerService.getByOwnerIdAndUserId(transactionRequestDto.getOwnerId(), userId);
        User user = userService.getUserById(userId);
        Transactions transactions = Transactions.builder()
                .user(user)
                .owner(owner)
                .date(transactionRequestDto.getDate())
                .note(transactionRequestDto.getNote())
                .amount(transactionRequestDto.getAmount())
                .method(transactionRequestDto.getMethod())
                .recordDateTime(LocalDateTime.now())
                .build();


        //saving transaction
        transactions = transactionRepository.save(transactions);
        TransactionResponseDto responseDto = generateTransactionResponse(transactions);

        //sending whatsapp update
            whatsAppSmsSenderService.addTransaction(responseDto, ownerService.getOwnerBalance(owner.getId(), userId).getAmountToPay(), user.getPhone());
        return responseDto;
    }

    public List<TransactionResponseDto> getTransactionByUserIdAndOwnerId(Long userId, Long ownerId){
        List<TransactionResponseDto> res = transactionRepository.findTransactionByUserIdAndOwnerId(userId, ownerId)
                .stream().map(this::generateTransactionResponse).collect(Collectors.toList());
        res.sort(Comparator.comparing(TransactionResponseDto :: getRecordDateTime).reversed());
        return res;
    }

    public TransactionResponseDto generateTransactionResponse(Transactions transactions){
        return TransactionResponseDto.builder()
                .ownerId(transactions.getOwner().getId())
                .amount(transactions.getAmount())
                .date(transactions.getDate())
                .note(transactions.getNote())
                .ownerName(transactions.getOwner().getName())
                .method(transactions.getMethod())
                .recordDateTime(transactions.getRecordDateTime())
                .build();
    }
}
