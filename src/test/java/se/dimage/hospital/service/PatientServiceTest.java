package se.dimage.hospital.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import se.dimage.hospital.dto.PatientRequestDTO;
import se.dimage.hospital.dto.PatientResponseDTO;
import se.dimage.hospital.dto.PatientWithJournalsResponseDTO;
import se.dimage.hospital.exception.PatientNotFoundException;
import se.dimage.hospital.mapper.PatientMapper;
import se.dimage.hospital.model.Patient;
import se.dimage.hospital.repository.PatientRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    PatientRepository repository;

    @Spy
    PatientMapper mapper = Mappers.getMapper(PatientMapper.class);

    @InjectMocks
    PatientService service;


    private Patient patientIn, patientOut, savedPatient;
    private PatientWithJournalsResponseDTO dtoWithJournals;
    private PatientResponseDTO dto;
    private PatientRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        patientOut = new Patient(1L, "Sully", "1234567890", List.of());
        dtoWithJournals = new PatientWithJournalsResponseDTO(1L, "Sully", "1234567890", List.of());
        requestDTO = new PatientRequestDTO("Santa Claws", "0987654321");
        patientIn = mapper.toEntity(requestDTO);
        savedPatient = mapper.toEntity(requestDTO);
        savedPatient.setId(1L);
        dto = mapper.toResponseDto(savedPatient);
    }

    @Test
    @DisplayName("getting a patient by id from repository should yield correct patient")
    void getPatientWithJournal() {
        //arrange
        when(repository.findById(1L)).thenReturn(Optional.of(patientOut));
        //act
        PatientWithJournalsResponseDTO responseDTO = service.getPatientWithJournal(1L);
        //assert
        assertEquals(dtoWithJournals, responseDTO);
    }

    @Test
    @DisplayName("service.add(requestDTO) should call repository.save(patient)")
    void add() {
        //arrange
        when(repository.save(eq(patientIn))).thenReturn(savedPatient);

        //act
        PatientResponseDTO responseDTO = service.add(requestDTO);

        //assert
        assertEquals(dto, responseDTO);
        verify(repository).save(eq(patientIn));
    }

    @Test
    @DisplayName("Service should throw exception when the repository returns empty Optional<Patient>")
    void getPatientWithJournalThrowsNotFoundException() {
        //arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        //act+assert
        PatientNotFoundException thrown = assertThrows(PatientNotFoundException.class, () -> service.getPatientWithJournal(1L));
        assertEquals("Patient # 1 not found.", thrown.getMessage());
    }

    @Test
    @DisplayName("Service should call repository.delete(1L) once, and not repository.save()")
    void serviceDeleteCallsRepositoryDelete() {
        //arrange
        when(repository.existsById(1L)).thenReturn(true);
        //act
        boolean result = service.delete(1L);
        //assert
        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
        verify(repository, never()).save(any());
    }
}