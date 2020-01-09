package com.graduation.mainapp.rest;

import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.dto.CompanyOrdersResponseDTO;
import com.graduation.mainapp.dto.OrderDTO;
import com.graduation.mainapp.dto.RestaurantDailyOrdersResponseDTO;
import com.graduation.mainapp.dto.UserOrderResponseDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;
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

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/main")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class OrderResource {
    private final OrderService orderService;

    @RequestMapping(value = "/orders/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveOrder(@RequestBody OrderDTO orderDTO) throws DomainObjectNotFoundException {
        log.info("Received request for saving a new order");
        Order order = orderService.save(orderDTO);
        UserOrderResponseDTO userOrderResponseDTO = orderService.createUserOrderResponseDTO(order);
        log.info("Successfully saved new order");
        return ResponseEntity.ok(userOrderResponseDTO);
    }

    @RequestMapping(value = "/orders/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserOrder(@PathVariable Long userId) throws DomainObjectNotFoundException {
        log.info("Received request for getting order for user with ID [{}]", userId);
        Order order = orderService.findByUser(userId);
        if (Objects.nonNull(order)) {
            UserOrderResponseDTO userOrderResponseDTO = orderService.createUserOrderResponseDTO(order);
            log.info("Successfully fetched order for user with ID [{}]", userId);
            return ResponseEntity.ok(userOrderResponseDTO);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/orders/{orderId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) throws DomainObjectNotFoundException {
        log.info("Received request for deleting order with ID [{}]", orderId);
        orderService.delete(orderId);
        log.info("Successfully deleted order with ID [{}]", orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/company/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrdersForCompany(@PathVariable Long companyId) {
        log.info("Received request for fetching orders for company with ID [{}]", companyId);
        List<CompanyOrdersResponseDTO> companyOrdersResponseDTOs = orderService.getOrdersForCompany(companyId);
        log.info("Successfully fetched orders for company with ID [{}]", companyId);
        return new ResponseEntity<>(companyOrdersResponseDTOs, HttpStatus.OK);
    }

    @RequestMapping(value = "/daily-orders/company/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> getColleaguesChoicesForCompany(@PathVariable Long companyId) {
        log.info("Received request for fetching daily orders for company with ID [{}]", companyId);
        List<CompanyOrdersResponseDTO> companyOrdersResponseDTOs = orderService.getDailyOrdersForCompany(companyId);
        log.info("Successfully fetched daily orders for company with ID [{}]", companyId);
        return new ResponseEntity<>(companyOrdersResponseDTOs, HttpStatus.OK);
    }

    @RequestMapping(value = "/daily-orders/restaurant/{restaurantAccountId}", method = RequestMethod.GET)
    public ResponseEntity<?> getDailyOrdersForRestaurant(@PathVariable Long restaurantAccountId) throws DomainObjectNotFoundException {
        log.info("Received request for fetching daily orders for restaurant account with ID [{}]", restaurantAccountId);
        List<RestaurantDailyOrdersResponseDTO> restaurantDailyOrdersResponseDTOs = orderService
                .getDailyOrdersForRestaurant(restaurantAccountId);
        log.info("Successfully fetched daily orders for restaurant account with ID [{}]", restaurantAccountId);
        return new ResponseEntity<>(restaurantDailyOrdersResponseDTOs, HttpStatus.OK);
    }
}
