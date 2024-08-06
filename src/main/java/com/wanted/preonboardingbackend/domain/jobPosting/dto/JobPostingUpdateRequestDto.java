package com.wanted.preonboardingbackend.domain.jobPosting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채용공고 수정 요청 DTO")
public class JobPostingUpdateRequestDto {

    @NotBlank(message = "채용 포지션을 입력해주세요.")
    @Schema(description = "채용 포지션", example = "프론트엔드 주니어 개발자")
    private String position;

    @Schema(description = "채용 보상금", example = "1500000")
    private int reward;

    @NotBlank(message = "채용 내용을 입력해주세요.")
    @Schema(description = "채용 내용", example = "원티드랩의 프론트엔드 주니어 개발자를 채용합니다.")
    private String content;

    @NotBlank(message = "사용 기술을 입력해주세요.")
    @Schema(description = "사용 기술", example = "React")
    private String skill;
}
