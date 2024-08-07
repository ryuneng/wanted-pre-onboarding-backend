package com.wanted.preonboardingbackend.domain.jobApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationRequestDto;
import com.wanted.preonboardingbackend.domain.jobApplication.service.JobApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobApplicationController.class)
public class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobApplicationService jobApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("채용공고에 지원한다.")
    @Test
    void saveJobApplication() throws Exception {
        // given
        JobApplicationRequestDto request = JobApplicationRequestDto.builder()
                .jobPostingId(1L)
                .userId(1L)
                .build();

        // when // then
        mockMvc.perform(
                post("/application")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
