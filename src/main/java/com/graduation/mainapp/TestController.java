package com.graduation.mainapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {
    @RequestMapping(value = "/api/test", method = RequestMethod.GET)
    public ResponseEntity test() {
        log.info("Test request received");
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
