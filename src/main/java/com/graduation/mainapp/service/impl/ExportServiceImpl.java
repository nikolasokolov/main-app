package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.repository.dao.OrderDAO;
import com.graduation.mainapp.repository.dao.rowmapper.RestaurantDailyOrdersRowMapper;
import com.graduation.mainapp.service.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExportServiceImpl implements ExportService {
    private final OrderDAO orderDAO;

    @Override
    public byte[] exportDailyOrders(Long restaurantId) {
        SimpleOutputStreamExporterOutput exporterOutput = null;
        byte[] bytes = new byte[0];

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            List<RestaurantDailyOrdersRowMapper> restaurantDailyOrders = orderDAO.getRestaurantDailyOrders(restaurantId);
            File file = ResourceUtils.getFile("classpath:reports/daily-orders.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(restaurantDailyOrders);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Nikola Sokolov");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporterOutput = new SimpleOutputStreamExporterOutput(byteArrayOutputStream);
            exporter.setExporterOutput(exporterOutput);
            exporter.exportReport();
            return byteArrayOutputStream.toByteArray();
        } catch (JRException | IOException e) {
            log.error("Unable to generate Radio Rating Report of type Excel.", e);
            return bytes;
        } finally {
            if (exporterOutput != null) {
                exporterOutput.close();
            }
        }
    }

}
