package com.cct.trn.core.tutorial.employee.service;

import java.util.List;
import java.util.Locale;

import util.database.CCTConnection;
import util.log.LogUtil;

import com.cct.abstracts.AbstractManager;
import com.cct.common.CommonUser;
import com.cct.exception.MaxExceedException;
import com.cct.trn.core.config.parameter.domain.ParameterConfig;
import com.cct.trn.core.tutorial.employee.domain.Employee;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearch;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearchCriteria;

public class EmployeeManager extends AbstractManager<EmployeeSearchCriteria, EmployeeSearch, Employee, CommonUser, Locale>{

	private EmployeeService service = null;
	
	public EmployeeManager(CCTConnection conn, CommonUser user, Locale locale) {
		super(conn, user, locale);
		this.service = new EmployeeService(conn, user, locale);
	}

	@Override
	public List<EmployeeSearch> search(EmployeeSearchCriteria criteria) throws Exception {
		// TODO Auto-generated method stub
		List<EmployeeSearch> listResult;
		try{
			//นับจำนวนรายการที่ค้นพบ
		this.service.countData(conn , criteria);
		LogUtil.SEC.debug("Count data[" +criteria.getTotalResult() + "]record.");
		
			//ตรวจสอบจำนวนที่ค้นพบเกินจำนวนที่ระบบกำหนดหรือไม่
		if( (criteria.isCheckMaxExceed()) && (criteria.getTotalResult() > ParameterConfig.getParameter().getApplication().getMaxExceed())) {
				//ถ้าใช่ให้ confirm เพื่อยืนยันการค้นหา
			throw new MaxExceedException();
		}
		else {
				//ถ้าไม่ใช่ให้ค้นหาข้อมูล
			listResult = service.search(conn, criteria);
		}
		} catch(Exception e) {
			throw e;
		}
		return listResult;
	}

	@Override
	public Employee searchById(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int add(Employee obj) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int edit(Employee obj) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateActive(String ids, String activeFlag) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
