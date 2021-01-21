package com.yatra.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	/**
	 * initiateExcelConnection function to establish an initial connection with
	 * a work sheet
	 * 
	 * @param workSheet
	 *            (String)
	 * @param doFilePathMapping
	 *            (boolean)
	 * @param workBookName
	 *            (String)
	 * @return HSSFSheet (Work sheet)
	 * 
	 */
	public XSSFSheet initiateExcelConnection(String workSheet, String workBook) {
		XSSFSheet sheet = null;
		XSSFWorkbook wb = null;
		FileInputStream fis = null;
		try {
			new ReadProperties();
			String file = ReadProperties.readPropertyPath(workBook);
			fis = new FileInputStream(file);
			wb = new XSSFWorkbook(fis);
			sheet = wb.getSheet(workSheet);
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	public HSSFSheet initiateOpenExcelConnection(String workSheet, String workBook){
		HSSFSheet sheet = null;
		HSSFWorkbook wb = null;
		FileInputStream fis = null;
		try {
			new ReadProperties();
			String file = ReadProperties.readPropertyPath(workBook);
			fis = new FileInputStream(file);
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheet(workSheet);
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	/**
	 * readExcelHeaders function to establish an initial connection with a work
	 * sheet
	 * 
	 * @param sheet
	 *            : Sheet Name
	 * @param excelHeaders
	 *            : Excel Headers List
	 * @param rowColumnCount
	 *            : Row and Column Count
	 * @return: Hashtable (Having Header column values)
	 */
	public Hashtable<String, Integer> readExcelHeaders(HSSFSheet sheet, Hashtable<String, Integer> excelHeaders,
			Hashtable<String, Integer> rowColumnCount) {

		HSSFRow row = null;
		HSSFCell cell = null;

		for (int r = 0; r < rowColumnCount.get("RowCount"); r++) {

			row = sheet.getRow(r);

			if (row == null)
				continue;

			for (int c = 0; c < rowColumnCount.get("ColumnCount"); c++) {

				cell = row.getCell(c);
				if (cell != null)
					excelHeaders.put(cell.toString(), c);
			}

			break;
		}

		return excelHeaders;
	}

	/**
	 * function will convert the HSSFCell type value to its equivalent string
	 * value
	 * 
	 * @param cell
	 *            : HSSFCell value
	 * @return String
	 */
	public String convertHSSFCellToString(HSSFCell cell) {

		String cellValue = "";

		if (cell != null)
			cellValue = cell.toString().trim();

		return cellValue;

	}

	public String evaluateAndReturnCellValue(HSSFSheet sheet, String cellRange) {

		String val = "";
		FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
		CellReference ref = new CellReference(cellRange);
		HSSFRow row = sheet.getRow(ref.getRow());

		if (row == null)
			return val;

		HSSFCell cell = row.getCell((int) ref.getCol());
		CellValue cellValue = evaluator.evaluate(cell);
		return cellValue.getStringValue();

	}
}
