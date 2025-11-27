package se.dimage.hospital.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import se.dimage.hospital.dto.PatientRequestDTO;
import se.dimage.hospital.dto.PatientResponseDTO;
import se.dimage.hospital.dto.PatientWithJournalsResponseDTO;
import se.dimage.hospital.model.Patient;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = JournalMapper.class)
public interface PatientMapper {

    Patient toEntity(PatientRequestDTO dto);

    PatientResponseDTO toResponseDto(Patient patient);

    @Mapping(expression = "java(patient.getJournals().stream().map(Journal::getRecord).toList())", target = "journals")
    PatientWithJournalsResponseDTO toResponseDtoWithJournals(Patient patient);
}
