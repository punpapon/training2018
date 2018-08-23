package com.cct.trn.core.standardui.dialog.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import util.database.CCTConnection;
import util.database.CCTConnectionUtil;
import util.database.SQLUtil;
import util.log.LogUtil;
import util.string.StringUtil;

import com.cct.abstracts.AbstractDAO;
import com.cct.common.CommonDomain;
import com.cct.common.CommonUser;
import com.cct.trn.core.standardui.dialog.domain.Dialog;
import com.cct.trn.core.standardui.dialog.domain.DialogSearch;
import com.cct.trn.core.standardui.dialog.domain.DialogSearchCriteria;

public class DialogDAO extends AbstractDAO<DialogSearchCriteria, CommonDomain, Dialog, CommonUser, Locale>{
	
/*	protected List<DialogSearch> search(CCTConnection conn, DialogSearchCriteria criteria) throws Exception {
	     
	}*/

	@Override
	protected long countData(CCTConnection conn, DialogSearchCriteria criteria,
			CommonUser user, Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected List<CommonDomain> search(CCTConnection conn,DialogSearchCriteria criteria, CommonUser user, Locale locale) throws Exception {
		List<CommonDomain> listResult = new ArrayList<CommonDomain>();
	     
		/*		int paramIndex = 0;
				Object[] params = new Object[1];
				params[paramIndex++] = criteria.getOrderList();*/
			     
			  /*  if (criteria.getNavigatePopup().equals("true")) {
			        // กรณีใช้งานแบบมี navigate
			        params[paramIndex++] = criteria.getStart() - 1;
			        params[paramIndex++] = criteria.getLinePerPage();
			    } else {
			        // กรณีใช้งานแบบไม่มี navigate
			        params[paramIndex++] = null;
			        params[paramIndex++] = null;
			    }*/
			     
			    // execute sql ...
			    String sql = SQLUtil.getSQLString(conn.getSchemas()
		                , getSqlPath().getClassName()
		                , getSqlPath().getPath()
		                , "searchEmployeeX"
		                );
		        LogUtil.SEC.debug("SQL : " + sql);
			    Statement stmt = null;    
		        ResultSet rst = null;
		        try{
		        	 stmt = conn.createStatement();	//ติดต่อฐ่านข้อมูล
		             rst = stmt.executeQuery(sql);	//query ข้อมูล
					    int i = 1;
					    /*if (criteria.getNavigatePopup().equals("true")) {
					        i = criteria.getStart();
					    }*/		    
					    while (rst.next()) {        
					    	Dialog dialog = new Dialog();
					        dialog.setRownum(i + "."); // สำหรับการ set ค่าลำดับ
					        dialog.setIdPopup(rst.getString("EMPLOYEE_ID")); // สำหรับการ set ค่า Id ไว้ที่ attribute กลางของ popup
					        // set ResultSet to object ...
					        DialogSearch result = new DialogSearch();
					        result.setFullname(StringUtil.nullToString(rst.getString("FULLNAME")));
					        result.setSex(StringUtil.nullToString(rst.getString("SEX")));
					        listResult.add(result);
					        
					        i++;
					    }
		        }catch (Exception e) {
		        	throw e;
		        }finally {
		        	CCTConnectionUtil.closeAll(rst, stmt);
		        }
			    return listResult;
	}

	@Override
	protected Dialog searchById(CCTConnection conn, String id, CommonUser user,
			Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int add(CCTConnection conn, Dialog obj, CommonUser user,
			Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int edit(CCTConnection conn, Dialog obj, CommonUser user,
			Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int delete(CCTConnection conn, String ids, CommonUser user,
			Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int updateActive(CCTConnection conn, String ids,
			String activeFlag, CommonUser user, Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean checkDup(CCTConnection conn, Dialog obj, CommonUser user,
			Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}
