package se.dimage.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.dimage.hospital.dto.PatientRequestDTO;
import se.dimage.hospital.dto.PatientResponseDTO;
import se.dimage.hospital.dto.PatientWithJournalsResponseDTO;
import se.dimage.hospital.exception.PatientNotFoundException;
import se.dimage.hospital.mapper.PatientMapper;
import se.dimage.hospital.model.Patient;
import se.dimage.hospital.repository.PatientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;
    private final PatientMapper mapper;

    public List<PatientResponseDTO> findAllPatients() {
        return repository.findAll().stream().map(mapper::toResponseDto).toList();
    }

    public PatientWithJournalsResponseDTO getPatientWithJournal(Long id) {
        return repository.findById(id).map(mapper::toResponseDtoWithJournals).orElseThrow(() -> new PatientNotFoundException(id));
    }

    public PatientResponseDTO add(PatientRequestDTO requestDTO) {
        return mapper.toResponseDto(repository.save(mapper.toEntity(requestDTO)));
    }

    public PatientResponseDTO update(Long id, PatientRequestDTO requestDTO) {
        Patient p = repository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        if (requestDTO.getPersonalNumber() != null) {
            p.setPersonalNumber(requestDTO.getPersonalNumber());
        }
        if (requestDTO.getName() != null) {
            p.setName(requestDTO.getName());
        }
        return mapper.toResponseDto(repository.save(p));
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        throw new PatientNotFoundException(id);
    }
}
