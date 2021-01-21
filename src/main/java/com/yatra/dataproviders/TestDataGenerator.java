package com.yatra.dataproviders;

//-----------------------------------------------------------------------------------------------------------
//Description    :   Data Source helper file for testdata
//Creator        :   Alok Ranjan
//Create         :   
//Modified on/By :   -
//-----------------------------------------------------------------------------------------------------------

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.record.chart.SheetPropertiesRecord;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import com.yatra.utils.ExcelUtil;
import com.yatra.utils.Log;

@SuppressWarnings({ "rawtypes", "unused" })
public class TestDataGenerator {
	
	ExcelUtil readTestData = new ExcelUtil();
	private String workBookName;
	private String workSheet;
	private String testCaseId;
	private String dataDriver;
	private String spreadsheetId;
	List<XSSFCell> sheetHeader = null;
	List<HSSFCell> sheetHeaderOO = null;
	
	TestDataGeneratorGoogle googleSheetDataObj = new TestDataGeneratorGoogle();

	public TestDataGenerator(String xlWorkBook, String xlWorkSheet, String dataDriver, String spreadsheetId) {
		this.workBookName = xlWorkBook;
		this.workSheet = xlWorkSheet;
		this.dataDriver = dataDriver;
		this.spreadsheetId = spreadsheetId;
	}

	public String getDataDriver() {
		return dataDriver;
	}

	public void setDataDriver(String dataDriver) {
		this.dataDriver = dataDriver;
	}

	public String getWorkBookName() {
		return workBookName;
	}

	public void setWorkBookName(String workBookName) {
		this.workBookName = workBookName;
	}

	public String getWorkSheet() {
		return workSheet;
	}

	public void setWorkSheet(String workSheet) {
		this.workSheet = workSheet;
	}
	

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	public static HashMap<String, String> getHashMap(List data) {

		HashMap<String, String> testdata = new LinkedHashMap<String, String>();
		for (int i = 0; i < data.size(); i++) {
			String[] temp = data.get(i).toString().split("_");

			if (temp.length == 1)
				testdata.put(temp[0], "");
			else
				testdata.put(temp[0], temp[1]);
		}
		return testdata;
	}

	public Object[][] getArray(List<List<XSSFCell>> sheetData, String testCaseId) {

		List<List<XSSFCell>> list = new ArrayList<List<XSSFCell>>();
		int indTCID = getIndexFromSheetHeaderList("TCID");
		int indYN = getIndexFromSheetHeaderList("Yes/No");

		Iterator<List<XSSFCell>> checkData = sheetData.iterator();
		List<XSSFCell> oneDList = null;
		while (checkData.hasNext()) {
			oneDList = (List<XSSFCell>) checkData.next();
			if (oneDList.get(indTCID).toString().equals(testCaseId) && oneDList.get(indYN).toString().equals("Yes"))
				list.add(oneDList);
		}

		Object[][] result = new Object[list.size()][];
		try {
			for (int i = 0; i < result.length; i++) {
				result[i] = new Object[] { list.get(i) };
			}
		} catch (Exception ex) {
		}
		return result;
	}

	public List<List<XSSFCell>> getNewArray(List<List<XSSFCell>> sheetData, String testCaseId ) {
		List<List<XSSFCell>> list = new ArrayList<List<XSSFCell>>();

		int indTCID = getIndexFromSheetHeaderList("TCID");
		int indYN = getIndexFromSheetHeaderList("Yes/No");

		Iterator<List<XSSFCell>> checkData = sheetData.iterator();
		List<XSSFCell> oneDList = null;
		while (checkData.hasNext()) {
			oneDList = (List<XSSFCell>) checkData.next();
			if (oneDList.get(indTCID).toString().equals(testCaseId) && oneDList.get(indYN).toString().equals("Yes"))
				list.add(oneDList);
		}
		return list;
	}

	// this method is done , need to execute it one
	public Object[][] getArray(List<List<XSSFCell>> sheetData, List<XSSFCell> headers) {

		int indYN = getIndexFromSheetHeaderList("Yes/No");
		Iterator<List<XSSFCell>> checkData = sheetData.iterator();
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> temp, list1 = null;
		List<XSSFCell> oneDList = null;
		while (checkData.hasNext()) {
			oneDList = (List<XSSFCell>) checkData.next();
			if (oneDList.get(indYN).toString().equals("Yes")){
				list1 = new ArrayList<String>();
			for (int i = 0; i < oneDList.size(); i++) {
				list1.add(headers.get(i).toString() + "_" + oneDList.get(i).toString());
			}
			list.add(list1);
		}
		}
		Object[][] result = new Object[list.size()][];
		try {
			for (int i = 0; i < result.length; i++) {
				result[i] = new Object[] { list.get(i) };
			}
		} catch (Exception ex) {
		}
		return result;
	}

	// this method is done , ran successfully
	public Object[][] getArrayOO(List<List<HSSFCell>> sheetData, List<HSSFCell> headers) {

//		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
//		System.out.println(stackTrace);
		
		int indYN = getIndexFromSheetHeaderListOO("Yes/No");

		Iterator<List<HSSFCell>> checkData = sheetData.iterator();
		List<HSSFCell> oneDList = null;
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> temp, list1 = null;
		int m=0;
		while (checkData.hasNext()) {
			oneDList = (List<HSSFCell>) checkData.next();
			m++;
			//System.out.println(m);
			int j = 0;
			if (oneDList.get(indYN).toString().equalsIgnoreCase("Yes")){
				list1 = new ArrayList<String>();
			for (int i = 0; i < oneDList.size(); i++) {
				list1.add(headers.get(i).toString() + "_" + oneDList.get(i).toString());
			}
			list.add(list1);
		}
		}
		Object[][] result = new Object[list.size()][];
		try {
			for (int i = 0; i < result.length; i++) {
				result[i] = new Object[] { list.get(i) };
			}
		} catch (Exception ex) {
		}
		return result;
	}

	// this method is done , need to run it once
	public static List<List<XSSFCell>> getData(XSSFSheet sheet) {
		List<List<XSSFCell>> sheetData = new ArrayList<List<XSSFCell>>();
		try {
			Iterator<Row> rows = sheet.rowIterator();

			while (rows.hasNext()) {
				XSSFRow rown = (XSSFRow) rows.next();
				Iterator<Cell> cells = rown.cellIterator();

				List<XSSFCell> data = new ArrayList<XSSFCell>();
				while (cells.hasNext()) {
					XSSFCell celln = (XSSFCell) cells.next();
					celln = (XSSFCell) castCellType(celln);
					data.add(celln);
				}
				sheetData.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return sheetData;
	}

	// this method is done , ran successfully
	public static List<List<HSSFCell>> getDataForOldExcel(HSSFSheet sheet) {
		List<List<HSSFCell>> sheetData = new ArrayList<List<HSSFCell>>();
		try {
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				
				HSSFRow rown = (HSSFRow) rows.next();
				Iterator<Cell> cells = rown.cellIterator();
				List<HSSFCell> data = new ArrayList<HSSFCell>();
				while (cells.hasNext()) {
					HSSFCell celln = (HSSFCell) cells.next();
					celln = (HSSFCell) castCellType(celln);
					data.add(celln);
				}
				sheetData.add(data);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return sheetData;
	}

	// done
	public static Cell castCellType(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			} else
				cell.setCellType(Cell.CELL_TYPE_STRING);
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			cell.getRichStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		return cell;
	}

	// done
	public static String convertXSSFCellToString(XSSFCell cell) {
		String cellValue = null;
		if (cell != null) {
			cellValue = cell.toString();
			cellValue = cellValue.trim();
		} else {
			cellValue = "";
		}
		return cellValue;
	}

	public Object[][] getTestDataForQuickBook(String testCaseId) throws Exception {
		XSSFSheet sheet;
		Object[][] array = null;
		try {
			sheet = readTestData.initiateExcelConnection(workSheet, workBookName);
			List<List<XSSFCell>> listData = getData(sheet);
			sheetHeader = listData.get(0);
			listData.remove(0);
			array = getArray(listData, testCaseId);
		} 
		catch (Exception e) {
		} finally {
		}
		return array;
	}

	public Object[][] getTestDataForSanityTest(String testCaseId) throws Exception {
		XSSFSheet sheet;
		Object[][] array = null;
		try {
			sheet = readTestData.initiateExcelConnection(workSheet, workBookName);
			List<List<XSSFCell>> listData = getData(sheet);
			sheetHeader = listData.get(0);
			listData.remove(0);
			// need to edit getArray
			array = getArray(listData, testCaseId);

		} catch (Exception e) {
		} finally {
		}
		return array;

	}

	// this method is done, ran successfully
	public Object[][] getTestDataForSanityTest() throws Exception {
		HSSFSheet sheetOO = null;
		XSSFSheet sheet = null;
		Object[][] array = null;
		try {
			switch (dataDriver) {
			case "EXCEL":
				sheet = readTestData.initiateExcelConnection(workSheet, workBookName);
				List<List<XSSFCell>> XSSFlistData = getData(sheet);
				sheetHeader = XSSFlistData.get(0);
				XSSFlistData.remove(0);
				array = getArray(XSSFlistData, sheetHeader);
				break;
			case "OPENEXCEL":
				sheetOO = readTestData.initiateOpenExcelConnection(workSheet, workBookName);
				List<List<HSSFCell>> HSSFlistData = getDataForOldExcel(sheetOO);
				sheetHeaderOO = HSSFlistData.get(0);
				HSSFlistData.remove(0);
				array = getArrayOO(HSSFlistData, sheetHeaderOO);
				break;
			case "GOOGLESHEET":
				array = googleSheetDataObj.getGoogleSheetTestData(spreadsheetId, workSheet);
				break;
			}
		} catch (Exception e) {
			Log.exception(e);
		} finally {
		
		}
		return array;
	}

	public List<List<XSSFCell>> getTestDataForSanityTests(String testCaseId, String workSheet) throws Exception {
		XSSFSheet sheet = null;
		List<List<XSSFCell>> array = null;
		try {
			sheet = readTestData.initiateExcelConnection(workSheet, workBookName);
			List<List<XSSFCell>> listData = getData(sheet);
			sheetHeader = listData.get(0);
			listData.remove(0);
			array = getNewArray(listData, testCaseId);

		} catch (Exception e) {
		} finally {
		}
		return array;

	}

	public int getIndexFromSheetHeaderList(String hdrColName) {
		Iterator<XSSFCell> hdrItr = sheetHeader.iterator();
		XSSFCell hdrVal = null;
		int indOfCol = 0;
		while (hdrItr.hasNext()) {
			hdrVal = hdrItr.next();
			if (hdrVal.toString().trim().equals(hdrColName))
				indOfCol = sheetHeader.indexOf((Object) hdrVal);
		}
		return indOfCol;

	}

	public int getIndexFromSheetHeaderListOO(String hdrColName) {
		Iterator<HSSFCell> hdrItr = sheetHeaderOO.iterator();
		HSSFCell hdrVal = null;
		int indOfCol = 0;
		while (hdrItr.hasNext()) {
			hdrVal = hdrItr.next();
			if (hdrVal.toString().trim().equals(hdrColName))
				indOfCol = sheetHeaderOO.indexOf((Object) hdrVal);
		}
		return indOfCol;

	}
		
	public static void showExcelData(List<List<XSSFCell>> sheetData) {

		for (int i = 0; i < sheetData.size(); i++) {
			List<XSSFCell> list = (List<XSSFCell>) sheetData.get(i);
			for (int j = 0; j < list.size(); j++) {
				Cell cell = (Cell) list.get(j);
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					System.out.print(cell.getNumericCellValue());
				} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					System.out.print(cell.getRichStringCellValue());
				} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
					System.out.print(cell.getBooleanCellValue());
				}
				if (j < list.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.println("");
		}
	}

}
