package com.csipon.crm.dto.mapper.impl;

import com.csipon.crm.dto.mapper.Mapper;
import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.dao.UserDao;
import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.domain.model.ComplaintStatus;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.real.RealComplaint;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ComplaintDto;
import com.csipon.crm.dto.row.ComplaintRowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
@Component
public class ComplaintMapper {

    private final OrderDao orderDao;
    private final UserDao userDao;

    @Autowired
    public ComplaintMapper(OrderDao orderDao, UserDao userDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
    }

    public Mapper<ComplaintDto, RealComplaint> dtoToModel() {
        return (dto, model) -> {
            dto.setMessage(dto.getMessage().trim());
            model.setId(dto.getId());
            model.setTitle(dto.getTitle());
            model.setMessage(dto.getMessage());
            if (dto.getStatus() != null) {
                model.setStatus(ComplaintStatus.valueOf(dto.getStatus()));
            }
            Order order = orderDao.findById(dto.getOrderId());
            model.setOrder(order);
            User customer = userDao.findById(dto.getCustomerId());
            model.setCustomer(customer);
        };
    }

    public Mapper<Complaint, ComplaintRowDto> modelToRowDto() {
        return (model, rowDto) -> {
            rowDto.setId(model.getId());
            rowDto.setTitle(model.getTitle());
            rowDto.setMessage(model.getMessage());
            rowDto.setStatus(model.getStatus().getName());
            rowDto.setCustomer(model.getCustomer().getId());
            rowDto.setOrder(model.getOrder().getId());
            rowDto.setOrderStatus(model.getOrder().getStatus().getName());
            rowDto.setProductTitle(model.getOrder().getProduct().getTitle());
            rowDto.setProductStatus(model.getOrder().getProduct().getStatus().getName());
            rowDto.setDate(model.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm")));
            if (model.getPmg() != null) {
                rowDto.setPmg(model.getPmg().getId());
            }
        };
    }

    public Mapper<String, AutocompleteDto> modelToAutocomplete() {
        return (modelTitle, autocompleteDto) -> {
            autocompleteDto.setValue(modelTitle);
        };
    }
}
