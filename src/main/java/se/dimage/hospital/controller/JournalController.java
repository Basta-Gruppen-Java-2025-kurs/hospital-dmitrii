package se.dimage.hospital.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dimage.hospital.dto.JournalRequestDTO;
import se.dimage.hospital.dto.JournalResponseDTO;
import se.dimage.hospital.service.JournalService;

import java.util.List;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalController {
    private static final Logger log = LoggerFactory.getLogger(JournalController.class);
    private final JournalService service;

    @GetMapping
    public ResponseEntity<List<JournalResponseDTO>> listAll() {
        log.info("Listing all journals");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalResponseDTO> listById(@PathVariable Long id) {
        log.info("Shoing details of journal # "+ id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<JournalResponseDTO> addJournal(@RequestBody @Valid JournalRequestDTO requestDTO) {
        log.info("Adding new journal: " + requestDTO);
        return ResponseEntity.ok(service.add(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalResponseDTO> updateJournal(@PathVariable Long id, @RequestBody @Valid JournalRequestDTO requestDTO) {
        log.info("Updating journal # " + id + ". New journal data: " + requestDTO);
        return ResponseEntity.ok(service.update(id, requestDTO));
    }



    @PatchMapping("/{id}")
    public ResponseEntity<JournalResponseDTO> patchJournal(@PathVariable Long id, @RequestBody JournalRequestDTO requestDTO) {
        log.info("Patching journal # " + id + " with this data " + requestDTO);
        return ResponseEntity.ok(service.patch(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteJournal(@PathVariable Long id) {
        log.info("Deleting journal # " + id);
        return ResponseEntity.ok(service.delete(id));
    }

}
