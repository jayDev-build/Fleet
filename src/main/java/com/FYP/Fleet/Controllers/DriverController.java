package com.FYP.Fleet.Controllers;

import com.FYP.Fleet.Dto.Request.DriverRequestDto;
import com.FYP.Fleet.Dto.MiniResponseDto.DriverIdPairResponseDto;
import com.FYP.Fleet.Dto.Response.DriverResponseDto;
import com.FYP.Fleet.Models.SecurityUser;
import com.FYP.Fleet.Service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@RestController
@RequestMapping("/driver")
public class DriverController {

    public final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService){
        this.driverService = driverService;
    }

    @PostMapping("/")
    public ResponseEntity<DriverResponseDto> createDriver(@RequestBody DriverRequestDto driverRequestDto, @AuthenticationPrincipal SecurityUser securityUser){
        DriverResponseDto driver = driverService.createDriver(driverRequestDto, securityUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(driver);
    }

    @GetMapping("/")
    public ResponseEntity<List<DriverResponseDto>> getAllDriverOfUser(@AuthenticationPrincipal SecurityUser securityUser){
        List<DriverResponseDto> driverList = driverService.getAllDriverOfUser(securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(driverList);
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverResponseDto> getDriverById(@PathVariable long driverId){
        DriverResponseDto driver = driverService.getDriverResponseById(driverId);
        return ResponseEntity.status(HttpStatus.OK).body(driver);
    }

    @GetMapping("/drivers")
    public ResponseEntity<List<DriverIdPairResponseDto>> getDriverPairId(@AuthenticationPrincipal SecurityUser securityUser){
        List<DriverIdPairResponseDto> responses = driverService.getDriverIdPairResponse(securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
