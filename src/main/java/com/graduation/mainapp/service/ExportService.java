package com.graduation.mainapp.service;

import com.graduation.mainapp.exception.NotFoundException;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface ExportService {

    byte[] exportDailyOrders(Long restaurantId) throws IOException, JRException, NotFoundException;

    byte[] exportMonthlyOrders(Long companyId, Long userId) throws IOException, JRException, NotFoundException;
}
