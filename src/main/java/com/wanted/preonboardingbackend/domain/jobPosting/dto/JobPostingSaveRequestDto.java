package com.wanted.preonboardingbackend.domain.jobPosting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채용공고 등록 요청 DTO")
public class JobPostingSaveRequestDto {

    @NotNull(message = "회사 ID는 Null이 될 수 없습니다.")
    @Schema(description = "회사 ID", example = "1")
    private Long companyId;

    @NotBlank(message = "채용 포지션을 입력해주세요.")
    @Schema(description = "채용 포지션", example = "백엔드 주니어 개발자")
    private String position;

    @Schema(description = "채용 보상금", example = "1000000")
    private int reward;

    @NotBlank(message = "채용 내용을 입력해주세요.")
    @Schema(description = "채용 내용", example = "원티드랩의 백엔드 주니어 개발자를 채용합니다.")
    private String content;

    @NotBlank(message = "사용 기술을 입력해주세요.")
    @Schema(description = "사용 기술", example = "Java & Spring")
    private String skill;
}
