package com.wanted.preonboardingbackend.domain.jobPosting.service;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import com.wanted.preonboardingbackend.domain.company.repository.CompanyRepository;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingDetailDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingResponseDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingSaveRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.JobPostingUpdateRequestDto;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import com.wanted.preonboardingbackend.domain.jobPosting.repository.JobPostingRepository;
import com.wanted.preonboardingbackend.global.dto.PageRequestDto;
import com.wanted.preonboardingbackend.global.enums.ErrorMessage;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        jobPostingRepository.deleteAllInBatch();
    }

    @DisplayName("신규 채용공고를 등록한다.")
    @Test
    void createJobPosting() {
        // given
        JobPostingSaveRequestDto request = createJobPostingSaveRequestDto(
                createCompany().getId(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "자바, 스프링"
        );

        // when
        JobPostingResponseDto jobPostingResponse = jobPostingService.save(request);

        // then
        assertThat(jobPostingResponse)
                .extracting("position", "reward", "content", "skill")
                .contains("백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "자바, 스프링");
    }

    @DisplayName("존재하지 않는 회사 ID로 신규 채용공고를 등록한다.")
    @Test
    void createJobPostingWithNonExistentCompanyId() {
        // given
        JobPostingSaveRequestDto request = createJobPostingSaveRequestDto(
                100L, "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "자바, 스프링"
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.COMPANY_NOT_FOUND.getMessage());
    }

    @DisplayName("신규 채용공고를 등록할 때, 회사 ID는 필수값이다.")
    @Test
    void createJobPostingWithoutCompanyId() {
        // given
        JobPostingSaveRequestDto request = createJobPostingSaveRequestDto(
                null, "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "자바, 스프링"
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @DisplayName("신규 채용공고를 등록할 때, 포지션은 필수값이다.")
    @Test
    void createJobPostingWithoutPosition() {
        // given
        JobPostingSaveRequestDto request = createJobPostingSaveRequestDto(
                createCompany().getId(), null, 1500, "백엔드 개발자 채용합니다.", "자바, 스프링"
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("신규 채용공고를 등록할 때, 채용 보상금은 0 이상의 정수만 가능하다.")
    @Test
    void createJobPostingWithNegativeReward() {
        // given
        JobPostingSaveRequestDto request = createJobPostingSaveRequestDto(
                createCompany().getId(), "백엔드 개발자", -1, "백엔드 개발자 채용합니다.", "자바, 스프링"
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.REWARD_NEGATIVE_ERROR.getMessage());
    }

    @DisplayName("신규 채용공고를 등록할 때, 채용 내용은 필수값이다.")
    @Test
    void createJobPostingWithoutContent() {
        // given
        JobPostingSaveRequestDto request = createJobPostingSaveRequestDto(
                createCompany().getId(), "백엔드 개발자", 1500, null, "자바, 스프링"
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("신규 채용공고를 등록할 때, 사용 기술은 필수값이다.")
    @Test
    void createJobPostingWithoutSkill() {
        // given
        JobPostingSaveRequestDto request = createJobPostingSaveRequestDto(
                createCompany().getId(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", null
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.save(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("채용공고를 수정한다.")
    @Test
    void updateJobPosting() {
        // given
        JobPosting beforeUpdateJobPosting = createBeforeUpdateJobPosting();

        JobPostingUpdateRequestDto toBeUpdatedRequest = createJobPostingUpdateRequestDto(
                "풀스택 개발자", 3000, "풀스택 개발자 채용합니다.", "Java, Vue.js"
        );

        // when
        JobPostingResponseDto jobPostingResponse = jobPostingService.update(beforeUpdateJobPosting.getId(), toBeUpdatedRequest);

        // then
        assertThat(jobPostingResponse)
                .extracting("position", "reward", "content", "skill")
                .contains("풀스택 개발자", 3000, "풀스택 개발자 채용합니다.", "Java, Vue.js");
    }

    @DisplayName("채용공고를 수정할 때, 알맞는 채용 공고 ID가 전달되어야 한다.")
    @Test
    void updateJobPostingWithNonExistentJobPostingId() {
        // given
        JobPostingUpdateRequestDto toBeUpdatedRequest = createJobPostingUpdateRequestDto(
                "풀스택 개발자", 3000, "풀스택 개발자 채용합니다.", "Java, Vue.js"
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(-1L, toBeUpdatedRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.JOB_NOT_FOUND.getMessage());
    }

    @DisplayName("채용공고를 수정할 때, 포지션은 필수값이다.")
    @Test
    void updateJobPostingWithoutPosition() {
        // given
        JobPosting beforeUpdateJobPosting = createBeforeUpdateJobPosting();

        JobPostingUpdateRequestDto toBeUpdatedRequest = createJobPostingUpdateRequestDto(
                null, 3000, "풀스택 개발자 채용합니다.", "Java, Vue.js"
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(beforeUpdateJobPosting.getId(), toBeUpdatedRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("채용공고를 수정할 때, 채용보상금은 0 이상의 정수만 가능하다.")
    @Test
    void updateJobPostingWithNegativeReward() {
        // given
        JobPosting beforeUpdateJobPosting = createBeforeUpdateJobPosting();

        JobPostingUpdateRequestDto toBeUpdatedRequest = createJobPostingUpdateRequestDto(
                "풀스택 개발자", -1, "풀스택 개발자 채용합니다.", "Java, Vue.js"
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(beforeUpdateJobPosting.getId(), toBeUpdatedRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.REWARD_NEGATIVE_ERROR.getMessage());
    }

    @DisplayName("채용공고를 수정할 때, 채용 내용은 필수값이다.")
    @Test
    void updateJobPostingWithoutContent() {
        // given
        JobPosting beforeUpdateJobPosting = createBeforeUpdateJobPosting();

        JobPostingUpdateRequestDto toBeUpdatedRequest = createJobPostingUpdateRequestDto(
                "풀스택 개발자", 3000, null, "Java, Vue.js"
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(beforeUpdateJobPosting.getId(), toBeUpdatedRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("채용공고를 수정할 때, 사용 기술은 필수값이다.")
    @Test
    void updateJobPostingWithoutSkill() {
        // given
        JobPosting beforeUpdateJobPosting = createBeforeUpdateJobPosting();

        JobPostingUpdateRequestDto toBeUpdatedRequest = createJobPostingUpdateRequestDto(
                "풀스택 개발자", 3000, "풀스택 개발자 채용합니다.", null
        );

        // when // then
        assertThatThrownBy(() -> jobPostingService.update(beforeUpdateJobPosting.getId(), toBeUpdatedRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("채용공고를 삭제한다.")
    @Test
    void deleteJobPosting() {
        // given
        JobPosting jobPosting = createBeforeUpdateJobPosting();

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

    @DisplayName("채용공고 목록을 조회한다.")
    @Test
    void getJobPostings() {
        // given
        PageRequestDto request = createJobPostings();

        // when // then
        assertThat(jobPostingService.getJobPostings(request).getDtoList()).hasSize(3)
                .extracting("position", "reward", "skill")
                .containsExactlyInAnyOrder(
                        tuple("백엔드 개발자", 1500, "자바, 스프링"),
                        tuple("프론트엔드 개발자", 1000, "자바스크립트"),
                        tuple("풀스택 개발자", 3000, "리액트, 스프링")
                );
    }

    @DisplayName("키워드로 채용공고 목록을 조회한다.")
    @Test
    void searchJobPostings() {
        // given
        PageRequestDto request = createJobPostings();

        // when // then
        assertThat(jobPostingService.searchJobPostings(request, "스프링").getDtoList()).hasSize(2)
                .extracting("position", "reward", "skill")
                .containsExactlyInAnyOrder(
                        tuple("백엔드 개발자", 1500, "자바, 스프링"),
                        tuple("풀스택 개발자", 3000, "리액트, 스프링")
                );
    }

    @DisplayName("채용공고 상세를 조회한다.")
    @Test
    void getJobPostingDetail() {
        // given
        JobPosting jobPosting = jobPostingRepository.save(createJobPostingBuilder(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "Java, Spring"));

        // when
        JobPostingDetailDto getDetailDto = jobPostingService.getJobPostingDetail(jobPosting.getId());

        // then
        assertThat(getDetailDto).isNotNull();
        assertThat(getDetailDto).usingRecursiveComparison() // 객체 동등성 비교 (주소값 상관없이 내부 값만 같으면 통과)
                .isEqualTo(createExpectedDetailDto(jobPosting));
    }

    @DisplayName("존재하지 않는 채용공고 ID의 상세를 조회한다.")
    @Test
    void getJobPostingDetailWithNonExistentJobPostingId() {
        // given
        Long nonExistentJobPostingId = 100L;

        // when // then
        assertThatThrownBy(() -> jobPostingService.getJobPostingDetail(nonExistentJobPostingId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.JOB_NOT_FOUND.getMessage());
    }

    private Company createCompany() {
        return companyRepository.save(Company.builder()
                .name("원티드랩")
                .nation("한국")
                .region("서울")
                .build());
    }

    // 채용공고 등록할 때 요청할 RequestDto 객체
    private JobPostingSaveRequestDto createJobPostingSaveRequestDto(Long companyId, String position, int reward, String content, String skill) {
        return JobPostingSaveRequestDto.builder()
                .companyId(companyId)
                .position(position)
                .reward(reward)
                .content(content)
                .skill(skill)
                .build();
    }

    private JobPosting createJobPostingBuilder(Company company, String position, int reward, String content, String skill) {
        return JobPosting.builder()
                .company(company)
                .position(position)
                .reward(reward)
                .content(content)
                .skill(skill)
                .build();
    }

    // 수정할 채용공고 등록 (수정하기 전의 채용공고)
    private JobPosting createBeforeUpdateJobPosting() {
        JobPosting beforeUpdateJobPosting = createJobPostingBuilder(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "Java, Spring");
        jobPostingRepository.save(beforeUpdateJobPosting);
        return beforeUpdateJobPosting;
    }

    // 채용공고 수정할 내용
    private JobPostingUpdateRequestDto createJobPostingUpdateRequestDto(String position, int reward, String content, String skill) {
        return JobPostingUpdateRequestDto.builder()
                .position(position)
                .reward(reward)
                .content(content)
                .skill(skill)
                .build();
    }

    // 채용공고 목록 조회를 위해 여러 개의 채용공고 등록
    private PageRequestDto createJobPostings() {
        jobPostingRepository.save(createJobPostingBuilder(
                createCompany(), "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "자바, 스프링")
        );
        jobPostingRepository.save(createJobPostingBuilder(
                createCompany(), "프론트엔드 개발자", 1000, "프론트엔드 개발자 채용합니다.", "자바스크립트")
        );
        jobPostingRepository.save(createJobPostingBuilder(
                createCompany(), "풀스택 개발자", 3000, "풀스택 개발자 채용합니다.", "리액트, 스프링")
        );

        return PageRequestDto.builder().page(1).size(10).build();
    }

    // 채용공고 상세 조회 시 기대하는 JobPostingDetailDto 객체
    private JobPostingDetailDto createExpectedDetailDto(JobPosting jobPosting) {
        return JobPostingDetailDto.builder()
                .jobPosting(jobPosting)
                .otherJobPostingIds(jobPostingRepository.findOtherJobsById(jobPosting.getId(), jobPosting.getCompany().getId()))
                .build();
    }
}