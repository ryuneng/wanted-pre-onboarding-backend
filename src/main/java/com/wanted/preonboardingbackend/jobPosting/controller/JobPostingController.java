package com.wanted.preonboardingbackend.jobPosting.controller;

import com.wanted.preonboardingbackend.dto.PageRequestDto;
import com.wanted.preonboardingbackend.dto.PageResponseDto;
import com.wanted.preonboardingbackend.jobPosting.dto.*;
import com.wanted.preonboardingbackend.jobPosting.service.JobPostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
@Tag(name = "채용공고 API", description = "채용공고에 대한 등록, 수정, 삭제, 조회 API를 제공합니다.")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @Operation(summary = "채용공고 등록", description = "채용공고를 등록합니다.")
    @PostMapping
    public ResponseEntity<JobPostingResponseDto> save(@Valid @RequestBody JobPostingSaveRequestDto requestDto) {

        JobPostingResponseDto responseDto = jobPostingService.save(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "채용공고 수정", description = "채용공고를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<JobPostingResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody JobPostingUpdateRequestDto requestDto) {

        JobPostingResponseDto responseDto = jobPostingService.update(id, requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "채용공고 삭제", description = "채용공고를 삭제합니다.")
    @DeleteMapping("/{id}")
    public Map<String, Long> delete(@PathVariable Long id) {

        jobPostingService.delete(id);

        return Map.of("SUCCESS DELETE", id);
    }

    @Operation(summary = "채용공고 목록 조회", description = "채용공고 전체 목록을 조회합니다.")
    @GetMapping("/list")
    public PageResponseDto<JobPostingListDto> getJobPostings(PageRequestDto pageRequestDto) {

        return jobPostingService.getJobPostings(pageRequestDto);
    }

    @Operation(summary = "채용공고 검색", description = "키워드를 통해 채용공고를 검색합니다.")
    @GetMapping("/search")
    public PageResponseDto<JobPostingListDto> searchJobPostings(PageRequestDto pageRequestDto, String keyword) {

        return jobPostingService.searchJobPostings(pageRequestDto, keyword);
    }

    @Operation(summary = "채용공고 상세 조회", description = "채용공고 상세 내용을 조회합니다.")
    @GetMapping("/detail")
    public JobPostingDetailDto getJobPostingDetail(Long id) {

        return jobPostingService.getJobPostingDetail(id);
    }
}
