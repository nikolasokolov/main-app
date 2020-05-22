package com.graduation.mainapp.rest;

import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.service.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main")
public class ExportResource {

    private final ExportService exportService;

    @RequestMapping(value = "/daily-orders/{userId}/export", method = RequestMethod.POST)
    public ResponseEntity<Resource> exportDailyOrders(@PathVariable Long userId) throws IOException, JRException, NotFoundException {
        byte[] dailyOrdersBytes = exportService.exportDailyOrders(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "inline; filename=dailyOrdersReport.pdf")
                .contentLength(dailyOrdersBytes.length)
                .body(new ByteArrayResource(dailyOrdersBytes));
    }

}
