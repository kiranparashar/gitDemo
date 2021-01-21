package com.yatra.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;

public class FileUtil {

	public static String separator = System.getProperty("file.separator");

	public static String createFile(String fileName) {

		String userHome = System.getProperty("user.dir");

		try {

			// System.out.println(userHome + separator + fileName);
			File file = new File(userHome + separator + fileName);

			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public FileReader openOrCreateFileToRead(String filePath)
			throws IOException {
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		return fileReader;
	}

	public FileWriter openOrCreateFileToWrite(String filePath,
			boolean appendModeOrNot) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(file.getName(), appendModeOrNot);
		return fileWriter;
	}

	public static void searchAndRemoveString(String fileName,
			ArrayList<String> stringOut, int listSize) throws IOException {

		int count = 0;
		File targetFile = new File(fileName);
		StringBuffer fileContents = new StringBuffer(
				FileUtils.readFileToString(targetFile));
		String[] fileContentLines = fileContents.toString().split(
				System.lineSeparator());

		emptyFile(targetFile);

		fileContents = new StringBuffer();

		for (int fileContentLinesIndex = 0; fileContentLinesIndex < fileContentLines.length; fileContentLinesIndex++) {

			if (fileContentLines[fileContentLinesIndex].contains(stringOut
					.get(count))) {
				System.out.println(stringOut.get(count));
				if (count < listSize) {
					count++;
				}
				continue;
			}

			fileContents.append(fileContentLines[fileContentLinesIndex]
					+ System.lineSeparator());
		}

		FileUtils.writeStringToFile(targetFile, fileContents.toString().trim());

	}

	private static void emptyFile(File targetFile)
			throws FileNotFoundException, IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile,
				"rw");

		randomAccessFile.setLength(0);
		randomAccessFile.close();
	}

	public boolean renameLogFile() throws FileNotFoundException {
		boolean result = false;

		try {
			String logPath = ReadProperties.readPropertyPath("logsPath");

			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Calendar cal = Calendar.getInstance();

			String sReportDate = dateFormat.format(cal.getTime()).replaceAll(
					":", "");

			File dir = new File(logPath);
			File[] files = dir.listFiles();
			if (files == null || files.length == 0) {
				System.out.println("No Log file exist on given path: "
						+ logPath);
			} else {
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().contains("testlog.log")) {
						File oldfile = new File(logPath + "testlog.log");
						Thread.sleep(500);
						File newFile = new File(logPath + "logs " + sReportDate
								+ ".log");
						Thread.sleep(500);
						oldfile.renameTo(newFile);
						result = true;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public File getReportFilePath() {
		File reportFile = null;
		try {
			File dir = new File(ReadProperties.readPropertyPath("reportPath"));

			File[] files = dir.listFiles();
			if (files == null || files.length == 0) {
				System.out.println("No Report exist on given path: "
						+ ReadProperties.readPropertyPath("reportPath"));
			} else {
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().contains("AutomationReport.html")) {
						reportFile = files[i];
					}
				}
			}
		} catch (Exception e) {

		}
		return reportFile;
	}

}
