package se.dimage.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.dimage.hospital.model.Journal;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Long> {

    List<Journal> findByRecord(String record);

}
