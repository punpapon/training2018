package com.cct.trn.core.tutorial.employee.service;

import java.util.List;
import java.util.Locale;

import util.database.CCTConnection;
import util.log.LogUtil;

import com.cct.abstracts.AbstractService;
import com.cct.common.CommonUser;
import com.cct.trn.core.config.parameter.domain.SQLPath;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearch;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearchCriteria;

public class EmployeeService extends AbstractService{

	private EmployeeDAO dao = null;
	
	public EmployeeService(CCTConnection conn, CommonUser user, Locale locale) {
		super(conn, user, locale);
		this.dao = new EmployeeDAO();
		this.dao.setSqlPath(SQLPath.EMPLOYEE_SQL);

	}
	
	//service ของการนับข้อมูล
	protected void countData(CCTConnection conn, EmployeeSearchCriteria criteria) throws Exception {
		long totalResult = 0;
		try {
			totalResult = dao.countData(conn, criteria, user, locale);
			criteria.setTotalResult(totalResult);
		} catch(Exception e){
			LogUtil.SEC.error("",e);
			throw e;
		}
	}
	
	//service ของการสืบค้นข้อมูล
	protected List<EmployeeSearch> search(CCTConnection conn, EmployeeSearchCriteria criteria) throws Exception {
	  List<EmployeeSearch> listResult;
	  
	  try {
		  listResult = dao.search(conn, criteria, user, locale);
	  } catch(Exception e) {
		  LogUtil.SEC.error("",e);
		  throw e;
	  }
		return listResult;
	 }
}
