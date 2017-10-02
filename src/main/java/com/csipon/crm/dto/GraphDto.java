package com.csipon.crm.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;

/**
 * @author Karpunets
 * @since 16.05.2017
 */
public class GraphDto {

    private List<Long> elementIds;
    private String fromDate;
    private String toDate;
    private List<String> labels;
    private List<List<Long>> series;

    private ChronoUnit dateType;

    public String getDatePattern(){
        switch (dateType) {
            case YEARS:
                fromDate = fromDate + "-01-01";
                toDate = toDate + "-01-01";
                break;
            case MONTHS:
                fromDate = fromDate + "-01";
                toDate = toDate + "-01";
                break;
            case DAYS:
                break;
        }
        return "yyyy-MM-dd";
    }

    public String getTypeDateChange(){
        switch (dateType) {
            case YEARS:
                return "year";
            case MONTHS:
                return "month";
            case DAYS:
                return "day";
        }
        return null;
    }

    public long getBetweenDates(LocalDate fromDate, LocalDate toDate) {
        switch (dateType) {
            case YEARS:
                return YEARS.between(fromDate, toDate);
            case MONTHS:
                return MONTHS.between(fromDate, toDate);
            case DAYS:
                return DAYS.between(fromDate, toDate);
        }
        return 0;
    }

    public List<Long> getElementIds() {
        return elementIds;
    }

    public void setElementIds(List<Long> elementIds) {
        this.elementIds = elementIds;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<List<Long>> getSeries() {
        return series;
    }

    public void setSeries(List<List<Long>> series) {
        this.series = series;
    }

    public ChronoUnit getDateType() {
        return dateType;
    }

    public void setDateType(ChronoUnit dateType) {
        this.dateType = dateType;
    }
}
