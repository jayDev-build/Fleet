package com.FYP.Fleet.Service;

import com.FYP.Fleet.Dto.Request.OwnerRequestDto;
import com.FYP.Fleet.Dto.Response.*;
import com.FYP.Fleet.Models.*;
import com.FYP.Fleet.Repository.OwnerRepository;
import com.FYP.Fleet.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final UserService userService;
    private final TripService tripService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository, UserService userService,@Lazy TripService tripService, TransactionRepository transactionRepository){
        this.ownerRepository = ownerRepository;
        this.userService = userService;
        this.tripService = tripService;
        this.transactionRepository = transactionRepository;
    }

    public OwnerResponseDto createOwner(OwnerRequestDto request, Long userId) {
        if (ownerRepository.existsByPhoneAndUserId(request.getPhone(), userId)) {
            throw new RuntimeException("Owner with this phone already exists");
        }

        User user = userService.getUserById(userId);

        Owner owner = Owner.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .user(user)
                .build();

        Owner saved = ownerRepository.save(owner);
        return mapToResponse(saved, userId);
    }

    public List<OwnerResponseDto> getAllOwners(Long userId) {
        return ownerRepository.findByUserId(userId)
                .stream()
                .map(o -> mapToResponse(o, userId))
                .collect(Collectors.toList());
    }

    public OwnerResponseDto getOwner(Long ownerId, Long userId) {
        Owner owner = ownerRepository.findByIdAndUserId(ownerId, userId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        return mapToResponse(owner, userId);
    }

    public OwnerBalanceDto getOwnerBalance(Long ownerId, Long userId) {
        Owner owner = ownerRepository.findByIdAndUserId(ownerId, userId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        List<Transactions> transactionsList = transactionRepository.findTransactionByUserIdAndOwnerId(userId, ownerId);
        List<Trip> trips = tripService
                .getTripsByUserIdAndOwnerId(userId, ownerId);


        long totalRent = trips.stream().mapToLong(Trip::getOwnerRate).sum();
        long totalPaid = transactionsList.stream().mapToLong(Transactions::getAmount).sum();
        long amountToPay = totalRent - totalPaid;

        OwnerBalanceDto dto = new OwnerBalanceDto();
        dto.setOwnerId(owner.getId());
        dto.setOwnerName(owner.getName());
        dto.setOwnerPhone(owner.getPhone());
        dto.setTotalPaid(totalPaid);
        dto.setTotalRent(totalRent);
        dto.setAmountToPay(amountToPay);
        dto.setStatus(amountToPay > 0 ? "RECEIVABLE"
                : amountToPay < 0 ? "PAYABLE"
                : "SETTLED");

        dto.setTrips(trips.stream()
                .map(this::mapToOwnerBalanceTripSummaryResponseDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private OwnerResponseDto mapToResponse(Owner owner, long userId) {
        OwnerResponseDto dto = new OwnerResponseDto();
        dto.setOwnerId(owner.getId());
        dto.setName(owner.getName());
        dto.setPhone(owner.getPhone());
        dto.setAmountToReceive(getOwnerBalance(owner.getId(),userId).getAmountToPay());

        if(owner.getVehicles() != null) {
            dto.setVehicleNumbers(
                    owner.getVehicles().stream()
                            .map(Vehicle::getNumber)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    private OwnerBalanceTripSummaryResponseDto mapToOwnerBalanceTripSummaryResponseDto(Trip trip) {
        return OwnerBalanceTripSummaryResponseDto.builder()
                .tripId(trip.getId())
                .rate(trip.getOwnerRate())
                .source(trip.getSource())
                .destination(trip.getDestination())
                .settled(trip.getSettled())
                .vehicleNumber(trip.getVehicle().getNumber())
//                .advance(trip.getOwnerAdvance())
                .build();
    }

    public Owner getOwnerById(Long ownerId) throws UserPrincipalNotFoundException {
        return ownerRepository.findById(ownerId).orElseThrow(()-> new UserPrincipalNotFoundException("Owner Id Invalid"));
    }

    public Owner getOwnerByVehicleNumberAndUserId(String vehicleNumber, Long userId){
        return ownerRepository.findByVehicleNumberAndUserId(vehicleNumber, userId);
    }


    public Owner getByOwnerIdAndUserId(Long ownerId, Long userId) {
        return ownerRepository.findByIdAndUserId(ownerId, userId)
                .orElseThrow(() -> new RuntimeException("Error Do Not Exist"));
    }
}