package com.wanted.preonboardingbackend.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    NOT_FOUND_COMPANY("해당 회사를 찾을 수 없습니다."),
    NOT_FOUND_JOB("해당 채용공고를 찾을 수 없습니다."),
    NOT_FOUND_USER("해당 유저를 찾을 수 없습니다.");

    private final String message;
}
