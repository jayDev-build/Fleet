package com.FYP.Fleet.Service;

import com.FYP.Fleet.Dto.MiniResponseDto.MiniExpenseResponseDto;
import com.FYP.Fleet.Dto.Request.ExpenseRequestDto;
import com.FYP.Fleet.Dto.Request.UpdateExpenseRequestDto;
import com.FYP.Fleet.Dto.Response.ExpenseResponseDto;
import com.FYP.Fleet.Enums.Status;
import com.FYP.Fleet.Models.Expense;
import com.FYP.Fleet.Models.Trip;
import com.FYP.Fleet.Repository.ExpenseRepository;
import com.FYP.Fleet.Repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expense")
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripService tripService;


    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, TripService tripService){
        this.expenseRepository = expenseRepository;
        this.tripService = tripService;
    }

    @Transactional
    public MiniExpenseResponseDto createExpense(ExpenseRequestDto expenseRequestDto){
        Trip trip = tripService.getTripById(expenseRequestDto.getTripId());
        Expense expense = Expense.builder()
                .trip(trip)
                .expenseType(expenseRequestDto.getExpenseType())
                .note(expenseRequestDto.getNote())
                .amount(expenseRequestDto.getAmount())
                .date(expenseRequestDto.getDate())
                .recordDateTime(LocalDateTime.now())
                .build();

        expense = expenseRepository.save(expense);
        trip.getExpenseList().add(expense);

        return getMiniExpenseResponseDto(expense);
    }

    public ExpenseResponseDto getExpenseResponseById(long expenseId){
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense Not Found"));
        return getExpenseResponse(expense);

    }

    private Expense getExpenseById(long expenseId){
        return expenseRepository.findById(expenseId).orElseThrow(
                () -> new RuntimeException("Expense Id Do Not Exist")
        );
    }

    public List<MiniExpenseResponseDto> getTripAllExpense(long tripId) {
        Trip trip = tripService.getTripById(tripId);
        List<Expense> expenseList = trip.getExpenseList();
        return expenseList.stream().map(this::getMiniExpenseResponseDto).toList();
    }

    private ExpenseResponseDto getExpenseResponse(Expense expense){
        return ExpenseResponseDto.builder()
                .expenseId(expense.getId())
                .tripId(expense.getTrip().getId())
                .driverId(expense.getTrip().getDriver().getId())
                .userId(expense.getTrip().getUser().getId())
                .userName(expense.getTrip().getUser().getName())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .source(expense.getTrip().getSource())
                .destination(expense.getTrip().getDestination())
                .note(expense.getNote())
                .expenseType(expense.getExpenseType())
                .build();
    }

    private MiniExpenseResponseDto getMiniExpenseResponseDto(Expense expense){
        return MiniExpenseResponseDto.builder()
                .expenseId(expense.getId())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .note(expense.getNote())
                .expenseType(expense.getExpenseType())
                .build();
    }

    public List<Expense> getByTripIdIn(List<Long> tripIds) {
        return tripService.getByIdIn(tripIds);
    }

    public void updateExpense(long expenseId, Long userId, UpdateExpenseRequestDto updateExpenseRequestDto) throws Exception{
        Expense expense = getExpenseByIdAndUserId(expenseId, userId);
        Trip trip = expense.getTrip();
        if(!trip.getStatus().equals(Status.ACTIVE)){
            throw new RuntimeException("Only Active Trips Expenses Can Be Modify");
        }
        if(updateExpenseRequestDto.getExpenseType() != null) expense.setExpenseType(updateExpenseRequestDto.getExpenseType());
        if(updateExpenseRequestDto.getAmount() != null) expense.setAmount(updateExpenseRequestDto.getAmount());
        if(updateExpenseRequestDto.getDate() != null) expense.setDate(updateExpenseRequestDto.getDate());
        expense.setRecordDateTime(LocalDateTime.now());

        expenseRepository.save(expense);

    }

    private Expense getExpenseByIdAndUserId(long expenseId, Long userId) {
        return expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(()-> new RuntimeException("Expense Not Found"));
    }

    public void deleteExpenseByIdAndUserId(Long expenseId, Long userId) {
        Expense expense = getExpenseByIdAndUserId(expenseId, userId);
        expenseRepository.delete(expense);
    }
}
