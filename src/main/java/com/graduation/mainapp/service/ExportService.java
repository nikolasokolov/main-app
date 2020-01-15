package com.graduation.mainapp.service;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface ExportService {
    void exportDailyOrders(Long restaurantId) throws FileNotFoundException, JRException;
}
