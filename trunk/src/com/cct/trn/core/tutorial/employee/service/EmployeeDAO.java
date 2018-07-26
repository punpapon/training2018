package com.cct.trn.core.tutorial.employee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.sql.ResultSet;
import java.sql.Statement;

import util.TRNUtil;
import util.calendar.CalendarUtil;
import util.database.CCTConnection;
import util.database.CCTConnectionUtil;
import util.database.SQLUtil;
import util.log.LogUtil;
import util.string.StringUtil;
import util.type.StringType.ResultType;

import com.cct.abstracts.AbstractDAO;
import com.cct.common.CommonUser;
import com.cct.domain.GlobalVariable;
import com.cct.domain.Transaction;
import com.cct.trn.core.config.parameter.domain.ParameterConfig;
import com.cct.trn.core.tutorial.employee.domain.Employee;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearch;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearchCriteria;


public class EmployeeDAO extends AbstractDAO<EmployeeSearchCriteria, EmployeeSearch, Employee, CommonUser, Locale> {

	@Override
	protected long countData(CCTConnection conn,
			EmployeeSearchCriteria criteria, CommonUser user, Locale locale)
			throws Exception {
		
		return 0;
	}

	@Override
	protected List<EmployeeSearch> search(CCTConnection conn,
			EmployeeSearchCriteria criteria, CommonUser user, Locale locale)
			throws Exception {
		// TODO Auto-generated method stub
		List<EmployeeSearch> listResult = new ArrayList<EmployeeSearch>();
		
		int paramIndex = 0;
		Object[] params = new Object[8];
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getPrefixId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getFullname(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getNickname(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getSex(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getDepartmentId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getPositionId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getStartWorkDate(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getEndWorkDate(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getWorkStatus(), conn.getDbType(), ResultType.NULL);
		
		//เข้าไปทำงานที่ไฟล์  EmployeeSQL
		String sql = SQLUtil.getSQLString(conn.getSchemas()
                , getSqlPath().getClassName()
                , getSqlPath().getPath()
                , "searchEmployee"
                , params);
        LogUtil.SEC.debug("SQL : " + sql);
 
        
        Statement stmt = null;
        ResultSet rst = null;
        try {        	
                stmt = conn.createStatement();	//ติดต่อฐ่านข้อมูล
                rst = stmt.executeQuery(sql);	//query ข้อมูล
                while (rst.next()) {
                 EmployeeSearch result = new EmployeeSearch();
                 Transaction transaction = new Transaction();
                 result.setId(StringUtil.nullToString(rst.getString("EMPLOYEE_ID")));
                 result.setFullname(StringUtil.nullToString(rst.getString("NAME")));
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
	protected Employee searchById(CCTConnection conn, String id,
			CommonUser user, Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int add(CCTConnection conn, Employee obj, CommonUser user,
			Locale locale) throws Exception {
		// TODO Auto-generated method stub
		int paramIndex = 0;
		int id = 0;
		
		Object[] params = new Object[8];
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getPrefixId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getFullname(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getNickname(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getSex(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getDepartmentId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getPositionId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getStartWorkDate(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getEndWorkDate(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getWorkStatus(), conn.getDbType(), ResultType.NULL);
		params[paramIndex] = user.getUserId();
		
		String sql = SQLUtil.getSQLString(conn.getSchemas(), getSqlPath().getClassName(), getSqlPath().getPath(),"addEmployee" , params);
		LogUtil.SEC.debug(sql);
		
		try{
			id = conn.executeInsertStatement(sql);
		} catch(Exception e) {
			throw e;
		} finally {
			
		}
		return id;
	}

	@Override
	protected int edit(CCTConnection conn, Employee obj, CommonUser user,
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
	protected boolean checkDup(CCTConnection conn, Employee obj,
			CommonUser user, Locale locale) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
