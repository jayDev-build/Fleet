package com.FYP.Fleet.Security;

import com.FYP.Fleet.Dto.Request.LoginRequestDto;
import com.FYP.Fleet.Dto.Request.SignUpRequestDto;
import com.FYP.Fleet.Dto.Response.LoginResponseDto;
import com.FYP.Fleet.Dto.Response.SignUpResponseDto;
import com.FYP.Fleet.Models.SecurityUser;
import com.FYP.Fleet.Models.User;
import com.FYP.Fleet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtils authUtils;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthUtils authUtils){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUtils = authUtils;
    }

    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto){
        Optional<User> userNameExist = userRepository.findByUsername(signUpRequestDto.getUsername());
        if(userNameExist.isPresent()){
            throw new IllegalArgumentException("UserName Already Taken");
        }

        User user = User.builder()
                .username(signUpRequestDto.getUsername())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .build();

        user = userRepository.save(user);

        return SignUpResponseDto.builder()
                .username(user.getUsername())
                .userId(user.getId())
                .build();

    }


    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        User user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(
                ()-> new UsernameNotFoundException("UserName Is Invalid"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        SecurityUser securityUser = new SecurityUser(user);
        String jwt = authUtils.generateToken(securityUser);

        return LoginResponseDto.builder()
                .jwt(jwt)
                .userId(securityUser.getId())
                .build();
    }
}
