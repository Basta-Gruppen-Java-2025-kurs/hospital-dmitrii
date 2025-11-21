package se.dimage.hospital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.dimage.hospital.dto.PatientResponseDTO;
import se.dimage.hospital.service.PatientService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService service;

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> listAllPatients() {
        return ResponseEntity.ok(service.findAllPatients());
    }
}
