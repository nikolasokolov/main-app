package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import com.graduation.mainapp.repository.dao.OrderDAO;
import com.graduation.mainapp.repository.dao.rowmapper.RestaurantDailyOrdersRowMapper;
import com.graduation.mainapp.service.ExportService;
import com.graduation.mainapp.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExportServiceImpl implements ExportService {
    private final OrderDAO orderDAO;
    private final RestaurantService restaurantService;

    @Override
    public void exportDailyOrders(Long restaurantId) throws FileNotFoundException, JRException, DomainObjectNotFoundException {
        Restaurant restaurant = restaurantService.findByIdOrThrow(restaurantId);
        String currentDate = LocalDate.now().toString();
        String path = "C:\\Users\\nsokolov\\Downloads";
        List<RestaurantDailyOrdersRowMapper> restaurantDailyOrders = orderDAO.getRestaurantDailyOrders(restaurantId);
        File file = ResourceUtils.getFile("classpath:reports/daily-orders.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(restaurantDailyOrders);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Nikola Sokolov");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(
                jasperPrint, path + "\\" + restaurant.getName() + "-daily-orders-" + currentDate + ".pdf");
    }
}
