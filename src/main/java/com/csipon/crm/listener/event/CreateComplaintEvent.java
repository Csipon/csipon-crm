package com.csipon.crm.listener.event;

import com.csipon.crm.domain.model.Complaint;
import org.springframework.context.ApplicationEvent;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 16.05.2017
 */
public class CreateComplaintEvent extends ApplicationEvent {

    private Complaint complaint;

    public CreateComplaintEvent(Object source, Complaint complaint) {
        super(source);
        this.complaint = complaint;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }
}
