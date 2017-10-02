package com.csipon.crm.domain;

import com.csipon.crm.domain.model.User;

import java.time.LocalDateTime;

/**
 * Created by Pasha on 02.05.2017.
 */
public class UserToken {

    private Long id;
    private String token;
    private LocalDateTime sendDate;
    private boolean used;
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
