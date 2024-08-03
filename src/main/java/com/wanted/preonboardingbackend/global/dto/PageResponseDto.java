package com.wanted.preonboardingbackend.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "페이지 응답 DTO")
public class PageResponseDto<T> {

    @Schema(description = "특정 타입의 DTO 리스트")
    private List<T> dtoList;

    @Schema(description = "현재 페이지 번호", example = "1")
    private int page;

    @Schema(description = "한 페이지당 출력할 데이터의 개수", example = "10")
    private int size;
    
    @Schema(description = "총 데이터 개수", example = "25")
    private long totalElement;
    
    @Schema(description = "총 페이지 수", example = "3")
    private int totalPage;

    @Schema(description = "첫번째 페이지", example = "true")
    private boolean first;
    
    @Schema(description = "마지막 페이지", example = "false")
    private boolean last;

    @Schema(description = "이전 데이터 여부 체크", example = "false")
    private boolean prev;

    @Schema(description = "다음 데이터 여부 체크", example = "true")
    private boolean next;
}
