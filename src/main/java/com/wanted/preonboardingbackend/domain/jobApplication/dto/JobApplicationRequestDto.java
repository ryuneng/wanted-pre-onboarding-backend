package com.wanted.preonboardingbackend.domain.jobApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채용 지원 요청 DTO")
public class JobApplicationRequestDto {

    @Schema(description = "채용공고 ID", example = "10")
    private Long jobPostingId;

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
}
