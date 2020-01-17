package com.graduation.mainapp.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidLogoException extends Exception {
    public InvalidLogoException(String message) {
        super(message);
        log.warn(message);
    }
}
