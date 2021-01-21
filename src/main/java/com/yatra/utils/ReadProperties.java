package com.yatra.utils;

/**@author - vinod.kumar
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.testng.log4testng.Logger;

public class ReadProperties {
	
	private static final Logger log = Logger.getLogger(ReadProperties.class);
	private static Properties properties;
	private static ReadProperties envProperties;
	
	public ReadProperties(){
		properties =  loadProperties();
	}
	public ReadProperties(String propertyFile){
		properties =  loadProperties(propertyFile);
	}
	
	private Properties loadProperties() {
		File file = new File("./src/main/resources/config.properties");
		FileInputStream fileInput = null;
		Properties props = new Properties();

		try {
			fileInput = new FileInputStream(file);
			props.load(fileInput);
			fileInput.close();
		} catch (FileNotFoundException e) {
			log.error("config.properties is missing or corrupt : " + e.getMessage());
		} catch (IOException e) {
			log.error("read failed due to: " + e.getMessage());
		}

		return props;
	}
	
	private Properties loadProperties(String propertyFile) {
		File file = new File("./src/main/resources/"+propertyFile+".properties");
		FileInputStream fileInput = null;
		Properties props = new Properties();

		try {
			fileInput = new FileInputStream(file);
			props.load(fileInput);
			fileInput.close();
		} catch (FileNotFoundException e) {
			log.error(""+propertyFile+".properties is missing or corrupt : " + e.getMessage());
		} catch (IOException e) {
			log.error("read failed due to: " + e.getMessage());
		}

		return props;
	}
	public static ReadProperties getInstance() {
		if (envProperties == null) {
			envProperties = new ReadProperties();
		}
		return envProperties;
	}
	
	public static ReadProperties getInstance(String propertyFile) {
		ReadProperties envProperties=null;
		if (envProperties == null) {
			envProperties = new ReadProperties(propertyFile);
		}
		return envProperties;
	}

	public static  String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public static boolean hasProperty(String key) {		
		return StringUtils.isNotBlank(properties.getProperty(key));
	}
	
	public static String readPropertyPath(String propertyKey) {
		
		File dir1 = new File(".");
		String strBasePath = null;
		String val = null;
		String projectPath=null;
		try {
			strBasePath = dir1.getCanonicalPath();
			properties.load(new FileInputStream(strBasePath + File.separator+"src"+File.separator+"main"+File.separator+"resources"
					+ File.separator + "config.properties"));
			
			projectPath = System.getProperty("user.dir");
			System.out.println(projectPath);
			
			val = projectPath+"\\"+properties.getProperty(propertyKey);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return val;
	}
	
	public static String readProperty(String propertyKey) {

		Properties prop = new Properties();
		File dir1 = new File(".");
		String strBasePath = null;
		String val = null;
		
		try {
			strBasePath = dir1.getCanonicalPath();
			prop.load(new FileInputStream(strBasePath + File.separator + "src/main/resources"
					+ File.separator + "config.properties"));
			
			val = prop.getProperty(propertyKey);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return val;

	}
	
	
}