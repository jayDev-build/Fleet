package com.FYP.Fleet.Service;

import com.FYP.Fleet.Dto.MiniResponseDto.MiniTripResponseDto;
import com.FYP.Fleet.Dto.Request.TripRequestDto;
import com.FYP.Fleet.Dto.Response.TripResponseDto;
import com.FYP.Fleet.Dto.Response.TripStatusResponseDto;
import com.FYP.Fleet.Dto.Response.TripSummaryResponseDto;
import com.FYP.Fleet.Enums.ExpenseType;
import com.FYP.Fleet.Enums.Status;
import com.FYP.Fleet.Models.*;
import com.FYP.Fleet.Repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final UserService userService;
    private final WhatsAppSmsSender whatsAppSmsSenderService;
    private final OwnerService ownerService;

    @Autowired
    public TripService(TripRepository tripRepository,
                       DriverService driverService,
                       VehicleService vehicleService,
                       UserService userService,
                       WhatsAppSmsSender whatsAppSmsSenderService,
                       OwnerService ownerService){
        this.tripRepository = tripRepository;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.whatsAppSmsSenderService = whatsAppSmsSenderService;
        this.ownerService = ownerService;
    }

    @Transactional
    public TripResponseDto  createTrip(TripRequestDto tripRequestDto, long userId){
        Driver driver = driverService.getDriverById(tripRequestDto.getDriverId());
        Vehicle vehicle = vehicleService.getVehicleByNumber(tripRequestDto.getVehicleNumber());
        User user = userService.getUserById(userId);

        Trip trip = Trip.builder()
                .driver(driver)
                .vehicle(vehicle)
                .user(user)
                .source(tripRequestDto.getSource())
                .destination(tripRequestDto.getDestination())
                .freightPrice(tripRequestDto.getFreightPrice())
                .startDate(tripRequestDto.getStartDate())
                .endDate(tripRequestDto.getEndDate())
                .status(Status.CREATED)
                .ownerRate(tripRequestDto.getOwnerRate())
                .recordDateTime(LocalDateTime.now())
//                .ownerAdvance(tripRequestDto.getOwnerAdvance())
                .build();

        //Synchronizing Entities
        trip = tripRepository.save(trip);
        vehicle.getTripList().add(trip);
        user.getTripList().add(trip);
        driver.getTripList().add(trip);

        //increasing ownerBalance
        Owner owner = ownerService.getOwnerByVehicleNumber(tripRequestDto.getVehicleNumber());
        owner.setAmountToReceive(owner.getAmountToReceive() + tripRequestDto.getOwnerRate());

        //Sending Whatsapp mssg
        TripResponseDto tripResponseDto = getTripResponse(trip);
        whatsAppSmsSenderService.tripCreatedConfirmation(tripResponseDto, user.getPhone());
        return tripResponseDto;

    }

    public TripResponseDto getTripResponseById(long tripId, long userId) throws RuntimeException{
        Optional<Trip> tripOptional = tripRepository.findByIdAndUserId(tripId, userId);
        if(tripOptional.isEmpty()){
            throw new RuntimeException("Trip Do Not Exist");
        }
        Trip trip = tripOptional.get();
        return getTripResponse(trip);

    }

    public TripSummaryResponseDto getTripSummaryById(long tripId){
        Trip trip = getTripById(tripId);

        Long freightPrice = trip.getFreightPrice();

        Long dieselExpense = getExpenseByCategory(ExpenseType.DIESEL, trip);
        Long tollExpense = getExpenseByCategory(ExpenseType.TOLL, trip);
        Long driverExpense = getExpenseByCategory(ExpenseType.DRIVER, trip);
        Long otherExpense = getExpenseByCategory(ExpenseType.OTHER, trip);
        Long ownerRate = trip.getOwnerRate();
        Long totalExpense = dieselExpense + tollExpense + driverExpense + otherExpense;

        Long profit = freightPrice - totalExpense - ownerRate;
        return TripSummaryResponseDto.builder()
                .freightPrice(freightPrice)
                .dieselExpense(dieselExpense)
                .tollExpense(tollExpense)
                .driverExpense(driverExpense)
                .otherExpense(otherExpense)
                .totalExpense(totalExpense)
                .profit(profit)
                .ownerRate(ownerRate)
                .build();
    }

    public Trip getTripById(long tripId){
        return tripRepository.findById(tripId).orElseThrow(
                ()-> new RuntimeException("Trip Id Invalid"));
    }

    public Long getTotalExpenseOfTrip(Trip trip){
        Long dieselExpense = getExpenseByCategory(ExpenseType.DIESEL, trip);
        Long tollExpense = getExpenseByCategory(ExpenseType.TOLL, trip);
        Long driverExpense = getExpenseByCategory(ExpenseType.DRIVER, trip);
        Long otherExpense = getExpenseByCategory(ExpenseType.OTHER, trip);

        return dieselExpense + tollExpense + driverExpense + otherExpense;
    }

    public Long getTotalProfitOfTrip(Trip trip){
        Long freightPrice = trip.getFreightPrice();
        Long totalExpense = getTotalExpenseOfTrip(trip);
        return freightPrice - totalExpense - trip.getOwnerRate();
    }

    public Long getExpenseByCategory(ExpenseType expenseType, Trip trip){
        return trip.getExpenseList().stream().filter(t -> t.getExpenseType().equals(expenseType)).mapToLong(Expense::getAmount).sum();
    }

    public List<MiniTripResponseDto> getTripsOfUser(Long userId) {
        List<Trip> trips = tripRepository.findByUserId(userId);
        List<MiniTripResponseDto> activeTrips = trips.stream().filter(t -> t.getStatus().equals(Status.ACTIVE)).map(this::getMiniTripResponse).collect(Collectors.toList());
        List<MiniTripResponseDto> completedTrips = trips.stream().filter(t -> t.getStatus().equals(Status.COMPLETED)).map(this::getMiniTripResponse).collect(Collectors.toList());
        List<MiniTripResponseDto> createdTrips = trips.stream().filter(t -> t.getStatus().equals(Status.CREATED)).map(this::getMiniTripResponse).collect(Collectors.toList());
        activeTrips.sort(Comparator.comparing(MiniTripResponseDto::getEndDate).reversed());
        completedTrips.sort(Comparator.comparing(MiniTripResponseDto::getEndDate).reversed());
        createdTrips.sort(Comparator.comparing(MiniTripResponseDto::getStartDate).reversed());

        List<MiniTripResponseDto> res = new ArrayList<>(activeTrips);
        res.addAll(createdTrips);
        res.addAll(completedTrips);

        return res;
    }

    private TripResponseDto getTripResponse(Trip trip){
        return TripResponseDto.builder()
                .id(trip.getId())
                .driverName(trip.getDriver().getName())
                .vehicleNumber(trip.getVehicle().getNumber())
                .driverId(trip.getDriver().getId())
                .userId(trip.getUser().getId())
                .userName(trip.getUser().getName())
                .source(trip.getSource())
                .destination(trip.getDestination())
                .freightPrice(trip.getFreightPrice())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .expenseList(trip.getExpenseList())
                .totalExpense(getTotalExpenseOfTrip(trip))
                .ownerRate(trip.getOwnerRate())
                .profit(getTotalProfitOfTrip(trip))
                .status(trip.getStatus())
                .build();
    }

    public String getTripStatus(long tripId) {
        Trip trip = getTripById(tripId);
        return trip.getStatus().name();
    }

    public MiniTripResponseDto getMiniTripResponse(Trip trip){
        return MiniTripResponseDto.builder()
                .id(trip.getId())
                .driverName(trip.getDriver().getName())
                .vehicleNumber(trip.getVehicle().getNumber())
                .source(trip.getSource())
                .destination(trip.getDestination())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .freightPrice(trip.getFreightPrice())
                .totalExpense(getTotalExpenseOfTrip(trip))
                .status(trip.getStatus())
                .profit(getTotalProfitOfTrip(trip))
                .ownerRate(trip.getOwnerRate())
                .build();
    }

    public TripStatusResponseDto closeTrip(long tripId, long userId) {
        Trip trip = getTripById(tripId);
        trip.setStatus(Status.COMPLETED);
        trip = tripRepository.save(trip);
        whatsAppSmsSenderService.closeTrip(getMiniTripResponse(trip));
        return TripStatusResponseDto.builder()
                .tripId(tripId)
                .status(trip.getStatus())
                .build();
    }

    public List<Expense> getByIdIn(List<Long> tripIds) {
        return tripRepository.findByIdIn(tripIds);
    }

    public List<Trip> getTripsByUserIdAndOwnerId(Long userId, Long ownerId) {
        return tripRepository.getTripsByUserIdAndOwnerId(userId, ownerId);
    }

    public List<Trip> getTripByUserId(Long userId){
        return tripRepository.findByUserId(userId);
    }

    public void settleTripPayment(long tripId, long userId) {
        Trip trip = getTripByIdAndUserId(tripId, userId);
        trip.setSettled(true);
        tripRepository.save(trip);
    }

    public Trip getTripByIdAndUserId(long tripId, long userId){
        return tripRepository.findByIdAndUserId(tripId, userId).orElseThrow(() -> new RuntimeException("Trip Not Found"));
    }

    public void startTrip(long tripId, Long id) {
        Trip trip = getTripByIdAndUserId(tripId, id);
        trip.setStatus(Status.ACTIVE);
        tripRepository.save(trip);
    }

    public List<Trip> findTripsWhereLastExpenseIsOlderThan24Hours(){
        return tripRepository.findTripsWhereLastExpenseIsOlderThan24Hours(LocalDateTime.now().minusHours(24));
    }
}
