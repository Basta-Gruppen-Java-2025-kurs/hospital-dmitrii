package se.dimage.hospital.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import se.dimage.hospital.dto.JournalRequestDTO;
import se.dimage.hospital.dto.JournalResponseDTO;
import se.dimage.hospital.model.Journal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface JournalMapper {
    Journal toEntity(JournalRequestDTO requestDTO);

    @Mapping(target = "patientName", source = "patient.name")
    @Mapping(target = "patientId", source = "patient.id")
    JournalResponseDTO toResponseDTO(Journal journal);
}
