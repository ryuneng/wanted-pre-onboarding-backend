package com.wanted.preonboardingbackend.domain.jobApplication.service;

import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationRequestDto;
import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationResponseDto;
import com.wanted.preonboardingbackend.domain.jobApplication.entity.JobApplication;
import com.wanted.preonboardingbackend.domain.jobApplication.repository.JobApplicationRepository;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import com.wanted.preonboardingbackend.domain.jobPosting.repository.JobPostingRepository;
import com.wanted.preonboardingbackend.domain.user.entity.User;
import com.wanted.preonboardingbackend.domain.user.repository.UserRepository;
import com.wanted.preonboardingbackend.global.dto.PageRequestDto;
import com.wanted.preonboardingbackend.global.dto.PageResponseDto;
import com.wanted.preonboardingbackend.global.enums.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 특정 사용자의 지원내역 목록 조회
     *
     * @param pageRequestDto 페이지 요청 정보가 포함된 PageRequestDto 객체
     * @param userId 사용자 ID
     * @return 페이징 처리가 된 해당 사용자의 지원내역 전체 목록
     */
    @Transactional(readOnly = true)
    public PageResponseDto<JobApplicationResponseDto> getApplications(PageRequestDto pageRequestDto, Long userId) {

        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1,
                pageRequestDto.getSize());

        Page<JobApplication> applications = jobApplicationRepository.findAllByUserId(pageable, userId);

        List<JobApplicationResponseDto> dtoList = applications.stream()
                .map(jobApplication -> new JobApplicationResponseDto(
                        jobApplication.getId(),
                        jobApplication.getUser().getName(),
                        jobApplication.getJobPosting().getId(),
                        jobApplication.getJobPosting().getCompany().getName()
                ))
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                dtoList,
                applications.getNumber() + 1,
                applications.getSize(),
                applications.getTotalElements(),
                applications.getTotalPages(),
                applications.isFirst(),
                applications.isLast(),
                applications.hasPrevious(),
                applications.hasNext()
        );
    }
}
