package com.FYP.Fleet.Controllers;

import com.FYP.Fleet.Dto.Request.UserRequestDto;
import com.FYP.Fleet.Dto.Response.UserBalanceResponseDto;
import com.FYP.Fleet.Dto.Response.UserResponseDto;
import com.FYP.Fleet.Models.SecurityUser;
import com.FYP.Fleet.Models.User;
import com.FYP.Fleet.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/profile/update")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto, @AuthenticationPrincipal SecurityUser securityUser){
        UserResponseDto user = userService.createUser(userRequestDto, securityUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserById(@AuthenticationPrincipal SecurityUser securityUser){
        UserResponseDto userResponseDto = userService.getUserResponseById(securityUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<UserBalanceResponseDto> getDashboardStatOfUser(@AuthenticationPrincipal SecurityUser securityUser){
        UserBalanceResponseDto balance = userService.getDashboardStats(securityUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }
}
