package com.wanted.preonboardingbackend.jobPosting.controller;

import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingRequestDto;
import com.wanted.preonboardingbackend.jobPosting.dto.JobPostingResponseDto;
import com.wanted.preonboardingbackend.jobPosting.service.JobPostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
@Tag(name = "채용공고 API", description = "채용공고에 대한 등록, 수정, 삭제, 조회 API를 제공합니다.")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @Operation(summary = "채용공고 등록", description = "채용공고를 등록합니다.")
    @PostMapping
    public ResponseEntity<JobPostingResponseDto> saveJob(@Valid @RequestBody JobPostingRequestDto jobPostingRequestDto) {

        JobPostingResponseDto responseDto = jobPostingService.saveJob(jobPostingRequestDto);
        
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
