package com.csipon.crm.excel.converter;

import com.csipon.crm.domain.model.*;
import com.netcracker.crm.domain.model.*;
import com.csipon.crm.excel.ExcelMapKey;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.csipon.crm.excel.ExcelMapKey.*;

/**
 * Created by Pasha on 22.05.2017.
 */
@Component
public class ExcelConverter {

    public Map<ExcelMapKey, List<?>> convertOrdersToMap(List<Order> orders) {
        List<LocalDateTime> preferedDates = new ArrayList<>();
        List<LocalDateTime> orderDates = new ArrayList<>();
        List<String> productTitles = new ArrayList<>();
        List<String> custFullNames = new ArrayList<>();
        List<OrderStatus> statuses = new ArrayList<>();

        for (Order order : orders) {
            custFullNames.add(getFullName(order.getCustomer()));
            productTitles.add(order.getProduct().getTitle());
            preferedDates.add(order.getPreferedDate());
            orderDates.add(order.getDate());
            statuses.add(order.getStatus());
        }

        Map<ExcelMapKey, List<?>> result = new LinkedHashMap<>();
        result.put(FULL_NAME, custFullNames);
        result.put(PRODUCT_TITLE, productTitles);
        result.put(DATE, orderDates);
        result.put(PREFERED_DATE, preferedDates);
        result.put(STATUS, statuses);
        return result;
    }

    public Map<ExcelMapKey, List<?>> convertComplaintsToMap(List<Complaint> complaints) {
        List<String> custFullNames = new ArrayList<>();
        List<LocalDateTime> complaintDates = new ArrayList<>();
        List<String> productTitles = new ArrayList<>();
        List<String> complaintTitles = new ArrayList<>();
        List<ComplaintStatus> statuses = new ArrayList<>();

        for (Complaint complaint : complaints) {
            custFullNames.add(getFullName(complaint.getCustomer()));
            complaintTitles.add(complaint.getTitle());
            productTitles.add(complaint.getOrder().getProduct().getTitle());
            complaintDates.add(complaint.getDate());
            statuses.add(complaint.getStatus());
        }

        Map<ExcelMapKey, List<?>> result = new LinkedHashMap<>();
        result.put(FULL_NAME, custFullNames);
        result.put(COMPLAINT_TITLE, complaintTitles);
        result.put(PRODUCT_TITLE, productTitles);
        result.put(STATUS, statuses);
        result.put(DATE, complaintDates);
        return result;
    }


    private String getFullName(User user) {
        return user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
    }
}
