package com.FYP.Fleet.Service;

import com.FYP.Fleet.Dto.MiniResponseDto.MiniTripResponseDto;
import com.FYP.Fleet.Dto.Request.UserRequestDto;
import com.FYP.Fleet.Dto.Response.UserBalanceResponseDto;
import com.FYP.Fleet.Dto.Response.UserResponseDto;
import com.FYP.Fleet.Enums.ExpenseType;
import com.FYP.Fleet.Enums.Status;
import com.FYP.Fleet.Models.*;
import com.FYP.Fleet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TripService tripService;

    @Autowired
    UserService(UserRepository userRepository,@Lazy TripService tripService){
        this.userRepository = userRepository;
        this.tripService = tripService;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto, long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new  UsernameNotFoundException("UserId is invalid"));
        if(userRequestDto.getName() != null) user.setName(userRequestDto.getName());
        if(userRequestDto.getPhone() != null) user.setPhone(userRequestDto.getPhone());
        if(userRequestDto.getCompanyName() != null) user.setCompanyName(userRequestDto.getCompanyName());
        if(userRequestDto.getCity() != null) user.setCity(userRequestDto.getCity());
        if(userRequestDto.getState() != null) user.setState(userRequestDto.getState());
        user = userRepository.save(user);
        return getUserResponse(user);
    }

    public UserResponseDto getUserResponseById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User do not exist"));
        return getUserResponse(user);

    }

    public User getUserById(long userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User do not exist"));
    }

    private UserResponseDto getUserResponse(User user){
        return UserResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .companyName(user.getCompanyName() != null ? user.getCompanyName() : "")
                .city(user.getCity() != null ? user.getCity() : "")
                .state(user.getState() != null ? user.getState() : "")
                .vehicleList(user.getVehicleList().stream().map(Vehicle::getNumber).toList())
                .driverList(user.getDriverList().stream().map(Driver::getName).toList())
                .tripList(user.getTripList().stream().map(Trip::getId).toList())
                .build();
    }

    public UserBalanceResponseDto getDashboardStats(Long userId) {
        List<Trip> allTrips = tripService.getTripByUserId(userId);

        // Split by status
        List<Trip> completedTrips = allTrips.stream().filter(t -> t.getStatus() == Status.COMPLETED).toList();
        List<Trip> activeTrips = allTrips.stream().filter(t -> t.getStatus() == Status.ACTIVE).toList();

        // --- FINANCIALS: COMPLETED (Finalized) ---
        Long completedFreight = completedTrips.stream().mapToLong(t -> nvl(t.getFreightPrice())).sum();
        Long completedOwnerRate = completedTrips.stream().mapToLong(t -> nvl(t.getOwnerRate())).sum();
        Long completedExpenses = sumAllExpenses(completedTrips);
        Long bookedProfit = completedFreight - completedExpenses - completedOwnerRate;

        // --- FINANCIALS: ACTIVE (Estimated) ---
        Long activeFreight = activeTrips.stream().mapToLong(t -> nvl(t.getFreightPrice())).sum();
        Long activeOwnerRate = activeTrips.stream().mapToLong(t -> nvl(t.getOwnerRate())).sum();
        Long activeExpenses = sumAllExpenses(activeTrips);
        // Estimated Profit = What we expect minus what we've already spent
        Long estimatedProfit = activeFreight - activeExpenses - activeOwnerRate;

        return UserBalanceResponseDto.builder()
                .totalActiveTrips(activeTrips.size())
                .totalTrips(allTrips.size())
                .totalFreightEarned(completedFreight + activeFreight) // Total Booked Pipeline
                .bookedProfit(bookedProfit)       // Real money saved
                .estimatedProfit(estimatedProfit) // Money on the road
//                .amountToPay(totalAmountToPay)    // Total debt to truck owners
                .build();
    }

    // Helper to sum all expenses in one pass
    private Long sumAllExpenses(List<Trip> trips) {
        return trips.stream()
                .flatMap(t -> t.getExpenseList().stream())
                .mapToLong(e -> nvl(e.getAmount()))
                .sum();
    }

    private long nvl(Long val) {
        return val == null ? 0L : val;
    }


}
