package com.csipon.crm.service.entity.impl;

import com.csipon.crm.dao.*;
import com.csipon.crm.domain.model.*;
import com.csipon.crm.domain.real.RealOrder;
import com.csipon.crm.domain.request.OrderRowRequest;
import com.csipon.crm.dto.*;
import com.csipon.crm.dto.mapper.Mapper;
import com.csipon.crm.dto.mapper.ModelMapper;
import com.csipon.crm.dto.mapper.impl.OrderMapper;
import com.csipon.crm.dto.row.OrderRowDto;
import com.csipon.crm.pdf.PDFGenerator;
import com.csipon.crm.scheduler.cacher.impl.OrderCache;
import com.csipon.crm.service.entity.OrderLifecycleService;
import com.csipon.crm.service.entity.OrderService;
import com.itextpdf.text.DocumentException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 02.05.2017
 */

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderDao orderDao;
    private final HistoryDao historyDao;
    private final OrderLifecycleService lifecycleService;
    private final OrderCache orderCache;
    private final OrderMapper orderMapper;
    private final PDFGenerator pdfGenerator;
    private final ProductDao productDao;
    private final DiscountDao discountDao;
    private final UserDao userDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, HistoryDao historyDao, OrderLifecycleService lifecycleService,
                            OrderCache orderCache, OrderMapper orderMapper, PDFGenerator pdfGenerator, ProductDao productDao, DiscountDao discountDao, UserDao userDao) {
        this.orderDao = orderDao;
        this.historyDao = historyDao;
        this.lifecycleService = lifecycleService;
        this.orderCache = orderCache;
        this.orderMapper = orderMapper;
        this.pdfGenerator = pdfGenerator;
        this.productDao = productDao;
        this.discountDao = discountDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public Order create(OrderDto orderDto) {
        Order order = ModelMapper.map(orderMapper.dtoToModel(), orderDto, RealOrder.class);
        lifecycleService.createOrder(order);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderDao.findById(id);
    }

    @Transactional
    @Override
    public List<AutocompleteDto> getAutocompleteOrder(String pattern, User user) {
        List<Order> orders;
        if (user.isContactPerson()) {
            orders = orderDao.findOrgOrdersByIdOrTitle(pattern, user.getId());
        } else {
            orders = orderDao.findByIdOrTitleByCustomer(pattern, user.getId());
        }
        return ModelMapper.mapList(orderMapper.modelToAutocomplete(), orders, AutocompleteDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getOrdersRow(OrderRowRequest orderRowRequest) {
        Map<String, Object> response = new HashMap<>();
        Long length = orderDao.getOrderRowsCount(orderRowRequest);
        response.put("length", length);
        List<Order> orders = orderDao.findOrderRows(orderRowRequest);

        List<OrderRowDto> ordersDto = ModelMapper.mapList(orderMapper.modelToRowDto(), orders, OrderRowDto.class);
        response.put("rows", ordersDto);
        return response;
    }

    @Override
    public List<Order> findByCustomer(User customer) {
        return orderDao.findAllByCustomerId(customer.getId());
    }

    @Override
    public boolean hasCustomerProduct(Long productId, Long customerId) {
        return orderDao.hasCustomerProduct(productId, customerId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkAccessToOrder(User user, Long orderId) {
        UserRole role = user.getUserRole();
        if (role.equals(UserRole.ROLE_ADMIN) || role.equals(UserRole.ROLE_PMG) || role.equals(UserRole.ROLE_CSR)) {
            return true;
        } else if (role.equals(UserRole.ROLE_CUSTOMER)) {
            Long count = null;
            if (user.isContactPerson()) {
                count = orderDao.checkOwnershipOfContactPerson(orderId, user.getId());
            } else {
                count = orderDao.checkOwnershipOfCustomer(orderId, user.getId());
            }
            return count > 0;
        }
        return false;
    }

    @Override
    public List<OrderViewDto> getCsrActivateOrder(Long csrId) {
        if (csrId != null) {
            return convertMapToList(orderCache.getActivateElement(csrId));
        }
        return null;
    }


    @Override
    public List<OrderViewDto> getCsrPauseOrder(Long csrId) {
        if (csrId != null) {
            return convertMapToList(orderCache.getPauseElement(csrId));
        }
        return null;
    }

    @Override
    public List<OrderViewDto> getCsrResumeOrder(Long csrId) {
        if (csrId != null) {
            return convertMapToList(orderCache.getResumeElement(csrId));
        }
        return null;
    }

    @Override
    public List<OrderViewDto> getCsrDisableOrder(Long csrId) {
        if (csrId != null) {
            return convertMapToList(orderCache.getDisableElement(csrId));
        }
        return null;
    }

    @Override
    public Integer getCsrOrderCount(Long csrId) {
        if (csrId != null) {
            return orderCache.getCountElements(csrId);
        }
        return 0;
    }

    private List<OrderViewDto> convertMapToList(Map<Long, Order> map) {
        List<OrderViewDto> orders = new ArrayList<>();
        Mapper<Order, OrderViewDto> mapper = orderMapper.modelToOrderViewDto();
        for (Map.Entry<Long, Order> m : map.entrySet()) {
            orders.add(ModelMapper.map(mapper, m.getValue(), OrderViewDto.class));
        }
        return orders;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<OrderHistoryDto> getOrderHistory(Long id) {
        List<History> histories = historyDao.findAllByOrderId(id);
        Set<OrderHistoryDto> orders = new TreeSet<>(orderHistoryDtoComparator);
        orders.addAll(ModelMapper.mapList(orderMapper.historyToOrderHistoryDto(), histories, OrderHistoryDto.class));
        return orders;
    }

    private Comparator<OrderHistoryDto> orderHistoryDtoComparator = (o1, o2) -> o1.getId() > o2.getId() ? -1 : 1;

    @Override
    public GraphDto getStatisticalGraph(GraphDto graphDto) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(graphDto.getDatePattern());
        try {
            LocalDate fromDate = LocalDate.parse(graphDto.getFromDate(), dtf);
            LocalDate toDate = LocalDate.parse(graphDto.getToDate(), dtf);
            if (toDate.compareTo(fromDate) < 0) {
                throw new Exception("toDate is less then fromDate");
            }
            return historyDao.findOrderHistoryBetweenDateChangeByProductIds(fromDate, toDate, graphDto);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    @Transactional(readOnly = true)
    public void getPdfReport(Long orderId, HttpServletResponse response) {
        Order order = orderDao.findById(orderId);
        User customer = userDao.findById(order.getCustomer().getId());
        Product product = productDao.findById(order.getProduct().getId());
        try {
            Discount discount = null;
            if (product.getDiscount() != null) {
                discount = discountDao.findById(product.getDiscount().getId());
            }
            byte[] bytePdf = pdfGenerator.generate(order, customer, product, discount);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + orderId + "-order-report.pdf");
            response.getOutputStream().write(bytePdf);
            response.getOutputStream().close();
        } catch (DocumentException | IOException | MessagingException e) {
            log.error("Error when getting PDF report.", e);
        }
    }

}
