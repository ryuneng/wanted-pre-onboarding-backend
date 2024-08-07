package com.wanted.preonboardingbackend.domain.jobPosting.service;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import com.wanted.preonboardingbackend.domain.company.repository.CompanyRepository;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingResponseDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingSaveRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingUpdateRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import com.wanted.preonboardingbackend.domain.jobPosting.repository.JobPostingRepository;
import com.wanted.preonboardingbackend.global.enums.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class JobPostingServiceTest {

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @DisplayName("신규 채용공고를 등록한다.")
    @Test
    void createJobPosting() {
        // given
        JobPostingSaveRequestDto request = JobPostingSaveRequestDto.builder()
                .companyId(createCompany().getId())
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

    @DisplayName("신규 채용공고를 등록할 때, 존재하는 회사 ID를 입력해야 한다.")
    @Test
    void createJobPostingWithNonExistentCompanyId() {
        // given
        JobPostingSaveRequestDto request = JobPostingSaveRequestDto.builder()
                .companyId(100L)
                .position("백엔드 개발자")
                .reward(1500)
                .content("백엔드 개발자 채용합니다.")
                .skill("자바, 스프링")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.COMPANY_NOT_FOUND.getMessage());
    }

    @DisplayName("신규 채용공고를 등록할 때, 회사 ID는 필수값이다.")
    @Test
    void createJobPostingWithoutCompanyId() {
        // given
        JobPostingSaveRequestDto request = JobPostingSaveRequestDto.builder()
                .position("백엔드 개발자")
                .reward(1500)
                .content("백엔드 개발자 채용합니다.")
                .skill("자바, 스프링")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @DisplayName("신규 채용공고를 등록할 때, 포지션은 필수값이다.")
    @Test
    void createJobPostingWithoutPosition() {
        // given
        JobPostingSaveRequestDto request = JobPostingSaveRequestDto.builder()
                .companyId(createCompany().getId())
                .reward(1500)
                .content("백엔드 개발자 채용합니다.")
                .skill("자바, 스프링")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("신규 채용공고를 등록할 때, 채용 보상금은 0 이상의 정수만 가능하다.")
    @Test
    void createJobPostingWithNegativeReward() {
        // given
        JobPostingSaveRequestDto request = JobPostingSaveRequestDto.builder()
                .companyId(createCompany().getId())
                .position("백엔드 개발자")
                .reward(-1)
                .content("백엔드 개발자 채용합니다.")
                .skill("자바, 스프링")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.REWARD_NEGATIVE_ERROR.getMessage());
    }

    @DisplayName("신규 채용공고를 등록할 때, 채용 내용은 필수값이다.")
    @Test
    void createJobPostingWithoutContent() {
        // given
        JobPostingSaveRequestDto request = JobPostingSaveRequestDto.builder()
                .companyId(createCompany().getId())
                .position("백엔드 개발자")
                .reward(1500)
                .skill("자바, 스프링")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("신규 채용공고를 등록할 때, 사용 기술은 필수값이다.")
    @Test
    void createJobPostingWithoutSkill() {
        // given
        JobPostingSaveRequestDto request = JobPostingSaveRequestDto.builder()
                .companyId(createCompany().getId())
                .position("백엔드 개발자")
                .reward(1500)
                .content("백엔드 개발자 채용합니다.")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("채용공고를 수정한다.")
    @Test
    void updateJobPosting() {
        // given
        JobPosting jobPostingToBeUpdated = createJobPosting(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "Java, Spring");
        jobPostingRepository.save(jobPostingToBeUpdated);

        JobPosting jobPostingNotUpdated = createJobPosting(
                createCompany(), "프론트엔드 개발자", 1000, "프론트엔드 개발자 채용합니다.", "React");
        jobPostingRepository.save(jobPostingNotUpdated);

        JobPostingUpdateRequestDto request = JobPostingUpdateRequestDto.builder()
                .position("풀스택 개발자")
                .reward(3000)
                .content("풀스택 개발자 채용합니다.")
                .skill("Java, Vue.js")
                .build();

        // when
        JobPostingResponseDto jobPostingResponse = jobPostingService.update(jobPostingToBeUpdated.getId(), request);

        // then
        assertThat(jobPostingResponse)
                .extracting("position", "reward", "content", "skill")
                .contains("풀스택 개발자", 3000, "풀스택 개발자 채용합니다.", "Java, Vue.js");
    }

    @DisplayName("채용공고를 수정할 때, 알맞는 채용 공고 ID가 전달되어야 한다.")
    @Test
    void updateJobPostingWithNonExistentJobPostingId() {
        // given
        JobPosting jobPostingToBeUpdated = createJobPosting(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "Java, Spring");
        jobPostingRepository.save(jobPostingToBeUpdated);

        JobPostingUpdateRequestDto request = JobPostingUpdateRequestDto.builder()
                .position("풀스택 개발자")
                .reward(3000)
                .content("풀스택 개발자 채용합니다.")
                .skill("Java, Vue.js")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(-1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.JOB_NOT_FOUND.getMessage());
        JobPostingResponseDto jobPostingResponse = jobPostingService.update(jobPostingToBeUpdated.getId(), request);
    }

    @DisplayName("채용공고를 수정할 때, 포지션은 필수값이다.")
    @Test
    void updateJobPostingWithoutPosition() {
        // given
        JobPosting jobPostingToBeUpdated = createJobPosting(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "Java, Spring");
        jobPostingRepository.save(jobPostingToBeUpdated);

        JobPostingUpdateRequestDto request = JobPostingUpdateRequestDto.builder()
                .position(null)
                .reward(3000)
                .content("풀스택 개발자 채용합니다.")
                .skill("Java, Vue.js")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(jobPostingToBeUpdated.getId(), request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("채용공고를 수정할 때, 채용보상금은 0 이상의 정수만 가능하다.")
    @Test
    void updateJobPostingWithNegativeReward() {
        // given
        JobPosting jobPostingToBeUpdated = createJobPosting(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "Java, Spring");
        jobPostingRepository.save(jobPostingToBeUpdated);

        JobPostingUpdateRequestDto request = JobPostingUpdateRequestDto.builder()
                .position("풀스택 개발자")
                .reward(-1)
                .content("풀스택 개발자 채용합니다.")
                .skill("Java, Vue.js")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(jobPostingToBeUpdated.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.REWARD_NEGATIVE_ERROR.getMessage());
    }

    @DisplayName("채용공고를 수정할 때, 채용 내용은 필수값이다.")
    @Test
    void updateJobPostingWithoutContent() {
        // given
        JobPosting jobPostingToBeUpdated = createJobPosting(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "Java, Spring");
        jobPostingRepository.save(jobPostingToBeUpdated);

        JobPostingUpdateRequestDto request = JobPostingUpdateRequestDto.builder()
                .position("풀스택 개발자")
                .reward(3000)
                .content(null)
                .skill("Java, Vue.js")
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(jobPostingToBeUpdated.getId(), request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("채용공고를 수정할 때, 사용 기술은 필수값이다.")
    @Test
    void updateJobPostingWithoutSkill() {
        // given
        JobPosting jobPostingToBeUpdated = createJobPosting(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "Java, Spring");
        jobPostingRepository.save(jobPostingToBeUpdated);

        JobPostingUpdateRequestDto request = JobPostingUpdateRequestDto.builder()
                .position("풀스택 개발자")
                .reward(3000)
                .content("풀스택 개발자 채용합니다.")
                .skill(null)
                .build();

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(jobPostingToBeUpdated.getId(), request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("채용공고를 삭제한다.")
    @Test
    void deleteJobPosting() {
        // given
        JobPosting jobPosting = createJobPosting(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "Java, Spring");
        jobPostingRepository.save(jobPosting);

        // when
        jobPostingService.delete(jobPosting.getId());

        // then
        Optional<JobPosting> deletedJobPosting = jobPostingRepository.findById(jobPosting.getId());
        assertThat(deletedJobPosting).isEmpty();
    }

    @DisplayName("존재하지 않는 채용공고를 삭제한다.")
    @Test
    void deleteJobPostingWithNonExistentJobPostingId() {
        // given
        Long nonExistentJobPostingID = 100L;

        // when // then
        assertThatThrownBy(() -> jobPostingService.delete(nonExistentJobPostingID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.JOB_NOT_FOUND.getMessage());
    }

    private JobPosting createJobPosting(Company company, String position, int reward, String content, String skill) {
        return JobPosting.builder()
                .company(company)
                .position(position)
                .reward(reward)
                .content(content)
                .skill(skill)
                .build();
    }

    private Company createCompany() {
        return companyRepository.save(Company.builder()
                .name("원티드랩")
                .nation("한국")
                .region("서울")
                .build());
    }
}