package com.csipon.crm.domain;

import java.time.LocalDateTime;

/**
 * Created by Pasha on 22.04.2017.
 */
public class UserAttempts {

    private Integer id;

    private String userMail;

    private int attempts;

    private LocalDateTime lastModified;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "UserAttempts{" +
                "id=" + id +
                ", userMail='" + userMail + '\'' +
                ", attempts=" + attempts +
                ", lastModified=" + lastModified +
                '}';
    }
}
