package se.dimage.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.dimage.hospital.model.AppUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
