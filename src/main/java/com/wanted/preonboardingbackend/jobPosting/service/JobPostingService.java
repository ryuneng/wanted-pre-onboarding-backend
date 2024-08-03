package com.wanted.preonboardingbackend.jobPosting.service;

import com.wanted.preonboardingbackend.company.domain.Company;
import com.wanted.preonboardingbackend.company.domain.CompanyRepository;
import com.wanted.preonboardingbackend.dto.PageRequestDto;
import com.wanted.preonboardingbackend.dto.PageResponseDto;
import com.wanted.preonboardingbackend.jobPosting.domain.JobPosting;
import com.wanted.preonboardingbackend.jobPosting.domain.JobPostingRepository;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingListDto;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingSaveRequestDto;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingResponseDto;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
     * @param id 수정할 채용공고 ID
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

    /**
     * 채용공고 삭제
     *
     * @param id 삭제할 채용공고 ID
     */
    @Transactional
    public void delete(Long id) {

        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 채용공고가 없습니다."));

        jobPostingRepository.delete(jobPosting);
    }

    /**
     * 채용공고 목록 조회
     *
     * @param pageRequestDto 페이지 요청 정보가 포함된 PageRequestDto 객체
     * @return 페이징처리가 된 채용공고 전체 목록
     */
    @Transactional(readOnly = true)
    public PageResponseDto<JobPostingListDto> getJobPostings(PageRequestDto pageRequestDto) {

        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1, // 페이지 번호를 1부터 시작하기 위해 -1
                pageRequestDto.getSize(),
                Sort.by("id").descending());

        Page<JobPosting> jobs = jobPostingRepository.findAll(pageable);

        List<JobPostingListDto> dtoList = jobs.stream().map(jobPosting -> new JobPostingListDto(
                jobPosting.getId(),
                jobPosting.getCompany().getName(),
                jobPosting.getCompany().getNation(),
                jobPosting.getCompany().getRegion(),
                jobPosting.getPosition(),
                jobPosting.getReward(),
                jobPosting.getSkill()
        ))
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                dtoList,
                jobs.getNumber() + 1, // 응답 페이지 번호를 1부터 시작하도록 +1
                jobs.getSize(),
                jobs.getTotalElements(),
                jobs.getTotalPages(),
                jobs.isFirst(),
                jobs.isLast(),
                jobs.hasPrevious(),
                jobs.hasNext()
        );
    }
}
