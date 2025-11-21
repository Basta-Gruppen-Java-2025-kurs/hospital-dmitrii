package se.dimage.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.dimage.hospital.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
