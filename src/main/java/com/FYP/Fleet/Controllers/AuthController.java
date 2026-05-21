package com.FYP.Fleet.Controllers;

import com.FYP.Fleet.Dto.Request.LoginRequestDto;
import com.FYP.Fleet.Dto.Request.SignUpRequestDto;
import com.FYP.Fleet.Dto.Response.LoginResponseDto;
import com.FYP.Fleet.Dto.Response.SignUpResponseDto;
import com.FYP.Fleet.Security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        SignUpResponseDto signUpResponseDto = authService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);

    }

    @GetMapping("/verify")
    public ResponseEntity.BodyBuilder verify(){
        return ResponseEntity.status(HttpStatus.OK);
    }

}
