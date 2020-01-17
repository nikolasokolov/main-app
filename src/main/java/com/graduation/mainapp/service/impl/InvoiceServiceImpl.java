package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import com.graduation.mainapp.service.EmailService;
import com.graduation.mainapp.service.ExportService;
import com.graduation.mainapp.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InvoiceServiceImpl implements InvoiceService {
    private final EmailService emailService;
    private final ExportService exportService;

    @Override
    public void sendInvoiceToCompany(Long companyId, Long userId) throws DomainObjectNotFoundException, IOException, JRException {
        byte[] monthlyOrdersBytes = exportService.exportMonthlyOrders(companyId, userId);
        emailService.sendInvoiceViaMail("nikacleski@live.com", "Monthly Invoice",
                "Dear company, here is the monthly invoice. Kind regards.", monthlyOrdersBytes, "monthly-orders.pdf");
    }
}
