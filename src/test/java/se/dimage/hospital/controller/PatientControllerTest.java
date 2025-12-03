package se.dimage.hospital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import se.dimage.hospital.dto.PatientRequestDTO;
import se.dimage.hospital.dto.PatientResponseDTO;
import se.dimage.hospital.dto.PatientWithJournalsResponseDTO;
import se.dimage.hospital.exception.PatientNotFoundException;
import se.dimage.hospital.mapper.PatientMapper;
import se.dimage.hospital.model.Patient;
import se.dimage.hospital.service.JournalService;
import se.dimage.hospital.service.PatientService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(value = PatientController.class, useDefaultFilters = false)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService service;
    @MockitoBean
    private JournalService journalService;

    @Spy
    PatientMapper mapper = Mappers.getMapper(PatientMapper.class);

    @Autowired
    private ObjectMapper objectMapper;

    private PatientWithJournalsResponseDTO journalsResponseDTO;
    private PatientResponseDTO responseDTO;
    private PatientRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        requestDTO = new PatientRequestDTO("Crocodile Dundy", "1234567890");
        Patient p = mapper.toEntity(requestDTO);
        p.setId(1L);
        p.setJournals(List.of());
        journalsResponseDTO = mapper.toResponseDtoWithJournals(p);
        responseDTO = mapper.toResponseDto(p);
    }

    @AfterEach
    void teardown() {
        responseDTO = null;
        requestDTO = null;
        journalsResponseDTO = null;
    }

    @Test
    @DisplayName("GET /patients/{id} returns correct response")
    void getByIdShouldReturnCorrectResponse() throws Exception {
        //arrange
        when(service.getPatientWithJournal(1L)).thenReturn(journalsResponseDTO);
        //act + assert
        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(journalsResponseDTO)));
    }

    @Test
    @DisplayName("POST /patients with json body returns 201 and the correct object")
    void postShouldReturnCorrectObjectAndStatus() throws Exception {
        //arrange
        when(service.add(eq(requestDTO))).thenReturn(responseDTO);
        // act+assert
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().is(201))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));
    }

    @Test
    @DisplayName("Trying to create a new patient with wrong personal number should cause error 400")
    void postWithWrongPNShouldReturnError400() throws Exception {
        //arrange
        requestDTO.setPersonalNumber("123");
        //act+assert
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("Patient not found should throw an exception and return 404 with an error message")
    void getNonexistentPatientShouldReturn404() throws Exception {
        //arrange
        when(service.getPatientWithJournal(2L)).thenThrow(new PatientNotFoundException(2L));
        //act+assert
        MvcResult result = mockMvc.perform(get("/patients/2"))
                .andExpect(status().isNotFound()).andReturn();
        String json = Objects.requireNonNull(result.getResolvedException()).getLocalizedMessage();
        log.info("404 with content: " + json);
        assertTrue(json.contains("Patient # 2 not found."));
    }
}