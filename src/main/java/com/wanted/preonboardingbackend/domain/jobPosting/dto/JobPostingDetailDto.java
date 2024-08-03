package com.wanted.preonboardingbackend.domain.jobPosting.dto;

import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "채용공고 상세 DTO")
public class JobPostingDetailDto {

    @Schema(description = "채용공고 ID", example = "1")
    private Long id;

    @Schema(description = "회사명", example = "원티드랩")
    private String companyName;

    @Schema(description = "국가", example = "한국")
    private String nation;

    @Schema(description = "지역", example = "서울")
    private String region;

    @Schema(description = "채용 포지션", example = "백엔드 주니어 개발자")
    private String position;

    @Schema(description = "채용 보상금", example = "1000000")
    private int reward;

    @Schema(description = "사용 기술", example = "Java & Spring")
    private String skill;

    @Schema(description = "채용 내용", example = "원티드랩의 백엔드 주니어 개발자를 채용합니다.")
    private String content;

    @Schema(description = "해당 회사가 올린 다른 채용공고 ID 리스트", example = "10, 11, 12")
    private List<Long> otherJobPostingIds = new ArrayList<>();

    @Builder
    public JobPostingDetailDto(JobPosting jobPosting, List<Long> otherJobPostingIds) {
        this.id = jobPosting.getId();
        this.companyName = jobPosting.getCompany().getName();
        this.nation = jobPosting.getCompany().getNation();
        this.region = jobPosting.getCompany().getRegion();
        this.position = jobPosting.getPosition();
        this.reward = jobPosting.getReward();
        this.skill = jobPosting.getSkill();
        this.content = jobPosting.getContent();
        this.otherJobPostingIds = otherJobPostingIds;
    }
}
