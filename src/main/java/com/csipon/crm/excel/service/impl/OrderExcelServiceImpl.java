package com.csipon.crm.excel.service.impl;

import com.csipon.crm.dto.OrderExcelDto;
import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.excel.ExcelMapKey;
import com.csipon.crm.excel.converter.ExcelConverter;
import com.csipon.crm.excel.drawer.ExcelDrawer;
import com.csipon.crm.excel.service.AbstractExcelService;
import com.csipon.crm.excel.service.OrderExcelService;
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
 * Created by Pasha on 22.05.2017.
 */
@Service
public class OrderExcelServiceImpl extends AbstractExcelService<Order> implements OrderExcelService {
    private final OrderDao orderDao;
    private final ExcelConverter converter;
    private String[] titles = {
            "№",
            "Customer full name",
            "Product title",
            "Order date",
            "Prefered date",
            "Order status",
    };

    @Autowired
    public OrderExcelServiceImpl(OrderDao orderDao, ExcelConverter converter, ExcelDrawer excelDrawer) {
        super(excelDrawer);
        this.orderDao = orderDao;
        this.converter = converter;
    }


    public void generateCustomerOrders(OutputStream stream, OrderExcelDto orderExcelDto) throws IOException {
        LocalDate from = convertString(orderExcelDto.getDateFrom());
        LocalDate to = convertString(orderExcelDto.getDateTo());
        orderExcelDto.setIdCustomer(checkId(orderExcelDto.getIdCustomer()));
        List<Order> orders = orderDao.findAllByCustomerIds(Arrays.asList(orderExcelDto.getIdCustomer()), from, to, orderExcelDto.getOrderByIndex());
        LocalDate[] range = calculateRange(from, to);
        Workbook workbook = parseData(orders, range);
        workbook.write(stream);
        stream.close();
    }

    public Map<LocalDate, Map<String, Integer>> prepareDataChart(LocalDate[] range, List<Order> orders) {
        Map<LocalDate, Map<String, Integer>> result = new LinkedHashMap<>();
        LocalDateTime temp = null;
        for (LocalDate date : range) {
            LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIN);
            Map<String, Integer> products = new LinkedHashMap<>();
            for (Order order : orders) {
                LocalDateTime orderDate = order.getDate();
                int value = 0;
                if ((dateTime.isAfter(orderDate) && orderDate.isAfter(temp)) || (temp == null && dateTime.isAfter(orderDate))) {
                    value = 1;
                }
                products.merge(order.getProduct().getTitle(), value, (a, b) -> a + b);
            }
            temp = LocalDateTime.from(dateTime);
            result.put(date, products);
        }
        return result;
    }

    @Override
    public Map<ExcelMapKey, List<?>> convertToMap(List<Order> objects) {
        return converter.convertOrdersToMap(objects);
    }

    @Override
    public String[] getTitles() {
        return titles;
    }
}
