package com.graduation.mainapp.service;

import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface ExportService {
    void exportDailyOrders(Long restaurantId) throws FileNotFoundException, JRException, DomainObjectNotFoundException;
}
