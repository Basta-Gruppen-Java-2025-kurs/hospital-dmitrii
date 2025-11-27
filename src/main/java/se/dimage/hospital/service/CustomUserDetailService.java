package se.dimage.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.dimage.hospital.exception.UserNotFoundException;
import se.dimage.hospital.model.AppUser;
import se.dimage.hospital.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        return new User(user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList());
    }

    public void updateRefreshToken(String username, String refreshToken) {
        AppUser user = repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        user.setRefreshToken(refreshToken);
        repository.save(user);
    }

    public boolean isRefreshTokenGood(String username, String refreshToken) {
        AppUser user = repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return refreshToken.equals(user.getRefreshToken());
    }
}
