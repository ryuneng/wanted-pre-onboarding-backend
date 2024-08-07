package com.wanted.preonboardingbackend.domain.jobApplication.service;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import com.wanted.preonboardingbackend.domain.company.repository.CompanyRepository;
import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationRequestDto;
import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationResponseDto;
import com.wanted.preonboardingbackend.domain.jobApplication.entity.JobApplication;
import com.wanted.preonboardingbackend.domain.jobApplication.repository.JobApplicationRepository;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import com.wanted.preonboardingbackend.domain.jobPosting.repository.JobPostingRepository;
import com.wanted.preonboardingbackend.domain.user.entity.User;
import com.wanted.preonboardingbackend.domain.user.repository.UserRepository;
import com.wanted.preonboardingbackend.global.dto.PageRequestDto;
import com.wanted.preonboardingbackend.global.enums.ErrorMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class JobApplicationServiceTest {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @AfterEach
    void tearDown() {
        jobApplicationRepository.deleteAllInBatch();
        jobPostingRepository.deleteAllInBatch();
        companyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("채용공고에 지원한다.")
    @Test
    void saveJobApplication() {
        // given
        Company company = createCompany();
        User user = createUser();
        JobPosting jobPosting = createJobPosting(company);

        JobApplicationRequestDto request = createJobApplicationRequestDto(
                jobPosting.getId(), user.getId()
        );

        // when
        JobApplicationResponseDto response = jobApplicationService.save(request);

        // then
        assertThat(response)
                .extracting("id", "userName", "jobPostingId", "companyName")
                .contains(response.getId(), user.getName(), jobPosting.getId(), company.getName());
    }

    @DisplayName("이미 지원한 채용공고에 지원한다.")
    @Test
    void saveJobApplicationWithDuplication() {
        // given
        JobApplicationRequestDto request = createJobApplicationRequestDto(
                createJobPosting(createCompany()).getId(),
                createUser().getId()
        );

        jobApplicationService.save(request);

        // when // then
        assertThatThrownBy(() -> jobApplicationService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.PREVIOUS_APPLICATION.getMessage());
    }

    @DisplayName("사용자의 ID로 지원내역 목록을 조회한다.")
    @Test
    void getApplications() {
        // given
        User user = createUser();
        PageRequestDto request = createJobApplications(user);

        // when // then
        assertThat(jobApplicationService.getApplications(request, user.getId()).getDtoList()).hasSize(3)
                .extracting("userName", "companyName")
                .containsExactlyInAnyOrder(
                        tuple("김구직", "원티드랩"),
                        tuple("김구직", "네이버"),
                        tuple("김구직", "카카오")
                );
    }

    @DisplayName("잘못된 사용자 ID로 지원내역 목록을 조회한다.")
    @Test
    void getApplicationsWithNonExistentUserId() {
        // given
        Long nonExistentUserId = 100L;

        User user = createUser();
        PageRequestDto request = createJobApplications(user);

        // when // then
        assertThatThrownBy(() -> jobApplicationService.getApplications(request, nonExistentUserId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());
    }

    // 채용공고 지원 요청 DTO
    private JobApplicationRequestDto createJobApplicationRequestDto(Long jobPostingId, Long userId) {
        return JobApplicationRequestDto.builder()
                .jobPostingId(jobPostingId)
                .userId(userId)
                .build();
    }

    private Company createCompany() {
        return companyRepository.save(Company.builder()
                .name("원티드랩")
                .nation("한국")
                .region("서울")
                .build());
    }

    private Company createAnotherCompany(String name, String nation, String region) {
        return companyRepository.save(Company.builder()
                .name(name)
                .nation(nation)
                .region(region)
                .build());
    }

    private JobPosting createJobPosting(Company company) {
        return jobPostingRepository.save(JobPosting.builder()
                .company(company)
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

    // 지원내역 목록 조회를 위해 여러 개의 지원내역 등록
    private JobApplication createJobApplication(User user, JobPosting jobPosting) {
        return JobApplication.builder()
                .jobPosting(jobPosting)
                .user(user)
                .build();
    }

    private PageRequestDto createJobApplications(User user) {
        jobApplicationRepository.save(createJobApplication(user, createJobPosting(
                createCompany()
        )));
        jobApplicationRepository.save(createJobApplication(user, createJobPosting(
                createAnotherCompany("네이버", "미국", "뉴욕")
        )));
        jobApplicationRepository.save(createJobApplication(user, createJobPosting(
                createAnotherCompany("카카오", "일본", "도쿄")
        )));

        return PageRequestDto.builder().page(1).size(10).build();
    }
}
