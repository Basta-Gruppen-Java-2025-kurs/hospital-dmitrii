package se.dimage.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {
    Long id;
    String name;
    String personalNumber;
    List<JournalResponseDTO> journals;
}
