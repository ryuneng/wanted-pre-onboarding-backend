package com.wanted.preonboardingbackend.domain.jobApplication.repository;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import com.wanted.preonboardingbackend.domain.company.repository.CompanyRepository;
import com.wanted.preonboardingbackend.domain.jobApplication.entity.JobApplication;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import com.wanted.preonboardingbackend.domain.jobPosting.repository.JobPostingRepository;
import com.wanted.preonboardingbackend.domain.user.entity.User;
import com.wanted.preonboardingbackend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class JobApplicationRepositoryTest {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("같은 채용공고 ID와 유저 ID를 가진 지원내역을 조회한다.")
    @Test
    void existsByJobPostingIdAndUserId() {
        // given
        JobApplication jobApplication = createJobApplication();
        jobApplicationRepository.save(jobApplication);

        // when
        boolean duplicatedJobApplication = jobApplicationRepository.existsByJobPostingIdAndUserId(
                jobApplication.getJobPosting().getId(),
                jobApplication.getUser().getId()
        );

        // then
        assertThat(duplicatedJobApplication).isTrue();
    }

    private JobApplication createJobApplication() {
        return JobApplication.builder()
                .jobPosting(createJobPosting())
                .user(createUser())
                .build();
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
