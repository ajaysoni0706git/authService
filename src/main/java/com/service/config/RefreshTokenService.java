package com.service.config;

import com.service.entity.RefreshTokens;
import com.service.entity.User;
import com.service.repository.RefreshTokenRepository;
import com.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepo;

    @Autowired
    UserRepository userRepo;

    public RefreshTokens createRefreshToken(String username){
        User user = userRepo.findByUsername(username);
        RefreshTokens refreshTokens = RefreshTokens.builder()
                .userInfo(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(2629800))
                .build();
        return refreshTokenRepo.save(refreshTokens);
    }

    public Optional<RefreshTokens> findByToken(String token){
        return refreshTokenRepo.findByToken(token);
    }

    public RefreshTokens verifyExpiration(RefreshTokens token){
        if(token.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepo.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }

}
