package com.wanted.preonboardingbackend.domain.jobPosting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채용공고 리스트 DTO")
public class JobPostingListDto {

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
    
}
