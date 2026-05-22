package com.FYP.Fleet.Controllers;

import com.FYP.Fleet.Dto.Request.VehicleRequestDto;
import com.FYP.Fleet.Dto.MiniResponseDto.VehicleNumberPairResponseDto;
import com.FYP.Fleet.Dto.Response.VehicleResponseDto;
import com.FYP.Fleet.Models.SecurityUser;
import com.FYP.Fleet.Models.Vehicle;
import com.FYP.Fleet.Service.VehicleService;
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
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    public final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

    @PostMapping("/")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody VehicleRequestDto vehicleRequestDto, @AuthenticationPrincipal SecurityUser securityUser) throws Exception{
        Vehicle vehicle = vehicleService.createVehicle(vehicleRequestDto, securityUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @GetMapping("/")
    public ResponseEntity<List<VehicleResponseDto>> getAllVehicleOfUser(@AuthenticationPrincipal SecurityUser securityUser){
        List<VehicleResponseDto> vehicleList = vehicleService.getAllVehicleByUserId(securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(vehicleList);
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<VehicleResponseDto> getVehicleByNumber(@PathVariable String number, @AuthenticationPrincipal SecurityUser securityUser) {
        VehicleResponseDto vehicle = vehicleService.getVehicleResponseByVehicleNumber(number, securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(vehicle);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(vehicle);
    }

    @PatchMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDto> updateVehicle(@PathVariable long vehicleId,@RequestBody VehicleRequestDto vehicleRequestDto) throws UserPrincipalNotFoundException {
        VehicleResponseDto responseDto = vehicleService.updateVehicle(vehicleId, vehicleRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleNumberPairResponseDto>> vehicleNumberPairResponse(@AuthenticationPrincipal SecurityUser securityUser){
        List<VehicleNumberPairResponseDto> vehicleNumberPairResponseDtoList = vehicleService.getVehicleNumberPairResponse(securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(vehicleNumberPairResponseDtoList);
    }
}
