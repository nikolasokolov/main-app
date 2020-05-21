package com.graduation.mainapp.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException(String message) {
        log.warn(message);
    }
}
