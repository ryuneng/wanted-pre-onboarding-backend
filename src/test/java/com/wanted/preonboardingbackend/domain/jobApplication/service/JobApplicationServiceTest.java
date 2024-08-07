package com.wanted.preonboardingbackend.domain.jobApplication.service;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import com.wanted.preonboardingbackend.domain.company.repository.CompanyRepository;
import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationRequestDto;
import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationResponseDto;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import com.wanted.preonboardingbackend.domain.jobPosting.repository.JobPostingRepository;
import com.wanted.preonboardingbackend.domain.user.entity.User;
import com.wanted.preonboardingbackend.domain.user.repository.UserRepository;
import com.wanted.preonboardingbackend.global.enums.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
public class JobApplicationServiceTest {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("채용공고에 지원한다.")
    @Test
    void saveJobApplication() {
        // given
        JobApplicationRequestDto request = JobApplicationRequestDto.builder()
                .jobPostingId(createJobPosting().getId())
                .userId(createUser().getId())
                .build();

        // when
        JobApplicationResponseDto response = jobApplicationService.save(request);

        // then
        assertThat(response)
                .extracting("id", "userName", "jobPostingId", "companyName")
                .contains(1L, "김구직", 1L, "원티드랩");
    }

    @DisplayName("이미 지원한 채용공고에 지원한다.")
    @Test
    void saveJobApplicationWithDuplication() {
        // given
        JobApplicationRequestDto request = JobApplicationRequestDto.builder()
                .jobPostingId(createJobPosting().getId())
                .userId(createUser().getId())
                .build();

        jobApplicationService.save(request);

        // when // then
        assertThatThrownBy(() -> jobApplicationService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.PREVIOUS_APPLICATION.getMessage());
    }

    private Company createCompany() {
        return companyRepository.save(Company.builder()
                .name("원티드랩")
                .nation("한국")
                .region("서울")
                .build());
    }

    private JobPosting createJobPosting() {
        return jobPostingRepository.save(JobPosting.builder()
                .company(createCompany())
                .position("백엔드 개발자")
                .reward(3000)
                .content("백엔드 개발자 채용합니다.")
                .skill("Java")
                .build());
    }

    private User createUser() {
        return userRepository.save(User.builder()
                .name("김구직")
                .build());
    }
}
