package se.dimage.hospital.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patients")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String personalNumber;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Journal> journals;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Patient patientObj) {
            return  Objects.equals(patientObj.id, this.id) &&
                    Objects.equals(patientObj.name, this.name) &&
                    Objects.equals(patientObj.personalNumber, this.personalNumber);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, personalNumber);
    }
}
