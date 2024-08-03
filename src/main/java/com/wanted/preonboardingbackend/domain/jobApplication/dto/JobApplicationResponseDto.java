package com.wanted.preonboardingbackend.domain.jobApplication.dto;

import com.wanted.preonboardingbackend.domain.jobApplication.entity.JobApplication;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "채용 지원 응답 DTO")
public class JobApplicationResponseDto {

    @Schema(description = "지원내역 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이름", example = "김구직")
    private String userName;

    @Schema(description = "채용공고 ID", example = "10")
    private Long jobPostingId;

    @Schema(description = "회사명", example = "원티드랩")
    private String companyName;

    public JobApplicationResponseDto(JobApplication jobApplication) {
        this.id = jobApplication.getId();
        this.userName = jobApplication.getUser().getName();
        this.jobPostingId = jobApplication.getJobPosting().getId();
        this.companyName = jobApplication.getJobPosting().getCompany().getName();
    }
}
