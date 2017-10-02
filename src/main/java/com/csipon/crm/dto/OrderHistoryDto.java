package com.csipon.crm.dto;

/**
 * Created by Pasha on 13.05.2017.
 */
public class OrderHistoryDto {
    private Long id;
    private String oldStatus;
    private String dateChangeStatus;
    private String descChangeStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getDateChangeStatus() {
        return dateChangeStatus;
    }

    public void setDateChangeStatus(String dateChangeStatus) {
        this.dateChangeStatus = dateChangeStatus;
    }

    public String getDescChangeStatus() {
        return descChangeStatus;
    }

    public void setDescChangeStatus(String descChangeStatus) {
        this.descChangeStatus = descChangeStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderHistoryDto that = (OrderHistoryDto) o;

        return id != null && that.getId() != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (oldStatus != null ? oldStatus.hashCode() : 0);
        result = 31 * result + (dateChangeStatus != null ? dateChangeStatus.hashCode() : 0);
        result = 31 * result + (descChangeStatus != null ? descChangeStatus.hashCode() : 0);
        return result;
    }
}
