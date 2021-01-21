package com.yatra.commonworkflows;


import com.yatra.utils.DBUtil;

public class DatabaseWorkFlows {

	static DBUtil dbutil= new DBUtil();
	static String queryForParentId="select parent_id from yatra.b2b_corporate where email =";
	static String queryForEmail="select email from yatra.b2b_corporate where id=";

	public static String getAdminUserEmail(String email) throws Exception{
		dbutil.connect();
		int  parentId= dbutil.executee(queryForParentId+"'"+email+"'" ,"parent_id");
		String adminEmail =dbutil.execute(queryForEmail+"'"+parentId+"'", "email");
		return adminEmail;

	}
	public static boolean isApprovalRequired(String email) throws Exception{
		String adminEmail = getAdminUserEmail(email);
		String query = "select * from b2b_features_config_details"
				+ " where code = 'approval_enabled' and agency_email='"+adminEmail+"'";
		String value =dbutil.execute(query, "value");
		if(value.equals("1")){
			return true;
		}
		else{
			return false;
		}
	}
}
