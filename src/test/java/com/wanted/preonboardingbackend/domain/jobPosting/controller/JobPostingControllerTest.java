package com.wanted.preonboardingbackend.domain.jobPosting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingSaveRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.service.JobPostingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobPostingController.class)
public class JobPostingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobPostingService jobPostingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("채용공고를 등록한다.")
    void saveJob() throws Exception {
        // given
        JobPostingSaveRequestDto request = JobPostingSaveRequestDto.builder()
                .companyId(1L)
                .position("백엔드 주니어 개발자")
                .reward(1000000)
                .content("원티드랩의 백엔드 주니어 개발자를 채용합니다.")
                .skill("Java & Spring")
                .build();

        // when // then
        mockMvc.perform(
                post("/job")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }
}
