package com.graduation.mainapp.rest;

import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.service.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main")
public class ExportResource {

    private final ExportService exportService;

    @RequestMapping(value = "/daily-orders/{userId}/export", method = RequestMethod.POST)
    public void exportDailyOrders(@PathVariable Long userId, HttpServletResponse httpServletResponse)
            throws IOException, JRException, NotFoundException {
        byte[] dailyOrdersBytes = exportService.exportDailyOrders(userId);
        ByteArrayOutputStream out = new ByteArrayOutputStream(dailyOrdersBytes.length);
        out.write(dailyOrdersBytes, 0, dailyOrdersBytes.length);

        httpServletResponse.setContentType("application/pdf");
        httpServletResponse.addHeader("Content-Disposition", "inline; filename=dailyOrdersReport.pdf");

        OutputStream outputStream;
        try {
            outputStream = httpServletResponse.getOutputStream();
            out.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
