package com.graduation.mainapp.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        log.warn(message);
    }
}
