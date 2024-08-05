package com.wanted.preonboardingbackend.domain.jobPosting.service;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import com.wanted.preonboardingbackend.domain.company.repository.CompanyRepository;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingResponseDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingSaveRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.repository.JobPostingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class JobPostingServiceTest {

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @AfterEach
    void tearDown() {
        jobPostingRepository.deleteAllInBatch();
    }

    @DisplayName("신규 채용공고를 등록한다.")
    @Test
    void createJobPosting() {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .nation("한국")
                .region("서울")
                .build());

        JobPostingSaveRequestDto request = JobPostingSaveRequestDto.builder()
                .companyId(company.getId())
                .position("백엔드 개발자")
                .reward(1500)
                .content("백엔드 개발자 채용합니다.")
                .skill("자바, 스프링")
                .build();

        // when
        JobPostingResponseDto jobPostingResponse = jobPostingService.save(request);

        // then
        assertThat(jobPostingResponse)
                .extracting("position", "reward", "content", "skill")
                .contains("백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "자바, 스프링");
    }

}