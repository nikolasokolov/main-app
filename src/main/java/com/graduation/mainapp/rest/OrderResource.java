package com.graduation.mainapp.rest;

import com.graduation.mainapp.converter.OrderConverter;
import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.rest.dto.CompanyOrdersDTO;
import com.graduation.mainapp.rest.dto.OrderDTO;
import com.graduation.mainapp.rest.dto.UserOrderDTO;
import com.graduation.mainapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main/orders")
public class OrderResource {

    private final OrderService orderService;
    private final OrderConverter orderConverter;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<UserOrderDTO> saveOrder(@RequestBody OrderDTO orderDTO) throws NotFoundException {
        log.info("Started creating a new order");
        Order order = orderService.save(orderDTO);
        UserOrderDTO userOrderDTO = orderConverter.convertToUserOrderResponseDTO(order);
        log.info("Finished creating a new order");
        return ResponseEntity.ok(userOrderDTO);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserOrderDTO> getDailyOrderForUser(@PathVariable Long userId) throws NotFoundException {
        log.info("Started fetching Order for User with ID=[{}]", userId);
        Order order = orderService.getDailyOrderForUser(userId);
        if (nonNull(order)) {
            UserOrderDTO userOrderDTO = orderConverter.convertToUserOrderResponseDTO(order);
            log.info("Finished fetching order for User with ID=[{}]", userId);
            return ResponseEntity.ok(userOrderDTO);
        } else {
            log.info("User with ID=[{}] has not ordered today", userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/{orderId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        log.info("Started deleting Order with ID=[{}]", orderId);
        orderService.deleteOrder(orderId);
        log.info("Finished deleting Order with ID=[{}]", orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/companies/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrdersForCompany(@PathVariable Long companyId) {
        log.info("Started fetching Orders for Company with ID=[{}]", companyId);
        List<CompanyOrdersDTO> companyOrdersDTOS = orderService.getOrdersForCompany(companyId);
        log.info("Finished fetching Orders for Company with ID=[{}]", companyId);
        return new ResponseEntity<>(companyOrdersDTOS, HttpStatus.OK);
    }
}
