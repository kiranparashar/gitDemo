package com.yatra.dataproviders;

//-----------------------------------------------------------------------------------------------------------
//Description    :   Data Source helper file for testdata from Google Sheet
//Creator        :   Vinod Bhardwaj
//Create         :   
//Modified on/By :   -
//-----------------------------------------------------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

@SuppressWarnings({ "unused" })
public class TestDataGeneratorGoogle {
	public static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final File DATA_STORE_DIR = new File(
			System.getProperty("user.dir") + File.separator + "src/main/resources");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	public static Object[] temp;
	public int size = 0;
	List<Object> googleSheetHeader = null;

	public Object[][] getGoogleSheetTestData(String spreadsheetId, String sSheetName) throws Exception {

		Object[][] array = null;

		try {
			System.out.println("getGoogleSheetTestData : Start");

			List<List<Object>> listData = getGoogleSheetData(spreadsheetId, sSheetName);
			googleSheetHeader = listData.get(0);
			listData.remove(0);

			array = getArrayGoogleSheet(listData, googleSheetHeader);

			System.out.println("_______________________________________");
			System.out.println("Total test in " + sSheetName + " sheet to Run: " + array.length);
			System.out.println("_______________________________________");

		} catch (Exception e) {
			System.out.println("Exception in getGoogleSheetTestData: ");
		} finally {
			System.out.println("getGoogleSheetTestData : End");
		}
		return array;

	}

	public static List<List<Object>> getGoogleSheetData(String spreadsheetId, String sheetName)
			throws InterruptedException {
		List<List<Object>> sheetData = new ArrayList<List<Object>>();

		try {

			sheetData = getAllTestDataFromGoogleSheet(spreadsheetId, sheetName);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheetData;
	}

	public static List<List<Object>> getAllTestDataFromGoogleSheet(String spreadsheetId, String sheetName)
			throws Exception {
		List<List<Object>> values = new ArrayList<List<Object>>();
		try {

			// Build a new authorized API client service.
			Sheets service = getSheetsService();

			// Select full sheet data
			String range = sheetName;

			System.out.println("GoogleSpreadsheetId:" + spreadsheetId);
			System.out.println("TestSheetName: " + sheetName);

			ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();

			values = response.getValues();

			// System.out.println(values);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return values;
	}

	public Object[][] getArrayGoogleSheet(List<List<Object>> sheetData, List<Object> headers) {
		List<List<Object>> list = new ArrayList<List<Object>>();
		List<Object> tempList, list1=null; 

		// int indYN = getIndexFromSheetHeaderListOO("Yes/No");
		int indYN = googleSheetHeader.size() - 1;

		Iterator<List<Object>> checkData = sheetData.iterator();
		List<Object> oneDList = null;
		while (checkData.hasNext()) {
			oneDList = (List<Object>) checkData.next();

			if (oneDList.get(indYN).toString().equalsIgnoreCase("Yes")) {
				tempList = new ArrayList<Object>();
				
				for (int i = 0; i < oneDList.size(); i++) {
					tempList.add(headers.get(i).toString() + "_" + oneDList.get(i).toString());
				}
				
				list.add(tempList);
			}
		}

		Object[][] result = new Object[list.size()][];
		try {
			for (int i = 0; i < result.length; i++) {
				result[i] = new Object[] { list.get(i)
						// listToHashMap(list.get(i))
				};

			}
		} catch (Exception ex) {
			System.out.println("Exception is :: " + ex);
		}
		return result;
	}

	public LinkedHashMap<String, String> listToHashMap(List<Object> list) {
		LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
		try {

			for (int i = 0; i < list.size(); i++) {
				hm.put(googleSheetHeader.get(i).toString(), list.get(i).toString());
			}

			System.out.println(hm);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hm;
	}

	public static void showExelData(List<List<XSSFCell>> sheetData) {
		//
		// Iterates the data and print it out to the console.
		//
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
		}
	}

	public static Cell castCellType(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				System.out.print(cell.getDateCellValue());
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

	public static Sheets getSheetsService() throws Exception {
		Credential credential = authorize();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

	public static Credential authorize() throws Exception {
		// Load client secrets.
		InputStream in = TestDataGeneratorGoogle.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);
	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

}
