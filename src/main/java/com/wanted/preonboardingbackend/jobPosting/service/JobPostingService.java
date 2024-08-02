package com.wanted.preonboardingbackend.jobPosting.service;

import com.wanted.preonboardingbackend.company.domain.Company;
import com.wanted.preonboardingbackend.company.domain.CompanyRepository;
import com.wanted.preonboardingbackend.jobPosting.domain.JobPosting;
import com.wanted.preonboardingbackend.jobPosting.domain.JobPostingRepository;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingSaveRequestDto;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingResponseDto;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;

    /**
     * 채용공고 등록
     *
     * @param requestDto 등록할 채용공고 정보가 포함된 JobPostingSaveRequestDto 객체
     * @return 등록된 채용공고 정보가 포함된 JobPostingResponseDto 객체
     */
    @Transactional
    public JobPostingResponseDto save(JobPostingSaveRequestDto requestDto) {

        Company company = companyRepository.findById(requestDto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회사가 없습니다."));

        JobPosting jobPosting = JobPosting.builder()
                .company(company)
                .position(requestDto.getPosition())
                .reward(requestDto.getReward())
                .content(requestDto.getContent())
                .skill(requestDto.getSkill())
                .build();

        JobPosting savedJob = jobPostingRepository.save(jobPosting);

        return new JobPostingResponseDto(savedJob);
    }

    /**
     * 채용공고 수정
     *
     * @param id 채용공고 ID
     * @param requestDto 수정할 채용공고 정보가 포함된 JobPostingUpdateRequestDto 객체
     * @return 수정된 채용공고 정보가 포함된 JobPostingResponseDto 객체
     */
    @Transactional
    public JobPostingResponseDto update(Long id, JobPostingUpdateRequestDto requestDto) {

        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 채용공고가 없습니다."));


        jobPosting.update(requestDto.getPosition(),
                        requestDto.getReward(),
                        requestDto.getContent(),
                        requestDto.getSkill());

        return new JobPostingResponseDto(jobPosting);
    }
}
