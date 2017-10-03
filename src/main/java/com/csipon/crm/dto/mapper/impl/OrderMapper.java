package com.csipon.crm.dto.mapper.impl;

import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.dao.UserDao;
import com.csipon.crm.domain.model.*;
import com.csipon.crm.domain.real.RealOrder;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.OrderDto;
import com.csipon.crm.dto.OrderHistoryDto;
import com.csipon.crm.dto.OrderViewDto;
import com.csipon.crm.dto.mapper.Mapper;
import com.csipon.crm.dto.row.OrderRowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
@Component
public class OrderMapper {

    private static final DateTimeFormatter FORMATTER_FULL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final ProductDao productDao;
    private final UserDao userDao;

    @Autowired
    public OrderMapper(ProductDao productDao, UserDao userDao) {
        this.productDao = productDao;
        this.userDao = userDao;
    }

    public Mapper<OrderDto, RealOrder> dtoToModel() {
        return (dto, model) -> {
            Product product = productDao.findById(dto.getProductId());
            User customer = userDao.findById(dto.getCustomerId());

            model.setProduct(product);
            model.setCustomer(customer);
            model.setStatus(OrderStatus.NEW);
            model.setDate(LocalDateTime.now());

            StringBuilder preferredDataTime = new StringBuilder();
            if (!dto.getPreferredDate().isEmpty()) {
                preferredDataTime.append(dto.getPreferredDate());
                if (!dto.getPreferredTime().isEmpty()) {
                    preferredDataTime.append('T');
                    preferredDataTime.append(dto.getPreferredTime());
                }
            }
            if (preferredDataTime.length() != 0) {
                model.setPreferedDate(LocalDateTime.parse(preferredDataTime));
            }
        };
    }

    public Mapper<Order, OrderRowDto> modelToRowDto() {
        return (model, rowDto) -> {
            rowDto.setId(model.getId());
            rowDto.setStatus(model.getStatus().getName());
            rowDto.setProductId(model.getProduct().getId());
            rowDto.setProductTitle(model.getProduct().getTitle());
            rowDto.setProductStatus(model.getProduct().getStatus().getName());
            rowDto.setCustomer(model.getCustomer().getId());
            if (model.getCsr() != null) {
                rowDto.setCsr(model.getCsr().getId());
            }
            if (model.getDate() != null) {
                rowDto.setDateFinish(model.getDate().format(FORMATTER_FULL));
            }
            if (model.getPreferedDate() != null) {
                rowDto.setPreferredDate(model.getPreferedDate().format(FORMATTER_FULL));
            }
        };
    }

    public Mapper<Order, OrderViewDto> modelToOrderViewDto() {
        return (model, orderViewDto) -> {
            orderViewDto.setId(model.getId());
            orderViewDto.setStatus(model.getStatus().getName());
            orderViewDto.setTitle(model.getProduct().getTitle());
            orderViewDto.setDate(FORMATTER.format(model.getPreferedDate()));
            if (model.getStatus() == OrderStatus.PROCESSING) {
                orderViewDto.setTimeOver(LocalDateTime.now().isAfter(model.getPreferedDate()));;
            }
        };
    }

    public Mapper<Order, AutocompleteDto> modelToAutocomplete() {
        return (model, autocompleteDto) -> {
            autocompleteDto.setId(model.getId());
            autocompleteDto.setValue(model.getProduct().getTitle() + " " + model.getDate().toLocalDate());
        };
    }

    public Mapper<History, OrderHistoryDto> historyToOrderHistoryDto() {
        return (history, orderHistoryDto) -> {
            orderHistoryDto.setId(history.getId());
            orderHistoryDto.setDateChangeStatus(history.getDateChangeStatus().toString());
            orderHistoryDto.setDescChangeStatus(history.getDescChangeStatus());
            orderHistoryDto.setOldStatus(history.getNewStatus().getName());
        };
    }
}
