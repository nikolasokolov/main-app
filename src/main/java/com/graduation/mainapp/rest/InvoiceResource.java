package com.graduation.mainapp.rest;

import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main/invoices")
public class InvoiceResource {

    private final InvoiceService invoiceService;

    @RequestMapping(value = "/companies/{companyId}/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> sendInvoiceToCompany(@PathVariable Long userId, @PathVariable Long companyId) throws NotFoundException, IOException, JRException, MessagingException {
        log.info("Started sending invoice for Company with ID=[{}]", companyId);
        invoiceService.sendInvoiceToCompany(userId, companyId);
        log.info("Finished sending invoice for Company with ID=[{}]", companyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
