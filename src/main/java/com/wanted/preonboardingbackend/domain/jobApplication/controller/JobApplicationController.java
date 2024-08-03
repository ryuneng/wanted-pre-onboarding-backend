package com.wanted.preonboardingbackend.domain.jobApplication.controller;

import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationRequestDto;
import com.wanted.preonboardingbackend.domain.jobApplication.dto.JobApplicationResponseDto;
import com.wanted.preonboardingbackend.domain.jobApplication.service.JobApplicationService;
import com.wanted.preonboardingbackend.global.dto.PageRequestDto;
import com.wanted.preonboardingbackend.global.dto.PageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
@Tag(name = "채용 지원 관련 API", description = "채용 지원에 대한 등록, 조회 API를 제공합니다.")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @Operation(summary = "채용 지원", description = "사용자가 채용공고에 지원합니다. * 재지원은 불가합니다.")
    @PostMapping
    public ResponseEntity<JobApplicationResponseDto> save(@RequestBody JobApplicationRequestDto requestDto) {

        JobApplicationResponseDto responseDto = jobApplicationService.save(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "사용자의 지원내역 목록 조회", description = "특정 사용자의 지원내역 전체 목록을 조회합니다.")
    @GetMapping("/list/{userId}")
    public PageResponseDto<JobApplicationResponseDto> getApplications(PageRequestDto pageRequestDto, @PathVariable Long userId) {

        return jobApplicationService.getApplications(pageRequestDto, userId);
    }
}
