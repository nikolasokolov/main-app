package com.graduation.mainapp.rest;

import com.graduation.mainapp.converter.OrderConverter;
import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.rest.dto.CompanyOrdersDTO;
import com.graduation.mainapp.rest.dto.OrderDTO;
import com.graduation.mainapp.rest.dto.RestaurantDailyOrdersDTO;
import com.graduation.mainapp.rest.dto.UserOrderDTO;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.service.ExportService;
import com.graduation.mainapp.service.InvoiceService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main")
public class OrderResource {

    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final ExportService exportService;
    private final OrderConverter orderConverter;

    @RequestMapping(value = "/orders/save", method = RequestMethod.POST)
    public ResponseEntity<UserOrderDTO> saveOrder(@RequestBody OrderDTO orderDTO) throws NotFoundException {
        log.info("Started creating a new order");
        Order order = orderService.save(orderDTO);
        UserOrderDTO userOrderDTO = orderConverter.convertToUserOrderResponseDTO(order);
        log.info("Finished creating a new order");
        return ResponseEntity.ok(userOrderDTO);
    }

    @RequestMapping(value = "/orders/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserOrderDTO> getDailyOrderForUser(@PathVariable Long userId) throws NotFoundException {
        log.info("Started fetching order for User with ID [{}]", userId);
        Order order = orderService.getDailyOrderForUser(userId);
        if (nonNull(order)) {
            UserOrderDTO userOrderDTO = orderConverter.convertToUserOrderResponseDTO(order);
            log.info("Finished fetching order for User with ID [{}]", userId);
            return ResponseEntity.ok(userOrderDTO);
        } else {
            log.info("User with ID=[{}] has not ordered today", userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/orders/{orderId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) throws NotFoundException {
        log.info("Started deleting Order with ID=[{}]", orderId);
        orderService.deleteOrder(orderId);
        log.info("Finished deleting Order with ID=[{}]", orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/company/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrdersForCompany(@PathVariable Long companyId) {
        log.info("Received request for fetching orders for company with ID [{}]", companyId);
        List<CompanyOrdersDTO> companyOrdersDTOS = orderService.getOrdersForCompany(companyId);
        log.info("Successfully fetched orders for company with ID [{}]", companyId);
        return new ResponseEntity<>(companyOrdersDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/daily-orders/company/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> getColleaguesChoicesForCompany(@PathVariable Long companyId) {
        log.info("Received request for fetching daily orders for company with ID [{}]", companyId);
        List<CompanyOrdersDTO> companyOrdersDTOS = orderService.getDailyOrdersForCompany(companyId);
        log.info("Successfully fetched daily orders for company with ID [{}]", companyId);
        return new ResponseEntity<>(companyOrdersDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/daily-orders/restaurant/{restaurantAccountId}", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantDailyOrdersDTO>> getDailyOrdersForRestaurant(
            @PathVariable Long restaurantAccountId) throws NotFoundException {
        log.info("Started fetching daily orders for restaurant with User ID=[{}]", restaurantAccountId);
        List<RestaurantDailyOrdersDTO> restaurantDailyOrdersDTOS = orderService
                .getDailyOrdersForRestaurant(restaurantAccountId);
        log.info("Started fetching daily orders for restaurant with User ID=[{}]", restaurantAccountId);
        return new ResponseEntity<>(restaurantDailyOrdersDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/company/{companyId}/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> sendInvoiceToCompany(@PathVariable Long userId, @PathVariable Long companyId) throws NotFoundException, IOException, JRException, MessagingException {
        log.info("Started sending invoice for Company with ID=[{}]", companyId);
        invoiceService.sendInvoiceToCompany(userId, companyId);
        log.info("Finished sending invoice for Company with ID=[{}]", companyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
