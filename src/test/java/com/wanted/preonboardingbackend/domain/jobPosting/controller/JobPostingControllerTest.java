package com.wanted.preonboardingbackend.domain.jobPosting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingSaveRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingUpdateRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.service.JobPostingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobPostingController.class)
public class JobPostingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobPostingService jobPostingService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("채용공고를 등록한다.")
    @Test
    void saveJobPosting() throws Exception {
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

    @DisplayName("채용공고를 수정한다.")
    @Test
    void updateJobPosting() throws Exception {
        // given
        JobPostingUpdateRequestDto request = JobPostingUpdateRequestDto.builder()
                .position("풀스택 개발자")
                .reward(3000)
                .content("풀스택 개발자 채용합니다.")
                .skill("Java, Vue.js")
                .build();

        // when // then
        mockMvc.perform(
                put("/job/" + 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("채용공고를 삭제한다.")
    @Test
    void deleteJobPosting() throws Exception {
        // given
        Long jobPostingId = 1L;

        // 서비스 메서드를 모킹하여 실제 메서드 호출 대신 원하는 동작 시뮬레이션
        doNothing().when(jobPostingService).delete(jobPostingId);

        // when // then
        mockMvc.perform(
                delete("/job/" + jobPostingId)
                        .content(objectMapper.writeValueAsString(jobPostingId))
                        .contentType(MediaType.APPLICATION_JSON)
                    )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        // 서비스 메서드 호출 여부 검증
        verify(jobPostingService).delete(jobPostingId);
    }
}
