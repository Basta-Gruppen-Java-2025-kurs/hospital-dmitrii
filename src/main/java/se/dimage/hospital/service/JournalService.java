package se.dimage.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.dimage.hospital.dto.JournalResponseDTO;
import se.dimage.hospital.mapper.JournalMapper;
import se.dimage.hospital.repository.JournalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalService {
    private final JournalRepository repository;
    private final JournalMapper mapper;

    public List<JournalResponseDTO> findAll() {
        return  repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

}
