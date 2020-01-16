package com.graduation.mainapp.service;

import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface ExportService {
    byte[] exportDailyOrders(Long restaurantId) throws IOException, JRException;
}
