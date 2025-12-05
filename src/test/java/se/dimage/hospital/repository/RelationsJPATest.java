package se.dimage.hospital.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import se.dimage.hospital.model.Journal;
import se.dimage.hospital.model.Patient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RelationsJPATest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private JournalRepository journalRepository;

    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();
        journalRepository.deleteAll();

        Patient p1 = new Patient(null, "Lucy", "6152735487", List.of());
        patientRepository.save(p1);

        Patient p2 = new Patient(null, "Paster", "178263836402", List.of());
        patientRepository.save(p2);

        Patient p3 = new Patient(null, "Doris", "712653-8217", List.of());
        patientRepository.save(p3);

        Patient p4 = new Patient(null, "Dolores", "0123456789", List.of());
        patientRepository.save(p4);

        Journal j1 = new Journal(null, p1, "Pregnant");
        journalRepository.save(j1);

        Journal j2 = new Journal(null, p1, "Dizzy");
        journalRepository.save(j2);

        Journal j3 = new Journal(null, p2, "Brain dead");
        journalRepository.save(j3);

        Journal j4 = new Journal(null, p2, "Pneumonia");
        journalRepository.save(j4);

        Journal j5 = new Journal(null, p4, "Dizzy");
        journalRepository.save(j5);
    }

    @Test
    @DisplayName("Many to one relation test")
    void manyToOneRelationTest() {
        List<Journal> found1 = journalRepository.findByRecord("Pneumonia");
        List<Journal> found2 = journalRepository.findByRecord("Dizzy");

        assertFalse(found1.isEmpty());
        assertFalse(found2.isEmpty());
        assertEquals("Paster", found1.getFirst().getPatient().getName());
        List<String> found2Names = found2.stream().map(j -> j.getPatient().getName()).toList();
        assertTrue(found2Names.contains("Lucy"));
        assertTrue(found2Names.contains("Dolores"));
    }

    @Test
    @DisplayName("One to many relation test")
    void oneToManyRelationTest() {
        //arrange
        Patient p = new Patient(null, "Gabriel", "1234567890", List.of());
        List<Journal> journals = new ArrayList<>();
        journals.add(new Journal(null, p, "born"));
        journals.add(new Journal(null, p, "coughing"));
        journals.add(new Journal(null, p, "sick"));
        journals.add(new Journal(null, p, "dying"));
        journals.add(new Journal(null, p, "dead"));
        p.setJournals(journals);
        //act
        Patient saved = patientRepository.save(p);
        //assert
        assertNotNull(saved);
        assertEquals("Gabriel", saved.getName());
        List<Journal> savedJournals = saved.getJournals();
        assertFalse(savedJournals.isEmpty());
        assertEquals(5, savedJournals.size());
        assertFalse(savedJournals.stream().map(j -> j.getPatient().getName().equals("Gabriel")).toList().contains(false));
    }
}
