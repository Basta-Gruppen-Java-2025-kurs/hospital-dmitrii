package se.dimage.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.dimage.hospital.dto.PatientResponseDTO;
import se.dimage.hospital.mapper.PatientMapper;
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
}
