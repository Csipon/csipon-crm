package com.csipon.crm.excel.builder;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

/**
 * Created by Pasha on 22.05.2017.
 */
public class ExcelBuilder {
    private Workbook workbook;
    private Sheet sheet;
    private Row currentRow;
    private Cell currentCell;

    private CellStyle titleCellStyle;
    private CellStyle valueCellStyle;

    public ExcelBuilder() {
        this.workbook = new XSSFWorkbook();
        setTitleStyle();
        setValueStyle();
    }

    public ExcelBuilder createSheet(){
        sheet = workbook.createSheet();
        return this;
    }

    public ExcelBuilder createSheet(String sheetName){
        sheet = workbook.createSheet(sheetName);
        return this;
    }

    public ExcelBuilder getRow(int numberRow){
        currentRow = sheet.getRow(numberRow);
        return this;
    }

    public ExcelBuilder createRow(int numberRow){
        currentRow = sheet.createRow(numberRow);
        return this;
    }

    public ExcelBuilder createCell(int numberCell){
        currentCell = currentRow.createCell(numberCell);
        return this;
    }
    public ExcelBuilder setCellTitleStyle(){
        currentCell.setCellStyle(titleCellStyle);
        return this;
    }

    public ExcelBuilder setCellValueStyle(){
        currentCell.setCellStyle(valueCellStyle);
        return this;
    }

    private void setTitleStyle(){
        titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        titleCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        titleCellStyle.setBorderRight(BorderStyle.MEDIUM);
        titleCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        titleCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    private void setValueStyle(){
        valueCellStyle = workbook.createCellStyle();
        valueCellStyle.setBorderBottom(BorderStyle.THIN);
        valueCellStyle.setBorderLeft(BorderStyle.THIN);
        valueCellStyle.setBorderRight(BorderStyle.THIN);
        valueCellStyle.setBorderTop(BorderStyle.THIN);
        valueCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        valueCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public ExcelBuilder setCellValue(Object o){
        if (o instanceof Double){
            currentCell.setCellValue((Double) o);
        }else if (o instanceof Long) {
            currentCell.setCellValue((long) o);
        }else if (o instanceof Integer){
            currentCell.setCellValue((int)o);
        }else if (o instanceof Boolean){
            currentCell.setCellValue((Boolean) o);
        }else if (o instanceof Date){
            currentCell.setCellValue((Date) o);
        }else if (o != null){
            currentCell.setCellValue(o.toString());
        }else {
            currentCell.setCellValue("empty");
        }
        return this;
    }

    public void autosizeColumn(int numberColumns){
        for (int i = 0; i < numberColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public Sheet getSheet() {
        return sheet;
    }

    public Workbook getWorkbook(){
        return workbook;
    }
}
