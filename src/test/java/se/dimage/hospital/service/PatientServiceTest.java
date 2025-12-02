package se.dimage.hospital.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import se.dimage.hospital.dto.PatientRequestDTO;
import se.dimage.hospital.dto.PatientResponseDTO;
import se.dimage.hospital.mapper.PatientMapper;
import se.dimage.hospital.model.Patient;
import se.dimage.hospital.repository.PatientRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    PatientRepository repository;

    @Spy
    PatientMapper mapper = Mappers.getMapper(PatientMapper.class);

    @InjectMocks
    PatientService service;


    private Patient patientIn, patientOut;
    private PatientResponseDTO dto;
    private PatientRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        patientIn = new Patient(null, "Sully", "1234567890", null);
        patientOut = new Patient(1L, "Sully", "1234567890", null);
        dto = new PatientResponseDTO(1L, "Sully", "1234567890", null);
        requestDTO = new PatientRequestDTO("Santa Claws", "0987654321");
    }

    @Test
    void getPatientWithJournal() {
        //arrange
        //act
        //assert
    }

    @Test
    void add() {
        //arrange
        when(repository.save(eq(mapper.toEntity(requestDTO)))).thenReturn(patientOut);

        //act
        PatientResponseDTO responseDTO = service.add(requestDTO);

        //assert
        assertEquals(dto, responseDTO);
        verify(repository).save(eq(mapper.toEntity(requestDTO)));
    }
}