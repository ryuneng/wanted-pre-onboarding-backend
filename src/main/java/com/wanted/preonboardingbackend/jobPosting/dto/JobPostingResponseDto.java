package com.wanted.preonboardingbackend.jobPosting.dto;

import com.wanted.preonboardingbackend.jobPosting.domain.JobPosting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "채용공고 응답 DTO")
public class JobPostingResponseDto {

    @Schema(description = "채용공고 ID", example = "1")
    private Long id;

    @Schema(description = "회사명", example = "원티드랩")
    private String companyName;

    @Schema(description = "채용 포지션", example = "백엔드 주니어 개발자")
    private String position;

    @Schema(description = "채용 보상금", example = "1000000")
    private int reward;

    @Schema(description = "채용 내용", example = "원티드랩의 백엔드 주니어 개발자를 채용합니다.")
    private String content;

    @Schema(description = "사용 기술", example = "Java & Spring")
    private String skill;

    public JobPostingResponseDto(JobPosting jobPosting) {
        this.id = jobPosting.getId();
        this.companyName = jobPosting.getCompany().getName();
        this.position = jobPosting.getPosition();
        this.reward = jobPosting.getReward();
        this.content = jobPosting.getContent();
        this.skill = jobPosting.getSkill();
    }
}
