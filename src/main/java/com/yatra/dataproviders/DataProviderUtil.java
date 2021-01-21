package com.yatra.dataproviders;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class DataProviderUtil  {
			

	
	@DataProvider(name = "TestData")
	public static Object[][] getData(ITestContext context) throws Exception {
		String sheetname,dataDriver,workBook;
		String spreadsheetId;
		Object[][] OW_SRP = null;
		sheetname = context.getCurrentXmlTest().getParameter("sheet");
		dataDriver = context.getCurrentXmlTest().getParameter("DataDriver");
		workBook = context.getCurrentXmlTest().getParameter("workBookName");
		spreadsheetId = context.getCurrentXmlTest().getParameter("spreadsheetId");
		
		TestDataGenerator testdata = new TestDataGenerator(workBook , sheetname ,dataDriver, spreadsheetId);
		try{
			OW_SRP = testdata.getTestDataForSanityTest();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return OW_SRP;
	}


}
