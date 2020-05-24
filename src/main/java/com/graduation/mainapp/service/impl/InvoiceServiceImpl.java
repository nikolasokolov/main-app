package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.AuthorityRepository;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.EmailService;
import com.graduation.mainapp.service.ExportService;
import com.graduation.mainapp.service.InvoiceService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;

import static com.graduation.mainapp.config.Constants.Authorities.ROLE_ADMIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final EmailService emailService;
    private final ExportService exportService;
    private final UserService userService;
    private final CompanyService companyService;
    private final AuthorityRepository authorityRepository;
    private final MailTemplateContextPopulator mailTemplateContextPopulator;

    @Override
    public void sendInvoiceToCompany(Long userId, Long companyId) throws NotFoundException, IOException, JRException, MessagingException {
        Authority authority = authorityRepository.findByName(ROLE_ADMIN);
        Company company = companyService.getCompany(companyId);
        User companyAdmin = userService.findByAuthoritiesAndCompany(authority, company);
        User restaurantAccount = userService.getUser(userId);
        String restaurantName = restaurantAccount.getRestaurant().getName();
        byte[] monthlyOrdersBytes = exportService.exportMonthlyOrders(companyId, userId);
        String currentMonth = LocalDate.now().getMonth().toString().toLowerCase();
        String month = currentMonth.substring(0, 1).toUpperCase() + currentMonth.substring(1);
        String subject = month + " invoice from " + restaurantName;
        String fileName = restaurantName + "-" + currentMonth + "-orders.pdf";
        String invoiceEmailTemplate = mailTemplateContextPopulator.populateInvoiceEmailTemplate(company, restaurantAccount);
        emailService.sendInvoiceViaMail(companyAdmin.getEmail(), subject, invoiceEmailTemplate, monthlyOrdersBytes, fileName);
    }
}
