package com.interview.userservice.security;

import com.interview.userservice.constants.MessageConstants;
import com.interview.userservice.entity.User;
import com.interview.userservice.exception.UserNotFoundException;
import com.interview.userservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UserNotFoundException(MessageConstants.USER_NOT_FOUND));
        return new UserPrincipal(user);
    }
}
