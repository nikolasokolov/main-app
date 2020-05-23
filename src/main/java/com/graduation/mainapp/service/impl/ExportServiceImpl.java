package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.exception.NotFoundException;
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
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final OrderDAO orderDAO;
    private final UserService userService;

    @Override
    public byte[] exportDailyOrders(Long userId) throws IOException, JRException, NotFoundException {
        User user = userService.getUser(userId);
        log.info("Started generating daily orders pdf report for Restaurant [{}]", user.getRestaurant().getName());
        List<RestaurantDailyOrdersRowMapper> restaurantDailyOrders = orderDAO.getRestaurantDailyOrders(user.getRestaurant().getId());

        File file = ResourceUtils.getFile("classpath:reports/daily-orders.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(restaurantDailyOrders);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
        ByteArrayOutputStream byteArrayOutputStream = getByteArrayOutputStream(jasperPrint);
        log.info("Finished generating daily orders pdf report for Restaurant [{}]", user.getRestaurant().getName());
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] exportMonthlyOrders(Long companyId, Long userId) throws IOException, JRException, NotFoundException {
        User user = userService.getUser(userId);
        log.info("Started generating Monthly Orders pdf report for Restaurant [{}]", user.getRestaurant().getName());
        List<CompanyMonthlyOrdersRowMapper> companyMonthlyOrdersRowMappers = orderDAO
                .getMonthlyOrdersForCompany(companyId, user.getRestaurant().getId());

        File file = ResourceUtils.getFile("classpath:reports/monthly-orders.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(companyMonthlyOrdersRowMappers);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
        ByteArrayOutputStream byteArrayOutputStream = getByteArrayOutputStream(jasperPrint);
        log.info("Finished generating monthly orders pdf report for Restaurant [{}]", user.getRestaurant().getName());
        return byteArrayOutputStream.toByteArray();
    }

    private ByteArrayOutputStream getByteArrayOutputStream(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
        return byteArrayOutputStream;
    }
}
