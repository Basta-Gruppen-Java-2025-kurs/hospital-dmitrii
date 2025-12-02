package se.dimage.hospital.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import se.dimage.hospital.model.Patient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest
class PatientJPATest {

    @Autowired
    private PatientRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
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
        Patient p1 = new Patient();
        p1.setName("Dolores");
        p1.setPersonalNumber("0123456789");

        Patient p2 = new Patient();
        p2.setName("Doris");
        p2.setPersonalNumber("0123456789");

        Patient p3 = new Patient();

        //act + assert
        repository.save(p1);
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(p2));
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(p3));

    }


}