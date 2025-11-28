package se.dimage.hospital.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(PatientController.class);
    private final PatientService service;

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> listAllPatients() {
        log.info("Listing all patients");
        return ResponseEntity.ok(service.findAllPatients());
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientResponseDTO>> searchForPatients(@RequestParam(required = false) String name,
                                                                      @RequestParam(required = false) String personalNumber,
                                                                      @RequestParam(required = false) Integer minJournals,
                                                                      @RequestParam(required = false) Integer maxJournals) {
        return ResponseEntity.ok(service.search(name, personalNumber, minJournals, maxJournals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientWithJournalsResponseDTO> getPatient(@PathVariable @Valid Long id) {
        log.info("Showing patient # " + id + " details");
        return ResponseEntity.ok(service.getPatientWithJournal(id));
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> addPatient(@RequestBody @Valid PatientRequestDTO requestDTO) {
        log.info("Add new patient: " + requestDTO);
        return ResponseEntity.ok(service.add(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable @Valid Long id, @Valid @RequestBody PatientRequestDTO requestDTO) {
        log.info("Update patient data for patient # " + id);
        return ResponseEntity.ok(service.update(id, requestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> patchPatient(@PathVariable @Valid Long id, @RequestBody PatientRequestDTO requestDTO) {
        log.info("Patch patient data for patient # " + id + " with this info: " + requestDTO);
        return ResponseEntity.ok(service.patch(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePatient(@PathVariable @Valid Long id) {
        log.info("Deleting patient # " + id);
        return ResponseEntity.ok(service.delete(id));
    }
}
