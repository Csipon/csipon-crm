package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.dao.HistoryDao;
import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.domain.model.*;
import com.csipon.crm.domain.real.RealHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Pasha on 16.05.2017.
 */
@Service
public class HistorySetter extends AbstractSetter<History> {

    private List<Order> orders;
    private List<Complaint> complaints;
    private List<Product> products;

    private final HistoryDao historyDao;

    @Autowired
    public HistorySetter(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    @Override
    public List<History> generate(int numbers) {
        generateOrderHistory();
        generateComplaintsHistory();
        generateProductHistory();
        return null;
    }

    @Override
    public History generateObject() {
        return new RealHistory();
    }


    private void generateOrderHistory() {
        for (Order order : orders) {
            orderCycle(order);
        }
    }

    private void generateComplaintsHistory() {
        for (Complaint complaint : complaints) {
            complaintCycle(complaint);
        }
    }

    private void generateProductHistory() {
        for (Product product : products) {
            productCycle(product);
        }
    }

    private void orderCycle(Order order) {
        int days = LocalDateTime.now().getDayOfYear() - order.getDate().getDayOfYear();
        LocalDateTime buffer = order.getDate();
        for (int i = 0; i < 3; i++) {
            OrderStatus orderStatus = OrderStatus.values()[i];
            History history = generateObject();
            history.setOrder(order);
            history.setNewStatus(orderStatus);
            setOrderDescHistory(history);
            history.setDateChangeStatus(buffer);
            historyDao.create(history);
            if (order.getStatus() == orderStatus) {
                break;
            }
            days = getRand(days);
            buffer = LocalDateTime.now().minusDays(days);
        }
        if (order.getStatus() == OrderStatus.PAUSED || order.getStatus() == OrderStatus.DISABLED) {
            endCycle(order, days, buffer);
        } else if (order.getStatus() == OrderStatus.ACTIVE && Math.random() > 0.7) {
            endActiveAllCycle(order, days);
        } else if (order.getStatus() == OrderStatus.REQUEST_TO_RESUME) {
            endRequestPauseCycle(order, buffer);
            days = getRand(days);
            endPauseCycle(order, LocalDateTime.now().minusDays(days));
            days = getRand(days);
            endResumeCycle(order, LocalDateTime.now().minusDays(days));
        } else if (order.getStatus() == OrderStatus.REQUEST_TO_PAUSE) {
            endRequestPauseCycle(order, buffer);
        } else if (order.getStatus() == OrderStatus.REQUEST_TO_DISABLE) {
            endRequestDisableCycle(order, buffer);
        }
    }


    private void endActiveAllCycle(Order order, int days) {
        days = getRand(days);
        endRequestPauseCycle(order, LocalDateTime.now().minusDays(days));
        days = getRand(days);
        endPauseCycle(order, LocalDateTime.now().minusDays(days));
        days = getRand(days);
        endResumeCycle(order, LocalDateTime.now().minusDays(days));
        days = getRand(days);
        endActivateCycle(order, LocalDateTime.now().minusDays(days));
    }

    private void endResumeCycle(Order order, LocalDateTime buffer) {
        History history = generateObject();
        history.setOrder(order);
        history.setNewStatus(OrderStatus.REQUEST_TO_RESUME);
        setOrderDescHistory(history);
        history.setDateChangeStatus(buffer);
        historyDao.create(history);
    }

    private void endActivateCycle(Order order, LocalDateTime buffer) {
        History history = generateObject();
        history.setOrder(order);
        history.setNewStatus(OrderStatus.ACTIVE);
        setOrderDescHistory(history, OrderStatus.REQUEST_TO_RESUME);
        history.setDateChangeStatus(buffer);
        historyDao.create(history);
    }

    private void endPauseCycle(Order order, LocalDateTime buffer) {
        History history = generateObject();
        history.setOrder(order);
        history.setNewStatus(OrderStatus.PAUSED);
        setOrderDescHistory(history);
        history.setDateChangeStatus(buffer);
        historyDao.create(history);
    }

    private void endRequestPauseCycle(Order order, LocalDateTime buffer) {
        History history = generateObject();
        history.setOrder(order);
        history.setNewStatus(OrderStatus.REQUEST_TO_PAUSE);
        setOrderDescHistory(history);
        history.setDateChangeStatus(buffer);
        historyDao.create(history);
    }

    private void endRequestDisableCycle(Order order, LocalDateTime buffer) {
        History history = generateObject();
        history.setOrder(order);
        history.setNewStatus(OrderStatus.REQUEST_TO_DISABLE);
        setOrderDescHistory(history);
        history.setDateChangeStatus(buffer);
        historyDao.create(history);
    }

    private void endCycle(Order order, int days, LocalDateTime buffer) {
        History history = generateObject();
        history.setOrder(order);
        setEndCycleStatus(order, history);
        setOrderDescHistory(history);
        history.setDateChangeStatus(buffer);
        historyDao.create(history);
        days = getRand(days);
        buffer = LocalDateTime.now().minusDays(days);
        history = generateObject();
        history.setOrder(order);
        history.setNewStatus(order.getStatus());
        setOrderDescHistory(history);
        history.setDateChangeStatus(buffer);
        historyDao.create(history);
    }

    private void setEndCycleStatus(Order order, History history) {
        if (order.getStatus() == OrderStatus.PAUSED) {
            history.setNewStatus(OrderStatus.REQUEST_TO_PAUSE);
        } else if (order.getStatus() == OrderStatus.DISABLED) {
            history.setNewStatus(OrderStatus.REQUEST_TO_DISABLE);
        }
    }

    private void complaintCycle(Complaint complaint) {
        int days = LocalDateTime.now().getDayOfYear() - complaint.getDate().getDayOfYear();
        LocalDateTime buffer = complaint.getDate();
        for (ComplaintStatus complaintStatus : ComplaintStatus.values()) {
            History history = generateObject();
            history.setComplaint(complaint);
            history.setNewStatus(complaintStatus);
            setComplaintDescHistory(history);
            history.setDateChangeStatus(buffer);
            historyDao.create(history);
            if (complaint.getStatus() == complaintStatus) {
                break;
            }
            days = getRand(days);
            buffer = LocalDateTime.now().minusDays(days);
        }
    }

    private void productCycle(Product product) {
        int days = getRand(365);
        LocalDateTime buffer = LocalDateTime.now().minusDays(days);
        for (ProductStatus productStatus : ProductStatus.values()) {
            History history = generateObject();
            history.setProduct(product);
            history.setNewStatus(productStatus);
            setProductDescHistory(history);
            history.setDateChangeStatus(buffer);
            historyDao.create(history);
            if (product.getStatus() == productStatus) {
                break;
            }
            days = getRand(days);
            buffer = LocalDateTime.now().minusDays(days);
        }
    }

    private int getRand(int index) {
        if (index <= 0) {
            return 0;
        }
        while (true) {
            int rand = random.nextInt(index);
            if (rand >= 0) {
                return rand;
            }
        }
    }


    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    private void setOrderDescHistory(History history) {
        setOrderDescHistory(history, null);
    }

    private void setOrderDescHistory(History history, OrderStatus oldStatus) {
        switch ((OrderStatus) history.getNewStatus()) {
            case NEW:
                history.setDescChangeStatus(getNewDesc());
                break;
            case PROCESSING:
                history.setDescChangeStatus(getProcessingDesc(history.getOrder().getCsr().getId()));
                break;
            case ACTIVE:
                if (oldStatus != null && oldStatus == OrderStatus.REQUEST_TO_RESUME) {
                    history.setDescChangeStatus(getResumeDesc(history.getOrder().getCsr().getId()));
                } else {
                    history.setDescChangeStatus(getActivateDesc(history.getOrder().getCsr().getId()));
                }
                break;
            case REQUEST_TO_PAUSE:
                history.setDescChangeStatus(getRequestPauseDesc());
                break;
            case REQUEST_TO_RESUME:
                history.setDescChangeStatus(getRequestResumeDesc());
                break;
            case REQUEST_TO_DISABLE:
                history.setDescChangeStatus(getRequestDisableDesc());
                break;
            case PAUSED:
                history.setDescChangeStatus(getPausedDesc(history.getOrder().getCsr().getId()));
                break;
            case DISABLED:
                history.setDescChangeStatus(getDisabledDesc(history.getOrder().getCsr().getId()));
                break;
        }
    }

    private void setComplaintDescHistory(History history) {
        switch ((ComplaintStatus) history.getNewStatus()) {
            case OPEN:
                history.setDescChangeStatus(getComplOpenDesc());
                break;
            case SOLVING:
                history.setDescChangeStatus(getComplSolvingDesc());
                break;
            case CLOSED:
                history.setDescChangeStatus(getComplClosedDesc());
                break;
        }
    }

    private void setProductDescHistory(History history) {
        switch ((ProductStatus) history.getNewStatus()) {
            case PLANNED:
                history.setDescChangeStatus(getProductPlanedDesc());
                break;
            case ACTUAL:
                history.setDescChangeStatus(getProductActualDesc());
                break;
            case OUTDATED:
                history.setDescChangeStatus(getProductOutdatedDesc());
                break;
        }
    }

    private String getProductPlanedDesc(){
        return "Product is successful create";
    }

    private String getProductActualDesc(){
        return "Product is successful move in actual status";
    }

    private String getProductOutdatedDesc(){
        return "Product already not actual after then product be outdated";
    }

    private String getComplOpenDesc() {
        return "Complaint is successful open";
    }

    private String getComplSolvingDesc() {
        return "Complaint have solving condition";
    }

    private String getComplClosedDesc() {
        return "Complaint is successful solved and closed";
    }

    private String getNewDesc() {
        return "Order successful create";
    }

    private String getProcessingDesc(Long csrId) {
        return "Csr with id : " + csrId + " is successful accepted this order";
    }

    private String getActivateDesc(Long csrId) {
        return "Csr with id : " + csrId + " after successfully done work for connect service, csr   activated this order";
    }

    private String getRequestPauseDesc() {
        return "Request to pause is successful send";
    }

    private String getRequestResumeDesc() {
        return "Request to resume is successful send";
    }

    private String getRequestDisableDesc() {
        return "Request to pause is successful send";
    }

    private String getPausedDesc(Long csrId) {
        return "Csr with id : " + csrId + " is successful paused this order";
    }

    private String getResumeDesc(Long csrId) {
        return "Csr with id : " + csrId + " is successful resume this order";
    }

    private String getDisabledDesc(Long csrId) {
        return "Csr with id : " + csrId + " is successful disabled this order";
    }
}
