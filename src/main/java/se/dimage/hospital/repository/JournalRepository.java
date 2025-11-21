package se.dimage.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.dimage.hospital.model.Journal;

public interface JournalRepository extends JpaRepository<Journal, Long> {

}
