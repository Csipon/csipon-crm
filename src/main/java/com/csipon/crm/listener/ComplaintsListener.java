package com.csipon.crm.listener;

import com.csipon.crm.dao.ComplaintDao;
import com.csipon.crm.dao.HistoryDao;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.listener.event.ChangeStatusComplaintEvent;
import com.csipon.crm.service.email.AbstractEmailSender;
import com.csipon.crm.service.email.EmailType;
import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.real.RealHistory;
import com.csipon.crm.listener.event.CreateComplaintEvent;
import com.csipon.crm.service.email.EmailParam;
import com.csipon.crm.service.email.EmailParamKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 13.05.2017
 */

@Component
public class ComplaintsListener {
    private static final Logger log = LoggerFactory.getLogger(ComplaintsListener.class);
    private HistoryDao historyDao;
    private AbstractEmailSender emailSender;
    private ComplaintDao complaintDao;

    @Autowired
    public ComplaintsListener(HistoryDao historyDao, ComplaintDao complaintDao,
                              @Qualifier("complaintSender") AbstractEmailSender emailSender) {
        this.historyDao = historyDao;
        this.complaintDao = complaintDao;
        this.emailSender = emailSender;
    }

    @EventListener
    public void createComplaint(CreateComplaintEvent event) {
        Complaint complaint = event.getComplaint();
        History history = generateHistory(complaint);
        String role = getRole(complaint.getCustomer());
        history.setDescChangeStatus(role + " with id " +
                complaint.getCustomer().getId() + " created complaint");
        historyDao.create(history);
        sendMail(complaint);
    }

    @EventListener(condition = "(#event.complaint.status.name.equals('OPEN') && #event.changeToStatus.name.equals('SOLVING')) || " +
            "(#event.complaint.status.name.equals('SOLVING') && #event.changeToStatus.name.equals('CLOSED')) ")
    public void acceptComplaint(ChangeStatusComplaintEvent event) {
        Complaint complaint = event.getComplaint();
        complaint.setStatus(event.getChangeToStatus());
        History history = generateHistory(complaint);
        String role = getRole(complaint.getPmg());
        history.setDescChangeStatus("Status was changed by " + role + " with id " +
                complaint.getPmg().getId());
        if(saveStatusAndHistory(complaint, history)){
            event.setDone(true);
        }
    }

    private void sendMail(Complaint complaint) {
        EmailParam emailMap = new EmailParam(EmailType.COMPLAINT);
        emailMap.put(EmailParamKeys.COMPLAINT, complaint);
        try {
            emailSender.send(emailMap);
        } catch (MessagingException e) {
            log.error("Letter hadn't been sent", e);
        }
    }

    private String getRole(User user) {
        String role = user.getUserRole().getName();
        return role.substring(role.indexOf("_") + 1);
    }

    private History generateHistory(Complaint complaint) {
        History history = new RealHistory();
        history.setDateChangeStatus(LocalDateTime.now());
        history.setNewStatus(complaint.getStatus());
        history.setComplaint(complaint);
        return history;
    }

    private boolean saveStatusAndHistory(Complaint complaint, History history) {
        boolean result = false;
        if(complaintDao.update(complaint)>0 && historyDao.create(history)>0) {
            sendMail(complaint);
            result = true;
        }
        return result;
    }
}
