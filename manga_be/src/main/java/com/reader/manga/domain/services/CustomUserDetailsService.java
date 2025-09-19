package com.reader.manga.domain.services;

import com.reader.manga.ports.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository jpaUserRepository;
    private final Pattern patternEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        if (isEmail(usernameOrEmail))
            return jpaUserRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException(usernameOrEmail));

        return jpaUserRepository.findByUsername(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(usernameOrEmail));
    }

    public boolean isEmail(String usernameOrEmail) {
        boolean matches = patternEmail.matcher(usernameOrEmail).matches();
        return matches;
    }

}
