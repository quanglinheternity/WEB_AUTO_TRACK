package com.transport.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BaseExportMap {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<Map<String, Object>> listData;
    private CellStyle headerStyle;
    private CellStyle dataStyle;
    private CellStyle dateStyle;
    private final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public BaseExportMap(List<Map<String, Object>> listData) {
        this.listData = listData;
        this.workbook = new XSSFWorkbook();
        initStyles();
    }

    private void initStyles() {
        // Header Style
        headerStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setFontName("Times New Roman");
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setWrapText(true);

        // Data Style
        dataStyle = workbook.createCellStyle();
        XSSFFont dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 12);
        dataFont.setFontName("Times New Roman");
        dataStyle.setFont(dataFont);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setWrapText(true);

        // Date Style
        dateStyle = workbook.createCellStyle();
        dateStyle.cloneStyleFrom(dataStyle);
        DataFormat format = workbook.createDataFormat();
        dateStyle.setDataFormat(format.getFormat("dd/mm/yyyy hh:mm:ss"));
    }

    public BaseExportMap writeHeaderLine(String[] headers, String sheetName) {
        sheet = workbook.createSheet(sheetName != null ? sheetName : "Data Export");
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        return this;
    }

    public BaseExportMap writeDataLines(String[] fieldKeys) {
        int rowNum = 1;

        for (Map<String, Object> rowData : listData) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;

            for (String key : fieldKeys) {
                Object value = rowData.get(key);
                Cell cell = row.createCell(colNum++);
                setCellValue(cell, value);
            }
        }

        // Auto-size columns
        for (int i = 0; i < fieldKeys.length; i++) {
            sheet.autoSizeColumn(i);
            int width = sheet.getColumnWidth(i);
            if (width < 3000) sheet.setColumnWidth(i, 4000);
            if (width > 15000) sheet.setColumnWidth(i, 15000);
        }

        return this;
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            cell.setCellStyle(dataStyle);
            return;
        }

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateStyle);
        } else {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(dataStyle);
    }

    // Export file .xlsx (tải về trực tiếp)
    public void export(HttpServletResponse response, String baseFileName) throws IOException {
        String timestamp = fileDateFormat.format(new Date());
        String fileName = baseFileName + "_" + timestamp + ".xlsx";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");

        String encodedFileName = "attachment; filename*=UTF-8''"
                + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", encodedFileName);

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();
    }

    // Export Base64
    public String exportToBase64() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
