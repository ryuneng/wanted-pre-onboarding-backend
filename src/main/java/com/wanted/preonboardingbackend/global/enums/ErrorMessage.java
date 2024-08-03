package com.wanted.preonboardingbackend.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    COMPANY_NOT_FOUND("해당 회사를 찾을 수 없습니다."),
    JOB_NOT_FOUND("해당 채용공고를 찾을 수 없습니다."),
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),
    PREVIOUS_APPLICATION("해당 채용공고에 이미 지원했습니다."),
    INTERNAL_SERVER_ERROR("서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");

    private final String message;
}
