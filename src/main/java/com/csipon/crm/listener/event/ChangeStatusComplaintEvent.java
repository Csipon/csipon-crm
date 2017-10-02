package com.csipon.crm.listener.event;

import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.domain.model.ComplaintStatus;
import org.springframework.context.ApplicationEvent;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 13.05.2017
 */
public class ChangeStatusComplaintEvent extends ApplicationEvent {

    private Complaint complaint;
    private boolean isDone;
    private ComplaintStatus changeToStatus;

    public ChangeStatusComplaintEvent(Object source, Complaint complaint, ComplaintStatus changeToStatus) {
        super(source);
        this.complaint = complaint;
        this.changeToStatus = changeToStatus;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public ComplaintStatus getChangeToStatus() {
        return changeToStatus;
    }

    public void setChangeToStatus(ComplaintStatus changeToStatus) {
        this.changeToStatus = changeToStatus;
    }
}
