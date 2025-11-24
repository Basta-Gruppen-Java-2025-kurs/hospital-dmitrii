package se.dimage.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientWithJournalsResponseDTO {
    Long id;
    String name;
    String personalNumber;
    List<String> journals;
}
