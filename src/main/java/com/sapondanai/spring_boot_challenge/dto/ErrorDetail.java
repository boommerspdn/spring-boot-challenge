package com.sapondanai.spring_boot_challenge.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorDetail {

    private final int status;
    private final String message;
}
