package com.csipon.crm.controller.excel;

import com.csipon.crm.dto.ComplaintExcelDto;
import com.csipon.crm.dto.OrderExcelDto;
import com.csipon.crm.excel.service.OrderExcelService;
import com.csipon.crm.excel.service.ComplaintExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by Pasha on 22.05.2017.
 */
@RestController
public class ExcelController {
    private final OrderExcelService orderExcelService;
    private final ComplaintExcelService complaintExcelService;

    @Autowired
    public ExcelController(OrderExcelService orderExcelService, ComplaintExcelService complaintExcelService) {
        this.orderExcelService = orderExcelService;
        this.complaintExcelService = complaintExcelService;
    }

    @GetMapping(path = "/*/report/users")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<?> reportByUsers(HttpServletResponse response, OrderExcelDto orderExcelDto) throws IOException {
        response.setContentType("application/xlsx");
        response.setHeader("Content-Disposition", "attachment; filename=Orders-" + LocalDate.now() + ".xlsx");
        orderExcelService.generateCustomerOrders(response.getOutputStream(), orderExcelDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/*/report/complaints")
    @PreAuthorize("hasAnyRole('ROLE_PMG', 'ROLE_ADMIN')")
    public ResponseEntity<?> reportByComplaints(HttpServletResponse response, ComplaintExcelDto complaintExcelDto) throws IOException {
        response.setContentType("application/xlsx");
        response.setHeader("Content-Disposition", "attachment; filename=Complaints-" + LocalDate.now() + ".xlsx");
        complaintExcelService.generateCustomerComplaints(response.getOutputStream(), complaintExcelDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
