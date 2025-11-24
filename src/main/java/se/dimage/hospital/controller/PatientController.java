package se.dimage.hospital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dimage.hospital.dto.PatientRequestDTO;
import se.dimage.hospital.dto.PatientResponseDTO;
import se.dimage.hospital.dto.PatientWithJournalsResponseDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<PatientWithJournalsResponseDTO> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPatientWithJournal(id));
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> addPatient(@RequestBody PatientRequestDTO requestDTO) {
        return ResponseEntity.ok(service.add(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id, @RequestBody PatientRequestDTO requestDTO) {
        return ResponseEntity.ok(service.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePatient(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
