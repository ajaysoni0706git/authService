package com.service.repository;


import com.service.entity.RefreshTokens;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokens, Integer>
{
    Optional<RefreshTokens> findByToken(String token);

}
