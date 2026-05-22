package com.induwara.store.services;

import com.induwara.store.dtos.LoginUserRequest;
import com.induwara.store.exceptions.UserNotFoundException;
import com.induwara.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean validateUser(LoginUserRequest request){
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if(user == null){
            throw new UserNotFoundException();
        }

        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
