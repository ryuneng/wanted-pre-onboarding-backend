package com.wanted.preonboardingbackend.domain.jobPosting.service;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import com.wanted.preonboardingbackend.domain.company.repository.CompanyRepository;
import com.wanted.preonboardingbackend.domain.jobPosting.dto.*;
import com.wanted.preonboardingbackend.global.dto.PageRequestDto;
import com.wanted.preonboardingbackend.global.dto.PageResponseDto;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import com.wanted.preonboardingbackend.domain.jobPosting.repository.JobPostingRepository;
import com.wanted.preonboardingbackend.global.enums.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
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
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage()));

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
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.JOB_NOT_FOUND.getMessage()));


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
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.JOB_NOT_FOUND.getMessage()));

        jobPostingRepository.delete(jobPosting);
    }

    /**
     * 채용공고 목록 조회
     *
     * @param pageRequestDto 페이지 요청 정보가 포함된 PageRequestDto 객체
     * @return 페이징 처리가 된 채용공고 전체 목록
     */
    public PageResponseDto<JobPostingListDto> getJobPostings(PageRequestDto pageRequestDto) {

        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1, // 페이지 번호를 1부터 시작하기 위해 -1
                pageRequestDto.getSize(),
                Sort.by("id").descending());

        Page<JobPosting> jobPostings = jobPostingRepository.findAll(pageable);

        return mapToPageResponseDto(jobPostings);
    }

    /**
     * 채용공고 검색
     *
     * @param pageRequestDto 페이지 요청 정보가 포함된 PageRequestDto 객체
     * @param keyword 검색할 키워드
     * @return 특정 키워드를 포함하여 페이징 처리된 채용공고 목록
     */
    public PageResponseDto<JobPostingListDto> searchJobPostings(PageRequestDto pageRequestDto, String keyword) {

        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("id").descending());

        Page<JobPosting> jobPostings;

        if (StringUtils.hasText(keyword)) {
            jobPostings = jobPostingRepository.findByKeyword(keyword, pageable);
        } else {
            jobPostings = jobPostingRepository.findAll(pageable);
        }

        return mapToPageResponseDto(jobPostings);
    }

    /**
     * JobPosting 엔티티의 Page 객체를 JobPostingListDto 타입의 PageResponseDto로 변환하는 메소드
     *
     * @param jobPostings JobPosting 엔티티의 Page 객체
     * @return 채용공고 목록과 페이지 정보를 포함한 PageResponseDto 객체
     */
    private PageResponseDto<JobPostingListDto> mapToPageResponseDto(Page<JobPosting> jobPostings) {

        List<JobPostingListDto> dtoList = jobPostings.stream().map(jobPosting -> new JobPostingListDto(
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
                jobPostings.getNumber() + 1, // 응답 페이지 번호를 1부터 시작하도록 +1
                jobPostings.getSize(),
                jobPostings.getTotalElements(),
                jobPostings.getTotalPages(),
                jobPostings.isFirst(),
                jobPostings.isLast(),
                jobPostings.hasPrevious(),
                jobPostings.hasNext()
        );
    }

    /**
     * 채용공고 상세 조회
     *
     * @param id 조회할 채용공고 ID
     * @return 채용내용과 해당 회사가 올린 다른 채용공고가 추가적으로 포함된 JobPostingDetailDto 객체
     */
    public JobPostingDetailDto getJobPostingDetail(Long id) {

        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.JOB_NOT_FOUND.getMessage()));

        Long companyId = jobPosting.getCompany().getId();

        List<Long> otherJobPostingIds = jobPostingRepository.findOtherJobsById(id, companyId);

        return new JobPostingDetailDto(jobPosting, otherJobPostingIds);
    }
}
