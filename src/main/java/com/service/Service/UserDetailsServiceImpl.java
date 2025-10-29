package com.service.Service;

import com.service.entity.User;
import com.service.model.UserDto;
import com.service.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;

@Service
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new CustomUserDetails(user);
    }

    public User checkIfUserExists(UserDto userDto) {
        return userRepo.findByUsername(userDto.getUsername());
    }

    public Boolean signUpUser(UserDto userDto) {

        if(Objects.nonNull(checkIfUserExists(userDto))) {
            return false;
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        String userId = java.util.UUID.randomUUID().toString();
        userRepo.save(new User(
                userId,
                userDto.getUsername(),
                userDto.getPassword(),
                new HashSet<>()
        ));

        return true;
    }
}
