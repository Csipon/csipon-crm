package com.csipon.crm.excel.drawer;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.charts.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pasha on 22.05.2017.
 */
@Component
public class ExcelDrawer {

    public void drawChart(Sheet sheet, int left, int top, int right, int bottom, Map<LocalDate, Map<String, Integer>> mapData) {
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, left, top, right, bottom);
        Chart chart = drawing.createChart(anchor);
        ChartLegend legend = chart.getOrCreateLegend();
        legend.setPosition(LegendPosition.BOTTOM);
        LineChartData data = chart.getChartDataFactory().createLineChartData();
        ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
        ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        Set<LocalDate> dateRange = mapData.keySet();
        ChartDataSource<LocalDate> x = DataSources.fromArray(dateRange.toArray(new LocalDate[dateRange.size()]));
        int i = 1;
        Set<String> productTitles = mapData.values().iterator().next().keySet();
        for (String title : productTitles) {
            ChartDataSource<Number> y = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(bottom + i + 5, bottom + i + 5, 2, dateRange.size() + 1));
            data.addSeries(x, y).setTitle(title);
            i++;
        }
        chart.plot(data, bottomAxis, leftAxis);
    }
}
