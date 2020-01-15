package com.graduation.mainapp.rest;

import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import com.graduation.mainapp.service.ExportService;
import com.graduation.mainapp.service.OrderService;
import com.netflix.ribbon.proxy.annotation.Http;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    private static final String APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET
            = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final ExportService exportService;

    @RequestMapping(value = "/daily-orders/{restaurantId}/export", method = RequestMethod.GET)
    public ResponseEntity<?> exportDailyOrders(@PathVariable Long restaurantId) {
        HttpHeaders header = new HttpHeaders();
        byte[] dailyOrdersBytes = exportService.exportDailyOrders(restaurantId);
        header.setContentDispositionFormData("inline", "dailyOrdersReport.pdf");
        header.setContentType(MediaType.valueOf(APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET));
        header.setContentLength(dailyOrdersBytes.length);
        return new ResponseEntity<Object>(dailyOrdersBytes, header, HttpStatus.OK);
    }

}
