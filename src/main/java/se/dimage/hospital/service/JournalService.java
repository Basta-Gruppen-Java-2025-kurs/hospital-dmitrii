package se.dimage.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.dimage.hospital.repository.JournalRepository;

@Service
@RequiredArgsConstructor
public class JournalService {
    private final JournalRepository repository;

}
