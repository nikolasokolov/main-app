package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import com.graduation.mainapp.repository.dao.OrderDAO;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyMonthlyOrdersRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.RestaurantDailyOrdersRowMapper;
import com.graduation.mainapp.service.ExportService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExportServiceImpl implements ExportService {
    private final OrderDAO orderDAO;
    private final UserService userService;

    @Override
    public byte[] exportDailyOrders(Long userId) throws IOException, JRException, DomainObjectNotFoundException {
        User user = userService.findByIdOrThrow(userId);
        log.info("Started generating daily orders pdf report for restaurant [{}]", user.getRestaurant().getName());
        List<RestaurantDailyOrdersRowMapper> restaurantDailyOrders = orderDAO.getRestaurantDailyOrders(user.getRestaurant().getId());
        File file = ResourceUtils.getFile("classpath:reports/daily-orders.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(restaurantDailyOrders);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("restaurant", user.getRestaurant().getName());
        parameters.put("today", LocalDate.now().toString());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        ByteArrayOutputStream byteArrayOutputStream = getByteArrayOutputStream(jasperPrint);
        log.info("Finished generating daily orders pdf report for restaurant [{}]", user.getRestaurant().getName());
        return byteArrayOutputStream.toByteArray();
    }

    private ByteArrayOutputStream getByteArrayOutputStream(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    @Override
    public byte[] exportMonthlyOrders(Long companyId, Long userId) throws IOException, JRException, DomainObjectNotFoundException {
        User user = userService.findByIdOrThrow(userId);
        log.info("Started generating monthly orders pdf report for restaurant [{}]", user.getRestaurant().getName());
        List<CompanyMonthlyOrdersRowMapper> companyMonthlyOrdersRowMappers = orderDAO
                .getMonthlyOrdersForCompany(companyId, user.getRestaurant().getId());
        File file = ResourceUtils.getFile("classpath:reports/monthly-orders.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(companyMonthlyOrdersRowMappers);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("restaurant", user.getRestaurant().getName());
        parameters.put("today", LocalDate.now().toString());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        ByteArrayOutputStream byteArrayOutputStream = getByteArrayOutputStream(jasperPrint);
        log.info("Finished generating monthly orders pdf report for restaurant [{}]", user.getRestaurant().getName());
        return byteArrayOutputStream.toByteArray();
    }

}
