package com.sapondanai.spring_boot_challenge.exception;

public class DuplicateFieldException extends RuntimeException {

    public DuplicateFieldException(String field, String value) {
        super(field + " already taken: " + value);
    }
}
