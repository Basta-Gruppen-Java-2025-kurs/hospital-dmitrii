package se.dimage.hospital.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "journals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonManagedReference
    Patient patient;

    @Column(nullable = false)
    String record;
}
