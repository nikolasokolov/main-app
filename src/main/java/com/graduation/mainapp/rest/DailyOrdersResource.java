package com.graduation.mainapp.rest;

import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.rest.dto.CompanyOrdersDTO;
import com.graduation.mainapp.rest.dto.RestaurantDailyOrdersDTO;
import com.graduation.mainapp.service.ExportService;
import com.graduation.mainapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main/daily-orders")
public class DailyOrdersResource {

    private final OrderService orderService;
    private final ExportService exportService;

    @RequestMapping(value = "/companies/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> getColleaguesChoicesForCompany(@PathVariable Long companyId) {
        log.info("Started fetching daily orders for Company with ID=[{}]", companyId);
        List<CompanyOrdersDTO> companyOrdersDTOs = orderService.getDailyOrdersForCompany(companyId);
        log.info("Finished fetching daily orders for Company with ID=[{}]", companyId);
        return new ResponseEntity<>(companyOrdersDTOs, HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurants/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantDailyOrdersDTO>> getDailyOrdersForRestaurant(
            @PathVariable Long userId) throws NotFoundException {
        log.info("Started fetching daily orders for Restaurant with User ID=[{}]", userId);
        List<RestaurantDailyOrdersDTO> restaurantDailyOrdersDTOs = orderService
                .getDailyOrdersForRestaurant(userId);
        log.info("Started fetching daily orders for Restaurant with User ID=[{}]", userId);
        return new ResponseEntity<>(restaurantDailyOrdersDTOs, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{userId}/export", method = RequestMethod.POST)
    public ResponseEntity<Resource> exportDailyOrders(@PathVariable Long userId) throws IOException, JRException, NotFoundException {
        log.info("Started exporting daily orders for Restaurant with User ID=[{}]", userId);
        byte[] dailyOrdersBytes = exportService.exportDailyOrders(userId);
        log.info("Finished exporting daily orders for Restaurant with User ID=[{}]", userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "inline; filename=dailyOrdersReport.pdf")
                .contentLength(dailyOrdersBytes.length)
                .body(new ByteArrayResource(dailyOrdersBytes));
    }
}
