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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class JobApplicationRepositoryTest {

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
        JobApplication jobApplication = createJobApplication(createUser());
        jobApplicationRepository.save(jobApplication);

        // when
        boolean duplicatedJobApplication = jobApplicationRepository.existsByJobPostingIdAndUserId(
                jobApplication.getJobPosting().getId(),
                jobApplication.getUser().getId()
        );

        // then
        assertThat(duplicatedJobApplication).isTrue();
    }

    @DisplayName("사용자의 ID로 지원내역 목록을 조회한다.")
    @Test
    void findAllByUserId() {
        // given
        User user = createUser();
        JobApplication application1 = createJobApplication(user);
        JobApplication application2 = createJobApplication(user);
        jobApplicationRepository.save(application1);
        jobApplicationRepository.save(application2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("id")));

        // when
        Page<JobApplication> jobApplications = jobApplicationRepository.findAllByUserId(pageable, user.getId());

        // then
        assertThat(jobApplications).hasSize(2)
                .extracting("id", "jobPosting.id", "user.name")
                .containsExactlyInAnyOrder(
                        tuple(application1.getId(), application1.getJobPosting().getId(), application1.getUser().getName()),
                        tuple(application2.getId(), application2.getJobPosting().getId(), application2.getUser().getName())
                );
    }

    private JobApplication createJobApplication(User user) {
        return JobApplication.builder()
                .jobPosting(createJobPosting())
                .user(user)
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
