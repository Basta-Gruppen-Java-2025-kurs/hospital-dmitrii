package se.dimage.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalResponseDTO {
    Long id;
    String record;
    Long patientId;
    String patientName;
}
