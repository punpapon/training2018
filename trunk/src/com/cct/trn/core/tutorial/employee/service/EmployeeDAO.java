package com.cct.trn.core.tutorial.employee.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import com.cct.trn.core.tutorial.employee.domain.Person;


public class EmployeeDAO extends AbstractDAO<EmployeeSearchCriteria, EmployeeSearch, Employee, CommonUser, Locale> {

	@Override
	protected long countData(CCTConnection conn,
			EmployeeSearchCriteria criteria, CommonUser user, Locale locale)
			throws Exception {
		
		int total = 0 ;
		int paramIndex = 0;
		
		String startWorkDate = null;
		if (criteria.getStartWorkDate() != null && !criteria.getStartWorkDate().equals("")) {
			startWorkDate = CalendarUtil.convertDateString(criteria.getStartWorkDate()
					, ParameterConfig.getParameter().getDateFormat().getForDisplay()
					, locale
					, GlobalVariable.DATE_FORMAT_FOR_SEARCH + GlobalVariable.Delimeter.BLANK + GlobalVariable.START_TIME
					, ParameterConfig.getParameter().getApplication().getDatabaseLocale());
		}
		
		String endWorkDate = null;
		if (criteria.getEndWorkDate() != null && !criteria.getEndWorkDate().equals("")) {
			endWorkDate = CalendarUtil.convertDateString(criteria.getEndWorkDate()
					, ParameterConfig.getParameter().getDateFormat().getForDisplay()
					, locale
					, GlobalVariable.DATE_FORMAT_FOR_SEARCH + GlobalVariable.Delimeter.BLANK + GlobalVariable.START_TIME
					, ParameterConfig.getParameter().getApplication().getDatabaseLocale());
			
		}
		
		LogUtil.TRAINING.debug("Create Date : " + startWorkDate + " -> " + endWorkDate);
		
		Object[] params = new Object[9];
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getPrefixId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getFullname(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getNickname(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getSex(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getDepartmentId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getPositionId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(startWorkDate, conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(endWorkDate, conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(criteria.getWorkStatus(), conn.getDbType(),ResultType.NULL);
		
		
		
		String sql = SQLUtil.getSQLString(
				conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "searchCount"
				,params);
		LogUtil.TRAINING.debug("SQL searchCount :" +sql);

		Statement stmt = null;
		ResultSet rst = null;
		try {
			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql);
			if (rst.next()) {
				total = rst.getInt("TOT");
				
			}
			
		}catch (Exception e){
			throw e;
		}finally {
			CCTConnectionUtil.closeAll(rst, stmt);
			
		}
		return total;
	}

	@Override
	protected List<EmployeeSearch> search(CCTConnection conn,
			EmployeeSearchCriteria criteria, CommonUser user, Locale locale)
			throws Exception {
		// TODO Auto-generated method stub
		List<EmployeeSearch> listResult = new ArrayList<EmployeeSearch>();
		int paramIndex = 0;
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
		params[paramIndex++] = criteria.getStart() - 1;
		params[paramIndex] = criteria.getLinePerPage();
		
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

	protected List<EmployeeSearch> searchExport(CCTConnection conn,
			EmployeeSearchCriteria criteria, CommonUser user, Locale locale)
			throws Exception {
		// TODO Auto-generated method stub
		List<EmployeeSearch> listResult = new ArrayList<EmployeeSearch>();
		int paramIndex = 0;
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
		params[paramIndex++] = criteria.getStart() - 1;
		params[paramIndex] = criteria.getLinePerPage();
		
		//เข้าไปทำงานที่ไฟล์  EmployeeSQL
		String sql = SQLUtil.getSQLString(conn.getSchemas()
                , getSqlPath().getClassName()
                , getSqlPath().getPath()
                , "searchExportEmployee"
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

	
	@Override
	protected Employee searchById(CCTConnection conn, String id,
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

		Employee employee = null;

		Statement stmt = null;
		ResultSet rst = null;
		try {
			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql);
			if (rst.next()) {
				employee = new Employee();
				employee.setId(StringUtil.nullToString(rst.getString("EMPLOYEE_ID")));
				employee.setPrefixId(StringUtil.nullToString(rst.getString("PREFIX_ID")));
				employee.setPrefixName(StringUtil.nullToString(rst.getString("PREFIX_NAME")));
				employee.setSex(StringUtil.nullToString(rst.getString("SEX")));
				employee.setName(StringUtil.nullToString(rst.getString("NAME")));
				employee.setLastName(StringUtil.nullToString(rst.getString("SURNAME")));
				employee.setNickName(StringUtil.nullToString(rst.getString("NICK_NAME")));
				employee.setDepartmentId(StringUtil.nullToString(rst.getString("DEPARTMENT_ID")));
				employee.setDepartmentDesc(StringUtil.nullToString(rst.getString("DEPARTMENT_NAME")));
				employee.setPositionId(StringUtil.nullToString(rst.getString("POSITION_ID")));
				employee.setPositionDesc(StringUtil.nullToString(rst.getString("POSITION_NAME")));
				employee.setStartWorkDate(StringUtil.nullToString(rst.getString("START_WORK_DATE")));
				employee.setEndWorkDate(StringUtil.nullToString(rst.getString("END_WORK_DATE")));
				employee.setWorkStatus(StringUtil.nullToString(rst.getString("WORK_STATUS")));
				employee.setRemark(StringUtil.nullToString(rst.getString("REMARK")));
				Transaction transaction =  new Transaction();
				transaction.setCreateDate(StringUtil.nullToString(rst.getString("CREATE_DATE")));
				transaction.setCreateUser(StringUtil.nullToString(rst.getString("CREATE_USER")));
				transaction.setUpdateDate(StringUtil.nullToString(rst.getString("UPDATE_DATE")));
				transaction.setUpdateUser(StringUtil.nullToString(rst.getString("UPDATE_USER")));
				employee.setTransaction(transaction);

			}

		} catch (Exception e) {
			throw e;
		} finally {
			CCTConnectionUtil.closeStatement(stmt);
		}
		
		return employee;
		
	}

	@Override
	protected int add(CCTConnection conn, Employee obj, CommonUser user,
			Locale locale) throws Exception {
		// TODO Auto-generated method stub
		int paramIndex = 0;
		int id = 0;
		
		Object[] params = new Object[10];
		
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getName(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getLastName(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getNickName(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getPrefixId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getSex(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getPositionId(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getStartWorkDate(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getWorkStatus(), conn.getDbType(), ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getRemark(), conn.getDbType(), ResultType.NULL);		
		params[paramIndex++] = TRNUtil.convertLongValue(user.getUserId());
		
		
		String sql = SQLUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				,"insertEmployee" 
				, params);
		LogUtil.TRAINING.debug("SQL insertProject :" +sql);
		Statement stmt = null;
		try{
			stmt = conn.createStatement();
			stmt.executeUpdate(sql.toString());
		} catch(Exception e) {
			throw e;
		} finally {
			CCTConnectionUtil.closeStatement(stmt);
		}
		return id;
	}

	@Override
	protected int edit(CCTConnection conn, Employee obj, CommonUser user,
			Locale locale) throws Exception {
		LogUtil.TRAINING.info("edit");
		
		int paramIndex = 0;
		Object[] params = new Object[10];
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getName(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getLastName(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getNickName(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getPrefixId(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getPositionId(), conn.getDbType(),ResultType.NULL);
		//params[paramIndex++] = StringUtil.replaceSpecialString(obj.getSex(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getEndWorkDate(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getWorkStatus(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getRemark(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = TRNUtil.convertLongValue(user.getUserId());
		params[paramIndex++] = TRNUtil.convertLongValue(obj.getId());
		
		String sql = SQLUtil.getSQLString(conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "updateEmployee"
				, params);

		LogUtil.TRAINING.debug("[SAVE EDIT PROJECT ] :"+sql);
		Statement stmt = null;

		try {

			stmt = conn.createStatement();
			stmt.executeUpdate(sql.toString());


		} catch (Exception e) {
			throw e;
		} finally {
			CCTConnectionUtil.closeStatement(stmt);
		}

		return 0;
	}

	@Override
	protected int delete(CCTConnection conn, String ids, CommonUser user,
			Locale locale) throws Exception {
		int paramIndex = 0;

		Object[] params = new Object[2];
		//params[paramIndex++] = TRNUtil.convertLongValue(user.getActive());
		params[paramIndex++] = TRNUtil.convertLongValue(user.getUserId());
		params[paramIndex++] = TRNUtil.convertLongValue(ids);

		String sql = SQLUtil.getSQLString(
				conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "deleteEmployee"
				, params );
		
		LogUtil.TRAINING.debug("[DELETE PROJECT ] :"+sql);
		Statement stmt = null;

		try {

			stmt = conn.createStatement();
			stmt.executeUpdate(sql.toString());


		} catch (Exception e) {
			throw e;
		} finally {
			CCTConnectionUtil.closeStatement(stmt);
		}
		
		
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
		boolean checkDup = false;
		int count = 0 ;
		int paramIndex = 0;

		Object[] params = new Object[7];
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getName(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getLastName(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getNickName(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getSex(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getPositionId(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getEmployeeId(), conn.getDbType(),ResultType.NULL);
		params[paramIndex++] = StringUtil.replaceSpecialString(obj.getWorkStatus(), conn.getDbType(),ResultType.NULL);

		String sql = SQLUtil.getSQLString(
				conn.getSchemas()
				, getSqlPath().getClassName()
				, getSqlPath().getPath()
				, "searchCountDup"
				, params );
		LogUtil.TRAINING.debug("SQL checkDup :" +sql);

		Statement stmt = null;
		ResultSet rst = null;
		try {
			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql);
			if (rst.next()) {
				count = rst.getInt("TOT");
			}
			if(count > 0){
				checkDup = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			CCTConnectionUtil.closeAll(rst, stmt);
		}
		return checkDup;
	
	}
	
	protected Collection<Person> ireportEmployee(CCTConnection conn) throws Exception {
		Collection<Person> listCollection = new ArrayList<Person>();
		
		String sql = SQLUtil.getSQLString(conn.getSchemas()
                , getSqlPath().getClassName()
                , getSqlPath().getPath()
                , "ireportEmployee"
                );
        LogUtil.SEC.debug("SQL : " + sql);
        Statement stmt = null;
        ResultSet rst = null;
        
        try{
        	 stmt = conn.createStatement();	//ติดต่อฐ่านข้อมูล
             rst = stmt.executeQuery(sql);	//query ข้อมูล
             while (rst.next()) {
	                 Person result = new Person();	                
	                 result.setPersonId(StringUtil.nullToString(rst.getString("PERSON_ID")));
	                 result.setName(StringUtil.nullToString(rst.getString("NAME")));
	                 result.setLastName(StringUtil.nullToString(rst.getString("LAST_NAME")));
	                 result.setAge(StringUtil.nullToString(rst.getString("AGE")));
	                 result.setDepartmentId(StringUtil.nullToString(rst.getString("DEPARTMENT_ID")));	                             
	                 listCollection.add(result);
             }
        } catch(Exception e) {
        	throw e;
        } finally {
        	CCTConnectionUtil.closeAll(rst, stmt);
        }
		
		return listCollection;
	}
}
