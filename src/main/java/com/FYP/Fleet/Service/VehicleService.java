package com.FYP.Fleet.Service;

import com.FYP.Fleet.Dto.Request.VehicleRequestDto;
import com.FYP.Fleet.Dto.MiniResponseDto.VehicleNumberPairResponseDto;
import com.FYP.Fleet.Dto.Response.VehicleResponseDto;
import com.FYP.Fleet.Models.Owner;
import com.FYP.Fleet.Models.Trip;
import com.FYP.Fleet.Models.User;
import com.FYP.Fleet.Models.Vehicle;
import com.FYP.Fleet.Repository.UserRepository;
import com.FYP.Fleet.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final OwnerService ownerService;

    @Autowired
    VehicleService(VehicleRepository vehicleRepository, UserRepository userRepository, UserService userService, OwnerService ownerService){
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.ownerService = ownerService;
    }
    @Transactional
    public Vehicle createVehicle(VehicleRequestDto vehicleRequestDto, long userId) throws UserPrincipalNotFoundException {
        User user = userService.getUserById(userId);
        Vehicle createVehicle = Vehicle.builder()
                .number(vehicleRequestDto.getVehicleNumber())
                .user(user)
                .build();

        if(vehicleRequestDto.getOwnerId() != null){
            Owner owner = ownerService.getOwnerById(vehicleRequestDto.getOwnerId());
            createVehicle.setOwner(owner);
        }
        createVehicle = vehicleRepository.save(createVehicle);
        user.getVehicleList().add(createVehicle);

        userRepository.save(user);

        return createVehicle;

    }

    public VehicleResponseDto getVehicleResponseByVehicleNumber(String number, long userId){
        Vehicle vehicle = vehicleRepository.findByNumberAndUserId(number, userId)
                .orElseThrow(()-> new RuntimeException("Vehicle Do Not Exist"));
        return getVehicleResponse(vehicle);

    }

    public Vehicle getVehicleByVehicleNumber(String number, long userId){
        return vehicleRepository.findByNumberAndUserId(number, userId)
                .orElseThrow(()-> new RuntimeException("Vehicle Do Not Exist"));
    }


    public VehicleResponseDto updateVehicle( long vehicleId, VehicleRequestDto vehicleRequestDto) throws UserPrincipalNotFoundException {
        Vehicle vehicle = getVehicleById(vehicleId);
        if(vehicleRequestDto.getOwnerId() != null){
            Owner owner = ownerService.getOwnerById(vehicleRequestDto.getOwnerId());
            vehicle.setOwner(owner);
        }
        if(vehicleRequestDto.getVehicleNumber() != null){
            vehicle.setNumber(vehicleRequestDto.getVehicleNumber());
        }
        vehicle = vehicleRepository.save(vehicle);
        return getVehicleResponse(vehicle);
    }

    public Vehicle getVehicleById(long id){
        return vehicleRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Vehicle Id Do Not Exist")
        );
    }

    public List<VehicleResponseDto> getAllVehicleByUserId(Long userId) {
        List<Vehicle> vehicleList = vehicleRepository.findByUserId(userId);
        return vehicleList.stream().map(this::getVehicleResponse).toList();
    }

    private VehicleResponseDto getVehicleResponse(Vehicle vehicle){
        return VehicleResponseDto.builder()
                .id(vehicle.getId())
                .vehicleNumber(vehicle.getNumber())
                .tripList(vehicle.getTripList().stream().map(Trip::getId).toList())
                .ownerId(vehicle.getOwner().getId())
                .ownerName(vehicle.getOwner().getName())
                .build();
    }

    public List<VehicleNumberPairResponseDto> getVehicleNumberPairResponse(Long id) {
        List<Vehicle> vehicleList = vehicleRepository.findByUserId(id);
        return vehicleList.stream().map(v->
                VehicleNumberPairResponseDto
                        .builder()
                        .vehicleId(v.getId())
                        .vehicleNumber(v.getNumber())
                        .build()).toList();
    }

}
