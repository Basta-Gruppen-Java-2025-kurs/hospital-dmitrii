package se.dimage.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalRequestDTO {
    @NotNull(message = "cannot be null")
    @NotBlank(message = "cannot be blank")
    private String record;

    @NotNull(message = "cannot be empty")
    @Positive(message = "must be positive")
    private Long patientId;
}
