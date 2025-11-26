package se.dimage.hospital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.dimage.hospital.dto.JournalResponseDTO;
import se.dimage.hospital.service.JournalService;

import java.util.List;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalController {
    private final JournalService service;

    @GetMapping
    public ResponseEntity<List<JournalResponseDTO>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }

}
