package com.example.review.user.filter;

import com.example.review.user.entity.User;
import com.example.review.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImpl getUserDetailsImpl(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Found" + username));
        return new UserDetailsImpl(user);
    }
}
