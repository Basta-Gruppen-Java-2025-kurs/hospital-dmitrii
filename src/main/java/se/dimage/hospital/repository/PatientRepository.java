package se.dimage.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.dimage.hospital.model.Patient;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE p.name LIKE :namePattern AND p.personalNumber LIKE :numberPattern AND SIZE(p.journals) BETWEEN :minJournals AND :maxJournals")
    List<Patient> search(@Param("namePattern") String namePattern, @Param("numberPattern") String numberPattern, @Param("minJournals") Integer minJournals, @Param("maxJournals") Integer maxJournals);

    List<Patient> findByNameContains(String namePattern);

    List<Patient> findByPersonalNumberContains(String numberPattern);

    List<Patient> findByNameAndPersonalNumber(String name, String personalNumber);
}
