package com.graduation.mainapp.service;

import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface InvoiceService {
    void sendInvoiceToCompany(Long companyId, Long userId) throws DomainObjectNotFoundException, IOException, JRException;
}
