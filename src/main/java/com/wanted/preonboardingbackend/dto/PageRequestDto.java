package com.wanted.preonboardingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "페이지 요청 DTO")
public class PageRequestDto {

    @Builder.Default
    @Schema(description = "페이지 번호", example = "1")
    private int page = 1;

    @Builder.Default
    @Schema(description = "한 페이지당 출력할 데이터의 개수", example = "10")
    private int size = 10;
}
