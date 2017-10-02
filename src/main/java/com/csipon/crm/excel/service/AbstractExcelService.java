package com.csipon.crm.excel.service;

import com.csipon.crm.excel.builder.ExcelBuilder;
import com.csipon.crm.excel.ExcelMapKey;
import com.csipon.crm.excel.drawer.ExcelDrawer;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.time.temporal.ChronoUnit.*;

/**
 * Created by Pasha on 26.05.2017.
 */
public abstract class AbstractExcelService<T> {
    private final ExcelDrawer excelDrawer;

    public abstract Map<ExcelMapKey, List<?>> convertToMap(List<T> objects);

    public abstract Map<LocalDate, Map<String, Integer>> prepareDataChart(LocalDate[] range, List<T> objects);

    public abstract String[] getTitles();

    public AbstractExcelService(ExcelDrawer excelDrawer) {
        this.excelDrawer = excelDrawer;
    }

    public Workbook parseData(List<T> objects, LocalDate[] range) {
        Map<ExcelMapKey, List<?>> map = convertToMap(objects);
        ExcelBuilder excelBuilder = new ExcelBuilder();
        excelBuilder.createSheet("Main");
        setTitleRow(excelBuilder, 0);
        excelBuilder.autosizeColumn(map.size() + 1);
        for (int i = 1; i <= objects.size(); i++) {
            rowSetter(excelBuilder, i, map);
            excelBuilder.autosizeColumn(map.size() + 1);
        }
        drawChart(excelBuilder, objects, range);
        return excelBuilder.getWorkbook();
    }


    private void drawChart(ExcelBuilder excelBuilder, List<T> objects, LocalDate[] range) {
        Map<LocalDate, Map<String, Integer>> map = prepareDataChart(range, objects);
        excelBuilder.createSheet("Chart");
        setDateTitle(excelBuilder, map.keySet(), 30);
        setChartData(map.values(), excelBuilder, 31);
        excelDrawer.drawChart(excelBuilder.getSheet(), 1, 1, 15, 25, map);
    }

    private void setDateTitle(ExcelBuilder excelBuilder, Set<LocalDate> dates, int rowNumber) {
        excelBuilder.createRow(rowNumber);
        int cellNumber = 1;
        excelBuilder.createCell(cellNumber++).setCellValue("").setCellTitleStyle();
        for (LocalDate date : dates) {
            excelBuilder.createCell(cellNumber++).setCellValue(date.toString()).setCellTitleStyle();
        }
        excelBuilder.autosizeColumn(dates.size() + 2);
    }

    private void setChartData(Collection<Map<String, Integer>> productValue, ExcelBuilder excelBuilder, int rowNumber) {
        boolean first = false;
        int rowCounter = rowNumber;
        int cellCounter = 1;
        for (Map<String, Integer> aProductValue : productValue) {
            for (Map.Entry<String, Integer> m : aProductValue.entrySet()) {
                if (!first) {
                    excelBuilder.createRow(rowCounter++);
                    excelBuilder.createCell(cellCounter++).setCellValue(m.getKey()).setCellTitleStyle();
                    excelBuilder.createCell(cellCounter).setCellValue(m.getValue()).setCellValueStyle();
                    cellCounter = 1;
                } else {
                    excelBuilder.getRow(rowCounter++).createCell(cellCounter).setCellValue(m.getValue()).setCellValueStyle();
                }
                excelBuilder.autosizeColumn(cellCounter);
            }
            if (!first) {
                cellCounter = 3;
            } else {
                cellCounter++;
            }
            first = true;
            rowCounter = rowNumber;
        }
    }

    private void setTitleRow(ExcelBuilder excelBuilder, int rowNumber) {
        excelBuilder.createRow(rowNumber);
        int cellNumber = 0;
        for (String title : getTitles()) {
            excelBuilder.createCell(cellNumber++).setCellValue(title).setCellTitleStyle();
        }
    }

    private void rowSetter(ExcelBuilder excelBuilder, int rowNumber, Map<ExcelMapKey, List<?>> map) {
        excelBuilder.createRow(rowNumber);
        cellSetter(excelBuilder, map, rowNumber - 1);
    }

    private void cellSetter(ExcelBuilder excelBuilder, Map<ExcelMapKey, List<?>> map, int index) {
        int cellNumber = 0;
        excelBuilder.createCell(cellNumber++).setCellValue((long) index + 1).setCellValueStyle();
        for (Map.Entry<ExcelMapKey, List<?>> m : map.entrySet()) {
            excelBuilder.createCell(cellNumber++).setCellValue(getListValue(m.getValue(), index)).setCellValueStyle();
        }
    }

    public LocalDate convertString(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dtf);
    }

    public LocalDate[] calculateRange(LocalDate from, LocalDate to) {
        List<LocalDate> result = new ArrayList<>();
        ChronoUnit unit = getChronoUnit(from, to);
        result.add(LocalDate.from(from));
        do {
            if (unit == YEARS) {
                from = from.plusYears(1);
                result.add(LocalDate.from(from));
            } else if (unit == MONTHS) {
                from = from.plusMonths(1);
                result.add(LocalDate.from(from));
            } else {
                from = from.plusDays(1);
                result.add(LocalDate.from(from));
            }
        } while (from.isBefore(to));
        result.add(to);
        return result.toArray(new LocalDate[result.size()]);
    }

    private ChronoUnit getChronoUnit(LocalDate from, LocalDate to) {
        if (from.plusYears(1).isBefore(to)) {
            return YEARS;
        } else if (from.plusMonths(1).isBefore(to)) {
            return MONTHS;
        } else {
            return DAYS;
        }
    }

    private Object getListValue(List<?> list, int index) {
        return list.get(index);
    }

    public Long[] checkId(Long[] id) {
        if (id == null || id.length == 0) {
            id = new Long[]{0L};
        }
        return id;
    }
}
