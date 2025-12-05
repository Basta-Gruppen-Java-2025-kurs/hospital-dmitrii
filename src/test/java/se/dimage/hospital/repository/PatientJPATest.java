package se.dimage.hospital.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import se.dimage.hospital.model.Patient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class PatientJPATest {

    @Autowired
    private PatientRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        //arrange
        Patient p1 = new Patient();
        p1.setName("Lucy");
        p1.setPersonalNumber("6152735487");
        repository.save(p1);

        Patient p2 = new Patient();
        p2.setName("Paster");
        p2.setPersonalNumber("178263836402");
        repository.save(p2);

        Patient p3 = new Patient();
        p3.setName("Doris");
        p3.setPersonalNumber("712653-8217");
        repository.save(p3);

        Patient p4 = new Patient();
        p4.setName("Dolores");
        p4.setPersonalNumber("0123456789");
        repository.save(p4);
    }

    @Test
    @DisplayName("Creating a Patient and findById with its id should return correct content")
    public void findByIdTest() {
        final String NAME = "John Door", PN = "1234567890";
        Long savedId;

        //arrange
        Patient p = new Patient();
        p.setName(NAME);
        p.setPersonalNumber(PN);

        //act
        savedId = repository.save(p).getId();
        Patient foundPatient = repository.findById(savedId).orElse(null);
        //assert
        assertNotNull(foundPatient);
        assertEquals(savedId, foundPatient.getId());
        assertEquals(NAME, foundPatient.getName());
        assertEquals(PN, foundPatient.getPersonalNumber());
    }

    @Test
    @DisplayName("search finds the correct patients")
    public void findByNameTest() {
        //arrange
        //act
        List<Patient> foundPatient = repository.search("Lucy", "%", 0, Integer.MAX_VALUE);

        //assert
        assertNotNull(foundPatient);
        assertFalse(foundPatient.isEmpty());
        assertEquals(1, foundPatient.size());
        assertEquals("Lucy", foundPatient.getFirst().getName());
    }

    @Test
    @DisplayName("Repository throws correct exceptions")
    public void exceptionTest() {
        //arrange
        Patient p2 = new Patient();
        p2.setName("Doris");
        p2.setPersonalNumber("0123456789");

        Patient p3 = new Patient();

        //act + assert
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(p2));
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(p3));

    }

    @Test
    @DisplayName("Basic persistence")
    public void basicPersistenceTest() {
        //arrange
        Patient p = new Patient(null, "Gross Fred", "6473856392", List.of());
        //act
        Patient saved = repository.save(p);
        Patient found = repository.findById(saved.getId()).orElse(null);
        //assert
        assertEquals(found, saved);
        assertEquals(saved.getName(), p.getName());
        assertEquals(saved.getPersonalNumber(), p.getPersonalNumber());
        assertEquals(saved.getJournals(), p.getJournals());
    }

    @Test
    @DisplayName("Updating a record")
    public void updatingARecordTest() {
        //arrange
        Patient p = new Patient(null, "Ernest Hemingway", "617283745693", List.of());
        //act/assert
        Patient saved = repository.save(p);
        assertEquals(p.getName(), saved.getName());
        saved.setName("Lando Calrissian");
        repository.save(saved);
        Patient found = repository.findById(saved.getId()).orElse(null);
        //assert
        assertEquals(p.getPersonalNumber(), saved.getPersonalNumber());
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getName(), found.getName());
    }

    @Test
    @DisplayName("Patient deletion")
    public void deletePatientTest() {
        //arrange
        Patient p = new Patient(null, "Antony Webber", "651242-7655", List.of());
        long savedId = repository.save(p).getId();
        long countRecords = repository.count();
        //act
        repository.deleteById(savedId);
        Optional<Patient> found = repository.findById(savedId);
        long newCount = repository.count();
        //assert
        assertTrue(found.isEmpty());
        assertTrue(countRecords > newCount);
    }

    @Test
    @DisplayName("Finding patients by name pattern")
    public void findByNameContainsTest() {
        //arrange
        //act
        List<Patient> found = repository.findByNameContains("Do");
        //assert
        assertEquals(2, found.size());
    }

    @Test
    @DisplayName("Finding patients by personal number pattern")
    public void findByPersonalNumberContainsTest() {
        //arrange
        //act
        List<Patient> found = repository.findByPersonalNumberContains("17");
        //assert
        assertEquals(2, found.size());
    }

}