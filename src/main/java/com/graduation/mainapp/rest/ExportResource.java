package com.graduation.mainapp.rest;

import com.graduation.mainapp.service.ExportService;
import com.graduation.mainapp.service.OrderService;
import com.netflix.ribbon.proxy.annotation.Http;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.FileNotFoundException;

@Slf4j
@RestController
@RequestMapping(value = "/main")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExportResource {
    private final ExportService exportService;

    @RequestMapping(value = "/daily-orders/{restaurantId}/export", method = RequestMethod.GET)
    public ResponseEntity<?> exportDailyOrders(@PathVariable Long restaurantId) throws FileNotFoundException, JRException {
        exportService.exportDailyOrders(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
