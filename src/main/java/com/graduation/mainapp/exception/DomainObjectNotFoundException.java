package com.graduation.mainapp.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DomainObjectNotFoundException extends Exception {
    public DomainObjectNotFoundException(String message) {
        super(message);
        log.warn(message);
    }
}
