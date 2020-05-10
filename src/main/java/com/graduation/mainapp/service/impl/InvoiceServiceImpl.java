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

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InvoiceServiceImpl implements InvoiceService {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final EmailService emailService;
    private final ExportService exportService;
    private final UserService userService;
    private final CompanyService companyService;
    private final AuthorityRepository authorityRepository;

    @Override
    public void sendInvoiceToCompany(Long userId, Long companyId) throws NotFoundException, IOException, JRException {
        Authority authority = authorityRepository.findByName(ROLE_ADMIN);
        Company company = companyService.findByIdOrThrow(companyId);
        User companyAdmin = userService.findByAuthoritiesAndCompany(authority, company);
        User restaurantAccount = userService.getUser(userId);
        String restaurantName = restaurantAccount.getRestaurant().getName();
        byte[] monthlyOrdersBytes = exportService.exportMonthlyOrders(companyId, userId);
        String currentMonth = LocalDate.now().getMonth().toString().toLowerCase();
        String month = currentMonth.substring(0, 1).toUpperCase() + currentMonth.substring(1);
        String subject = month + " invoice for " + company.getName();
        String body = "Dear " + company.getName() + ", <br/> In addition to this e-mail, you can find the monthly invoice " +
                "from restaurant " + restaurantName + ". <br/> Kind regards, <br/>" + restaurantName + " team.";
        String fileName = restaurantName + "-" + currentMonth + "-orders.pdf";
        emailService.sendInvoiceViaMail(companyAdmin.getEmail(), subject,
                body, monthlyOrdersBytes, fileName);
    }
}
