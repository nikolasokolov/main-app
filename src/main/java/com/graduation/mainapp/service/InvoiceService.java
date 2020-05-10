package com.graduation.mainapp.service;

import com.graduation.mainapp.exception.NotFoundException;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface InvoiceService {
    void sendInvoiceToCompany(Long companyId, Long userId) throws NotFoundException, IOException, JRException;
}
