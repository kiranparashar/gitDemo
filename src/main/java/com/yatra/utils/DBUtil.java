package com.yatra.utils;

/**
 * @author saurabh.verma
 *
 */

//Please update the SSO token as per the Test Case

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DBUtil {

	String token ;
	int id;
	Statement stmt;
	Connection con;
	String dbURL;
	String userName;
	String password;
	ResultSet rs;
	public void connect() {
		try {
			this.dbURL = ReadProperties.getProperty("dbURL");
			this.userName = ReadProperties.getProperty("userName");
			this.password = ReadProperties.getProperty("password");
			Class.forName("com.mysql.jdbc.Driver");
			this.con = DriverManager.getConnection(dbURL, userName, password);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void connect(String key){
		try {
			this.dbURL = ReadProperties.getProperty(key+"dbURL");
			this.userName = ReadProperties.getProperty(key+"userName");
			this.password = ReadProperties.getProperty(key+"password");
			Class.forName("com.mysql.jdbc.Driver");
			this.con = DriverManager.getConnection(dbURL, userName, password);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String execute(String query, String ColumnName) {

		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				token = rs.getString(ColumnName);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return token;

	}
	public List<String> getList(String query, String ColumnName){
		List<String> list = new ArrayList<String>();
		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				token = rs.getString(ColumnName);
				list.add(token);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public int executee(String query, String ColumnName) {

		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				id = rs.getInt(ColumnName);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return id;

	}
	public LinkedHashMap<String, String> execute(String query ,String column1 , String column2)
	throws Exception{
		LinkedHashMap<String , String> data= new LinkedHashMap<String , String>();
		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				data.put(rs.getString(column1), rs.getString(column2));
			}
		} catch (Exception e) {
			Log.exception(e);
		}
		return data;
		
	}
	
	public List<String> execute(String query ,String column1 , String column2 , String column3)
	throws Exception{
		//LinkedHashMap<String , String> data= new LinkedHashMap<String , String>();
		List<String> data= new ArrayList<String>();
		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				//data.put(rs.getString(column1), rs.getString(column2));
				data.add(rs.getString(column1)+"_"+rs.getString(column2)+"_"+rs.getString(column3));
			}
		} catch (Exception e) {
			Log.exception(e);
		}
		return data;
		
	}
	public ResultSet execute(String query) throws Exception{
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
		} catch (Exception e) {
			Log.exception(e);
		}
		return rs;
		
	}
}
