package com.FYP.Fleet.Controllers;

import com.FYP.Fleet.Dto.MiniResponseDto.MiniTripResponseDto;
import com.FYP.Fleet.Dto.Request.TripRequestDto;
import com.FYP.Fleet.Dto.Response.TripResponseDto;
import com.FYP.Fleet.Dto.Response.TripStatusResponseDto;
import com.FYP.Fleet.Dto.Response.TripSummaryResponseDto;
import com.FYP.Fleet.Models.SecurityUser;
import com.FYP.Fleet.Service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trip")
public class TripController {

    public final TripService tripService;

    public TripController(TripService tripService){
        this.tripService = tripService;
    }

    @PostMapping("/")
    public ResponseEntity<TripResponseDto> createTrip(@RequestBody TripRequestDto tripRequestDto, @AuthenticationPrincipal SecurityUser securityUser){
        TripResponseDto trip = tripService.createTrip(tripRequestDto, securityUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(trip);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponseDto> getTrip(@PathVariable long tripId){
        TripResponseDto trip = tripService.getTripResponseById(tripId);
        return ResponseEntity.status(HttpStatus.OK).body(trip);
    }

    @GetMapping("/summary/{tripId}")
    public ResponseEntity<TripSummaryResponseDto> getTripSummary(@PathVariable long tripId){
        TripSummaryResponseDto summary = tripService.getTripSummaryById(tripId);
        return ResponseEntity.status(HttpStatus.OK).body(summary);
    }

    @GetMapping("/")
    public ResponseEntity<List<MiniTripResponseDto>> getAllTripsOfUser(@AuthenticationPrincipal SecurityUser securityUser){
        List<MiniTripResponseDto> trips = tripService.getTripsOfUser(securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(trips);
    }

    @GetMapping("/status/{tripId}")
    public ResponseEntity<String> getTripStatus(@PathVariable long tripId){
        String status = tripService.getTripStatus(tripId);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @PatchMapping("/status/{tripId}/close")
    public ResponseEntity<TripStatusResponseDto> closeTrip(@PathVariable long tripId, @AuthenticationPrincipal SecurityUser securityUser){
        TripStatusResponseDto responseDto = tripService.closeTrip(tripId, securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/status/{tripId}/start")
    public ResponseEntity.BodyBuilder startTrip(@PathVariable long tripId, @AuthenticationPrincipal SecurityUser securityUser){
        tripService.startTrip(tripId, securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK);
    }

    @PatchMapping("/{tripId}/settle")
    public ResponseEntity.BodyBuilder settleTripPayment(@PathVariable long tripId, @AuthenticationPrincipal SecurityUser securityUser){
        tripService.settleTripPayment(tripId, securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK);
    }

}
