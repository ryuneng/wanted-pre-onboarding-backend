package com.wanted.preonboardingbackend.jobPosting.service;

import com.wanted.preonboardingbackend.company.domain.Company;
import com.wanted.preonboardingbackend.company.domain.CompanyRepository;
import com.wanted.preonboardingbackend.jobPosting.domain.JobPosting;
import com.wanted.preonboardingbackend.jobPosting.domain.JobPostingRepository;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingRequestDto;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Schema(description = "채용공고 응답 DTO")
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;

    /**
     * 채용공고 등록
     *
     * @param dto 등록할 채용공고 정보가 포함된 JobPostingRequestDto 객체
     * @return 등록된 채용공고 정보가 포함된 JobPostingResponseDto 객체
     */
    @Transactional
    public JobPostingResponseDto saveJob(JobPostingRequestDto dto) {

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("회사를 찾을 수 없습니다."));

        JobPosting jobPosting = JobPosting.builder()
                .company(company)
                .position(dto.getPosition())
                .reward(dto.getReward())
                .content(dto.getContent())
                .skill(dto.getSkill())
                .build();

        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);

        return new JobPostingResponseDto(savedJobPosting);
    }
}
