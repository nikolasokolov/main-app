package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MailTemplateContextPopulator {

    private final SpringTemplateEngine springTemplateEngine;

    public String populateInvoiceEmailTemplate(Company company, User restaurantAccount) {
        String currentMonth = LocalDate.now().getMonth().toString().toLowerCase();
        String month = currentMonth.substring(0, 1).toUpperCase() + currentMonth.substring(1);
        Context context = new Context();
        context.setVariable("companyName", company.getName());
        context.setVariable("restaurantName", restaurantAccount.getRestaurant().getName());
        context.setVariable("month", month);
        return springTemplateEngine.process("invoiceTemplate", context);
    }
}
