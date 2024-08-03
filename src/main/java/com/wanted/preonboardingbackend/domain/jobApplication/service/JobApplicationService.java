package com.wanted.preonboardingbackend.domain.jobApplication.service;

import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationRequestDto;
import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationResponseDto;
import com.wanted.preonboardingbackend.domain.jobApplication.entity.JobApplication;
import com.wanted.preonboardingbackend.domain.jobApplication.repository.JobApplicationRepository;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import com.wanted.preonboardingbackend.domain.jobPosting.repository.JobPostingRepository;
import com.wanted.preonboardingbackend.domain.user.entity.User;
import com.wanted.preonboardingbackend.domain.user.repository.UserRepository;
import com.wanted.preonboardingbackend.global.enums.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;

    /**
     * 채용 지원 등록
     * 
     * @param requestDto 등록할 지원 정보가 포함된 JobApplicationRequestDto 객체
     * @return 등록된 지원 정보가 포함된 JobApplicationResponseDto 객체
     */
    @Transactional
    public JobApplicationResponseDto save(JobApplicationRequestDto requestDto) {

        // 중복 지원 여부 확인
        if (jobApplicationRepository.existsByJobPostingIdAndUserId(requestDto.getJobPostingId(), requestDto.getUserId())) {
            throw new IllegalArgumentException(ErrorMessage.PREVIOUS_APPLICATION.getMessage());
        }

        JobPosting jobPosting = jobPostingRepository.findById(requestDto.getJobPostingId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.JOB_NOT_FOUND.getMessage()));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        JobApplication jobApplication = JobApplication.builder()
                .jobPosting(jobPosting)
                .user(user)
                .build();

        JobApplication savedApplication = jobApplicationRepository.save(jobApplication);

        return new JobApplicationResponseDto(savedApplication);
    }
}
