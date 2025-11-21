package se.dimage.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestDTO {
    @NotNull(message = "name is missing")
    @NotBlank(message = "name cannot be empty")
    String name;

    @NotNull(message = "personal number is missing")
    @Length(min = 10, message = "personal number must be 10 or 12 digits long")
    @Pattern(regexp = "^(\\d{8}|\\d{6})-?\\d{4}$", message = "wrong format of personal number")
    String personalNumber;

}
