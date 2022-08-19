package com.example.exportreport.exportreport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

	public <T> void writeToExcelInMultiSheets(final String fileName, final List<DataPojo> data) {
		File file = null;
		OutputStream fos = null;
		XSSFWorkbook workbook = null;
		try {
			file = new File(fileName);
			workbook = getXSSFWorkbook(workbook, "Statement_mada", data, true);
			workbook = getXSSFWorkbook(workbook, "details_mada", data, false);
			fos = new FileOutputStream(file);
			workbook.write(fos);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T> XSSFWorkbook getXSSFWorkbook(XSSFWorkbook workbook, String sheetName, final List<DataPojo> data,
			boolean mainSheet) {
		try {
			if (workbook == null)
				workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet(sheetName);

			// Create header CellStyle
			Font headerFont = workbook.createFont();
			headerFont.setColor(IndexedColors.WHITE.index);
			CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
			// fill foreground color ...
			headerCellStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.index);

			// and solid fill pattern produces solid grey cell fill
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerCellStyle.setFont(headerFont);

			List<String> fieldNames = mainSheet ? getFieldNamesForClass(data.get(0).getClass())
					: getFieldNamesForClass(data.get(0).getDetails().getClass());
			int rowCount = 0;
			int columnCount = 0;

			Row row1 = sheet.createRow(rowCount++);
			Row row2 = sheet.createRow(rowCount++);
			Cell labelCell = row2.createCell(1);
			labelCell.setCellValue(sheetName);

			Row row3 = sheet.createRow(rowCount++);
			Row row4 = sheet.createRow(rowCount++);

			Row row = sheet.createRow(rowCount++);
			for (String fieldName : fieldNames) {
				Cell cell = row.createCell(columnCount++);
				if (rowCount == 5)
					cell.setCellStyle(headerCellStyle);
				cell.setCellValue(fieldName);
			}
			Class<? extends Object> classz = mainSheet ? data.get(0).getClass() : data.get(0).getDetails().getClass();
			for (DataPojo d : data) {
				T t = mainSheet ? (T) d : (T) d.getDetails();
				row = sheet.createRow(rowCount++);
				columnCount = 0;
				for (String fieldName : fieldNames) {
					Cell cell = row.createCell(columnCount);
					Method method = null;
					try {
						method = classz.getMethod("get" + capitalize(fieldName));
					} catch (NoSuchMethodException nme) {
						method = classz.getMethod("get" + fieldName);
					}
					Object value = method.invoke(t, (Object[]) null);
					if (value != null) {
						if (value instanceof String) {
							cell.setCellValue((String) value);
						} else if (value instanceof Long) {
							cell.setCellValue((Long) value);
						} else if (value instanceof Integer) {
							cell.setCellValue((Integer) value);
						} else if (value instanceof Double) {
							cell.setCellValue((Double) value);
						}
					}
					columnCount++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

	// retrieve field names from a POJO class
	private List<String> getFieldNamesForClass(Class<?> clazz) throws Exception {
		List<String> fieldNames = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if ("details".equals(fields[i].getName()))
				continue;
			fieldNames.add(fields[i].getName());
		}
		return fieldNames;
	}

	// capitalize the first letter of the field name for retriving value of the
	// field later
	private String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

}