package com.wanted.preonboardingbackend.domain.jobPosting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboardingbackend.domain.company.entity.Company;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingDetailDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingListDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingSaveRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingUpdateRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import com.wanted.preonboardingbackend.domain.jobPosting.service.JobPostingService;
import com.wanted.preonboardingbackend.global.dto.PageRequestDto;
import com.wanted.preonboardingbackend.global.dto.PageResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .andDo(print())
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
                .andDo(print())
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
                .andDo(print())
                .andExpect(status().isOk());

        // 서비스 메서드 호출 여부 검증
        verify(jobPostingService).delete(jobPostingId);
    }

    @DisplayName("채용공고 목록을 조회한다.")
    @Test
    void getJobPostings() throws Exception {
        // given
        PageRequestDto request = new PageRequestDto();

        PageResponseDto<JobPostingListDto> result = new PageResponseDto<>();

        when(jobPostingService.getJobPostings(request)).thenReturn(result);

        // when // then
        mockMvc.perform(
                        get("/job/list")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("키워드로 채용공고 목록을 조회한다.")
    @Test
    void searchJobPostings() throws Exception {
        // given
        PageRequestDto request = new PageRequestDto();

        PageResponseDto<JobPostingListDto> result = new PageResponseDto<>();

        when(jobPostingService.searchJobPostings(request, "test")).thenReturn(result);

        // when // then
        mockMvc.perform(
                        get("/job/search")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("채용공고 상세를 조회한다.")
    @Test
    void getJobPostingDetail() throws Exception {
        // given
        List<Long> otherJobPostingIds = Arrays.asList(2L, 3L);
        JobPosting jobPosting = createJobPosting();

        JobPostingDetailDto result = JobPostingDetailDto.builder()
                .jobPosting(jobPosting)
                .otherJobPostingIds(otherJobPostingIds)
                .build();

        when(jobPostingService.getJobPostingDetail(result.getId())).thenReturn(result);

        // when // then
        mockMvc.perform(
                        get("/job/detail/" + result.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.companyName").value("원티드랩"))
                .andExpect(jsonPath("$.nation").value("한국"))
                .andExpect(jsonPath("$.region").value("서울"))
                .andExpect(jsonPath("$.position").value("백엔드 개발자"))
                .andExpect(jsonPath("$.reward").value(1500))
                .andExpect(jsonPath("$.skill").value("Java"))
                .andExpect(jsonPath("$.content").value("백엔드 개발자 모집합니다."))
                .andExpect(jsonPath("$.otherJobPostingIds[0]").value(2L))
                .andExpect(jsonPath("$.otherJobPostingIds[1]").value(3L));
    }

    // 채용공고 상세 조회에서 반환할 JobPosting
    private JobPosting createJobPosting() {
        return JobPosting.builder()
                .id(1L)
                .company(Company.builder()
                        .id(10L)
                        .name("원티드랩")
                        .nation("한국")
                        .region("서울")
                        .build()
                )
                .position("백엔드 개발자")
                .reward(1500)
                .skill("Java")
                .content("백엔드 개발자 모집합니다.")
                .build();
    }
}
