package com.service.controller;

import com.service.Service.UserDetailsServiceImpl;
import com.service.config.JwtService;
import com.service.config.RefreshTokenService;
import com.service.entity.RefreshTokens;
import com.service.entity.User;
import com.service.model.UserDto;
import com.service.requestDto.AuthRequestDto;
import com.service.requestDto.RefreshTokenRequestDto;
import com.service.responseDto.JwtResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("auth/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        try {
            String userId = userDetailsService.signUpUser(userDto);

            if(Objects.isNull(userId)) {
                return new ResponseEntity<>("Already exists", HttpStatus.BAD_REQUEST);
            }
            RefreshTokens refreshTokens = refreshTokenService.createRefreshToken(userDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userDto.getUsername());

            return new ResponseEntity<>(JwtResponseDto.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshTokens.getToken())
                    .userId(userId)
                    .build(), HttpStatus.OK
                    );
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));

        if(authentication.isAuthenticated()) {
            RefreshTokens refreshTokens = refreshTokenService.createRefreshToken(authRequestDto.getUsername());
            String jwtToken = jwtService.GenerateToken(authRequestDto.getUsername());
            User user = userDetailsService.loadUserByUsername(authRequestDto.getUsername()).getUsername();

            return new ResponseEntity<>(JwtResponseDto.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshTokens.getToken())
                    .userId(user.getUserId().toString())
                    .build(), HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
