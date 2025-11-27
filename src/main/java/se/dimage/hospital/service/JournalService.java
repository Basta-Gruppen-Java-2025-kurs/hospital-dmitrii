package se.dimage.hospital.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.dimage.hospital.dto.JournalRequestDTO;
import se.dimage.hospital.dto.JournalResponseDTO;
import se.dimage.hospital.exception.JournalNotFoundException;
import se.dimage.hospital.exception.PatientNotFoundException;
import se.dimage.hospital.mapper.JournalMapper;
import se.dimage.hospital.model.Journal;
import se.dimage.hospital.model.Patient;
import se.dimage.hospital.repository.JournalRepository;
import se.dimage.hospital.repository.PatientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalService {
    private static final Logger log = LoggerFactory.getLogger(JournalService.class);
    private final JournalRepository repository;
    private final JournalMapper mapper;
    private final PatientRepository patientRepository;

    public List<JournalResponseDTO> findAll() {
        return  repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public JournalResponseDTO add(JournalRequestDTO requestDTO) {
        return mapper.toResponseDTO(repository.save(mapper.toEntity(requestDTO)));
    }

    public JournalResponseDTO findById(Long id) {
        return repository.findById(id).map(mapper::toResponseDTO).orElseThrow(() -> new JournalNotFoundException(id));
    }

    public JournalResponseDTO update(Long id, JournalRequestDTO requestDTO) {
        log.info("Journal update service called. Id: " + id);
        Journal j = mapper.toEntity(requestDTO);
        log.info("Mapped to entity: " + j);
        j.setId(id);
        log.info("Id set: " + j.getId());
        Journal saved = repository.save(j);
        log.info("Saved to repository: " +  saved);
        return mapper.toResponseDTO(saved);
    }

    public JournalResponseDTO patch(Long id, JournalRequestDTO requestDTO) {
        Journal j = repository.findById(id).orElseThrow(() -> new JournalNotFoundException(id));
        if (requestDTO.getRecord()   != null) {
            j.setRecord(requestDTO.getRecord());
        }
        if (requestDTO.getPatientId() != null) {
            Long patientId = requestDTO.getPatientId();
            Patient p = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException(patientId));
            j.setPatient(p);
        }
        return mapper.toResponseDTO(repository.save(j));

    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            throw new JournalNotFoundException(id);
        }
        repository.deleteById(id);
        return true;
    }
}
