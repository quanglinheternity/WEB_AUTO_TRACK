package com.transport.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.val;

public class BaseExport<T> {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<T> listData;
    private CellStyle headerStyle;
    private CellStyle dataStyle;
    private CellStyle dateStyle;
    private final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public BaseExport(List<T> listData) {
        this.listData = listData;
        this.workbook = new XSSFWorkbook();
        initStyles();
    }

    private void initStyles() {
        Objects.requireNonNull(workbook, "Workbook must not be null");
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
        dateStyle.setDataFormat(format.getFormat("dd/mm/yyyy"));
    }

    public BaseExport<T> writeHeaderLine(String[] headers, String sheetName) {
        sheet = workbook.createSheet(sheetName != null ? sheetName : "Data Export");
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }

        return this;
    }

    public BaseExport<T> writeDataLines(String[] fieldNames, Class<T> clazz) {
        int rowNum = 1;

        for (val data : listData) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;

            for (String fieldName : fieldNames) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(data);

                    Cell cell = row.createCell(colNum);
                    setCellValue(cell, value);
                    colNum++;
                } catch (Exception e) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue("ERROR");
                    cell.setCellStyle(dataStyle);
                    e.printStackTrace();
                }
            }
        }

        for (int i = 0; i < fieldNames.length; i++) {
            sheet.autoSizeColumn(i);
            if (sheet.getColumnWidth(i) < 3000) {
                sheet.setColumnWidth(i, 4000);
            }
            if (sheet.getColumnWidth(i) > 15000) {
                sheet.setColumnWidth(i, 15000);
            }
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
            cell.setCellStyle(dataStyle);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
            cell.setCellStyle(dataStyle);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
            cell.setCellStyle(dataStyle);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellStyle(dataStyle);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
            cell.setCellStyle(dataStyle);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateStyle);
        } else {
            cell.setCellValue(value.toString());
            cell.setCellStyle(dataStyle);
        }
    }

    // Export file .xlsx (tải về trực tiếp)
    public void export(HttpServletResponse response, String baseFileName) throws IOException {
        // Tạo tên file có thời gian
        String timestamp = fileDateFormat.format(new Date());
        String fileName = baseFileName + "_" + timestamp + ".xlsx";

        // Thiết lập header
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");

        String encodedFileName = "attachment; filename*=UTF-8''"
                + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", encodedFileName);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    // Export Base64 string
    public void exportToBase64(HttpServletResponse response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        String base64 = Base64.getEncoder().encodeToString(byteArray);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(base64.getBytes());
        outputStream.close();
    }
}
