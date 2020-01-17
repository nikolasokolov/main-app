package com.graduation.mainapp.service;

import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface ExportService {
    byte[] exportDailyOrders(Long restaurantId) throws IOException, JRException, DomainObjectNotFoundException;
    byte[] exportMonthlyOrders(Long companyId, Long userId) throws IOException, JRException, DomainObjectNotFoundException;
}
