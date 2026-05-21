package com.FYP.Fleet.Controllers;

import com.FYP.Fleet.Dto.Request.OwnerRequestDto;
import com.FYP.Fleet.Dto.Response.OwnerBalanceDto;
import com.FYP.Fleet.Dto.Response.OwnerResponseDto;
import com.FYP.Fleet.Models.SecurityUser;
import com.FYP.Fleet.Service.OwnerService;
import jakarta.validation.Valid;
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
import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService){
        this.ownerService = ownerService;
    }

    @PostMapping("/")
    public ResponseEntity<OwnerResponseDto> createOwner(@RequestBody @Valid OwnerRequestDto request,
                                                        @AuthenticationPrincipal SecurityUser securityUser) {
        OwnerResponseDto ownerResponseDto = ownerService.createOwner(request, securityUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerResponseDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<OwnerResponseDto>> getAllOwners(@AuthenticationPrincipal SecurityUser securityUser) {
        List<OwnerResponseDto> ownerResponseDtoList = ownerService.getAllOwners(securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ownerResponseDtoList);
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<OwnerResponseDto> getOwner(@PathVariable Long ownerId,@AuthenticationPrincipal SecurityUser securityUser) {
        OwnerResponseDto ownerResponseDto = ownerService.getOwner(ownerId, securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ownerResponseDto);
    }

    @GetMapping("/{ownerId}/balance")
    public ResponseEntity<OwnerBalanceDto> getOwnerBalance(@PathVariable Long ownerId,@AuthenticationPrincipal SecurityUser securityUser) {
        OwnerBalanceDto ownerResponseDto = ownerService.getOwnerBalance(ownerId, securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ownerResponseDto);
    }
}