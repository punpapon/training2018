package com.cct.trn.core.standardui.datatable.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.tomcat.jni.Local;

import util.TRNUtil;
import util.database.CCTConnection;
import util.database.CCTConnectionUtil;
import util.database.SQLUtil;
import util.log.LogUtil;
import util.string.StringUtil;
import util.type.StringType.ResultType;

import com.cct.abstracts.AbstractDAO;
import com.cct.common.CommonUser;
import com.cct.domain.Transaction;
import com.cct.trn.core.standardui.datatable.domain.DataTable;
import com.cct.trn.core.standardui.datatable.domain.DataTableSearch;
import com.cct.trn.core.standardui.datatable.domain.DataTableSearchCriteria;
import com.cct.trn.core.standardui.datatable.domain.PopupSearchCriteria;
import com.cct.trn.core.tutorial.employee.domain.Employee;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearch;

public class DataTableDAO extends AbstractDAO<DataTableSearchCriteria, DataTableSearch, DataTable, CommonUser, Locale>{

	@Override
	protected long countData(CCTConnection conn,
			DataTableSearchCriteria criteria, CommonUser user, Locale locale)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected List<DataTableSearch> search(CCTConnection conn,
			DataTableSearchCriteria criteria, CommonUser user, Locale locale)
			throws Exception {
		List<DataTableSearch> listResult = new ArrayList<DataTableSearch>();
		
		/*int paramIndex = 0;
		Object[] params = new Object[11];
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getPrefixId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getFullname(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getNickname(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getSex(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getDepartmentId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getPositionId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getStartWorkDate(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getEndWorkDate(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getWorkStatus(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = criteria.getStart() ;
		params[paramIndex] = criteria.getLinePerPage();*/
		
		//เข้าไปทำงานที่ไฟล์  EmployeeSQL
				String sql = SQLUtil.getSQLString(conn.getSchemas()
		                , getSqlPath().getClassName()
		                , getSqlPath().getPath()
		                , "searchEmployeeX"
		                );
		        LogUtil.SEC.debug("SQL : " + sql);
		        
		        Statement stmt = null;
		        ResultSet rst = null;
		        try {        	
		                stmt = conn.createStatement();	//ติดต่อฐ่านข้อมูล
		                rst = stmt.executeQuery(sql);	//query ข้อมูล
		                while (rst.next()) {
			                 DataTableSearch result = new DataTableSearch();
			                 Transaction transaction = new Transaction();
			                 result.setId(StringUtil.nullToString(rst.getString("EMPLOYEE_ID")));
			                 result.setFullname(StringUtil.nullToString(rst.getString("FULLNAME")));
			                 result.setSex(StringUtil.nullToString(rst.getString("SEX")));
			                 result.setDepartmentDesc(StringUtil.nullToString(rst.getString("DEPARTMENT_NAME")));
			                 result.setPositionDesc(StringUtil.nullToString(rst.getString("POSITION_NAME")));
			                 result.setStartWorkDate(StringUtil.nullToString(rst.getString("START_WORK_DATE")));
			                 result.setEndWorkDate(StringUtil.nullToString(rst.getString("END_WORK_DATE")));
			                 result.setWorkStatus(StringUtil.nullToString(rst.getString("WORK_STATUS")));
			                 transaction.setCreateUser(StringUtil.nullToString(rst.getString("CREATE_USER")));
			                 transaction.setCreateDate(StringUtil.nullToString(rst.getString("CREATE_DATE")));
			                 transaction.setUpdateUser(StringUtil.nullToString(rst.getString("UPDATE_USER")));
			                 transaction.setUpdateDate(StringUtil.nullToString(rst.getString("UPDATE_DATE")));
			                 transaction.setCreateRemark(StringUtil.nullToString(rst.getString("REMARK")));
			                 result.setTransaction(transaction);
			                 listResult.add(result);			                 
		                }            
		            
		        } catch (Exception e) {
		            throw e;
		        } finally {
		            CCTConnectionUtil.closeAll(rst, stmt);
		        }
		        
		return listResult;
	}
	protected List<DataTableSearch> search2(CCTConnection conn,
			PopupSearchCriteria criteriaPopup, CommonUser user, Locale locale)
			throws Exception {
		List<DataTableSearch> listResult = new ArrayList<DataTableSearch>();
		
		int paramIndex = 0;
		Object[] params = new Object[2];
		params[paramIndex++] = StringUtil.replaceSpecialString(criteriaPopup.getFirstName(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteriaPopup.getLastName(), conn.getDbType(), ResultType.NULL);
		
		
		//เข้าไปทำงานที่ไฟล์  EmployeeSQL
				String sql = SQLUtil.getSQLString(conn.getSchemas()
		                , getSqlPath().getClassName()
		                , getSqlPath().getPath()
		                , "searchEmployee2"
		                , params);
		        LogUtil.SEC.debug("SQL : " + sql);
		        
		        Statement stmt = null;
		        ResultSet rst = null;
		        try {        	
		                stmt = conn.createStatement();	//ติดต่อฐ่านข้อมูล
		                rst = stmt.executeQuery(sql);	//query ข้อมูล
		                while (rst.next()) {
			                 DataTableSearch result = new DataTableSearch();
			                 Transaction transaction = new Transaction();
			                 result.setId(StringUtil.nullToString(rst.getString("EMPLOYEE_ID")));
			                 result.setFirstName(StringUtil.nullToString(rst.getString("NAME")));
			                 result.setLastName(StringUtil.nullToString(rst.getString("SURNAME")));
			                 result.setSex(StringUtil.nullToString(rst.getString("SEX")));
			                 result.setDepartmentDesc(StringUtil.nullToString(rst.getString("DEPARTMENT_NAME")));
			                 result.setPositionDesc(StringUtil.nullToString(rst.getString("POSITION_NAME")));
			                 result.setStartWorkDate(StringUtil.nullToString(rst.getString("START_WORK_DATE")));
			                 result.setEndWorkDate(StringUtil.nullToString(rst.getString("END_WORK_DATE")));
			                 result.setWorkStatus(StringUtil.nullToString(rst.getString("WORK_STATUS")));
			                 transaction.setCreateUser(StringUtil.nullToString(rst.getString("CREATE_USER")));
			                 transaction.setCreateDate(StringUtil.nullToString(rst.getString("CREATE_DATE")));
			                 transaction.setUpdateUser(StringUtil.nullToString(rst.getString("UPDATE_USER")));
			                 transaction.setUpdateDate(StringUtil.nullToString(rst.getString("UPDATE_DATE")));
			                 transaction.setCreateRemark(StringUtil.nullToString(rst.getString("REMARK")));
			                 result.setTransaction(transaction);
			                 listResult.add(result);			                 
		                }            
		            
		        } catch (Exception e) {
		            throw e;
		        } finally {
		            CCTConnectionUtil.closeAll(rst, stmt);
		        }
		        
		return listResult;
	}
	@Override
	protected DataTable searchById(CCTConnection conn, String id,
			CommonUser user, Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	protected DataTable searchById2(CCTConnection conn, String id,
			CommonUser user, Locale locale) throws Exception {
		LogUtil.TRAINING.debug("searchById");
		
		int paramIndex = 0;

		Object[] params = new Object[1];
		params[paramIndex++] = TRNUtil.convertLongValue(id);

		String sql = SQLUtil.getSQLString(
				conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "searchById"
				, params );
		LogUtil.TRAINING.debug("SQL searchById :" +sql);
		
		DataTable dataTable = null;
		Statement stmt = null;
		ResultSet rst = null;
		
		try {
			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql);
			if (rst.next()) {
				dataTable = new DataTable();
				dataTable.setId(StringUtil.nullToString(rst.getString("EMPLOYEE_ID")));
				dataTable.setFirstName(StringUtil.nullToString(rst.getString("NAME")));
				dataTable.setLastName(StringUtil.nullToString(rst.getString("SURNAME")));			
			}

		} catch (Exception e) {
			throw e;
		} finally {
			CCTConnectionUtil.closeStatement(stmt);
		}
		
		return dataTable;
	}

	@Override
	protected int add(CCTConnection conn, DataTable obj, CommonUser user,
			Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int edit(CCTConnection conn, DataTable obj, CommonUser user,
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
	protected boolean checkDup(CCTConnection conn, DataTable obj,
			CommonUser user, Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	

}
