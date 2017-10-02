package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.dao.ComplaintDao;
import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.domain.model.ComplaintStatus;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.real.RealComplaint;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pasha on 05.05.2017.
 */
@Service
public class ComplaintSetter extends AbstractSetter<Complaint> {

    private List<Order> orders;
    private List<User> pmgs;
    private List<String> titles = new ArrayList<>();
    private List<String> descs = new ArrayList<>();


    @Autowired
    private ComplaintDao complaintDao;
    @Value(value = "classpath:testdata/complaint.json")
    private Resource resource;


    @Override
    public List<Complaint> generate(int numbers) {
        fillComplaint();
        List<Complaint> complaints = new ArrayList<>();

        for (int i = 0; i < numbers; i++) {
            Complaint complaint = generateObject();
            complaintDao.create(complaint);
            complaints.add(complaint);
        }
        return complaints;
    }

    private void fillComplaint() {
        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(resource.getInputStream());
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for (Object o : a) {
            JSONObject person = (JSONObject) o;
            String title = (String) person.get("title");
            String desc = (String) person.get("description");
            titles.add(title);
            descs.add(desc);
        }
    }

    @Override
    public Complaint generateObject() {
        Complaint complaint = new RealComplaint();
        setUserOrderDate(complaint);
        complaint.setTitle(titles.get(random.nextInt(titles.size())));
        complaint.setMessage(descs.get(random.nextInt(descs.size())));
        ComplaintStatus status = getStatus();
        complaint.setStatus(status);
        if (status != ComplaintStatus.OPEN) {
            complaint.setPmg(getPmg());
        }
        return complaint;
    }


    private ComplaintStatus getStatus() {
        return ComplaintStatus.values()[random.nextInt(ComplaintStatus.values().length)];
    }

    private User getPmg() {
        return pmgs.get(random.nextInt(pmgs.size()));
    }

    private void setUserOrderDate(Complaint complaint) {
        Order order = orders.get(random.nextInt(orders.size()));
        LocalDateTime date = order.getDate();
        date.plusDays(random.nextInt(10));
        complaint.setOrder(order);
        complaint.setCustomer(order.getCustomer());
        complaint.setDate(date);
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setPmgs(List<User> pmgs) {
        this.pmgs = pmgs;
    }
}
