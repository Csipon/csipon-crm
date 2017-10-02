package com.csipon.crm.excel.service.impl;

import com.csipon.crm.dao.ComplaintDao;
import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.dto.ComplaintExcelDto;
import com.csipon.crm.excel.ExcelMapKey;
import com.csipon.crm.excel.converter.ExcelConverter;
import com.csipon.crm.excel.drawer.ExcelDrawer;
import com.csipon.crm.excel.service.AbstractExcelService;
import com.csipon.crm.excel.service.ComplaintExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pasha on 26.05.2017.
 */
@Service
public class ComplaintExcelServiceImpl extends AbstractExcelService<Complaint> implements ComplaintExcelService{
    private final ComplaintDao complaintDao;
    private final ExcelConverter converter;

    private String[] titles = {
            "№",
            "Customer full name",
            "Complaint title",
            "Product title",
            "Complaint status",
            "Created date"
    };

    @Autowired
    public ComplaintExcelServiceImpl(ExcelConverter converter, ExcelDrawer drawer, ComplaintDao complaintDao) {
        super(drawer);
        this.converter = converter;
        this.complaintDao = complaintDao;
    }


    @Override
    public void generateCustomerComplaints(OutputStream stream, ComplaintExcelDto complaintExcelDto) throws IOException {
        LocalDate from = convertString(complaintExcelDto.getDateFrom());
        LocalDate to = convertString(complaintExcelDto.getDateTo());
        complaintExcelDto.setIdProduct(checkId(complaintExcelDto.getIdProduct()));
        List<Complaint> complaints = complaintDao.findAllByProductIds(Arrays.asList(complaintExcelDto.getIdProduct()), from, to, complaintExcelDto.getOrderByIndex());
        LocalDate[] range = calculateRange(from, to);
        Workbook workbook = parseData(complaints, range);
        workbook.write(stream);
        stream.close();
    }


    @Override
    public Map<ExcelMapKey, List<?>> convertToMap(List<Complaint> objects) {
        return converter.convertComplaintsToMap(objects);
    }

    @Override
    public Map<LocalDate, Map<String, Integer>> prepareDataChart(LocalDate[] range, List<Complaint> objects) {
        Map<LocalDate, Map<String, Integer>> result = new LinkedHashMap<>();
        LocalDateTime temp = null;
        for (LocalDate date : range) {
            LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIN);
            Map<String, Integer> products = new LinkedHashMap<>();
            for (Complaint complaint : objects) {
                LocalDateTime complaintDate = complaint.getDate();
                int value = 0;
                if ((dateTime.isAfter(complaintDate) && complaintDate.isAfter(temp)) || (temp == null && dateTime.isAfter(complaintDate))) {
                    value = 1;
                }
                products.merge(complaint.getOrder().getProduct().getTitle(), value, (a, b) -> a + b);
            }
            temp = LocalDateTime.from(dateTime);
            result.put(date, products);
        }
        return result;
    }

    @Override
    public String[] getTitles() {
        return titles;
    }
}
