package com.cct.trn.core.standardui.datatable.service;

import java.util.List;
import java.util.Locale;

import org.apache.tomcat.jni.Local;

import util.database.CCTConnection;

import com.cct.abstracts.AbstractManager;
import com.cct.common.CommonUser;
import com.cct.trn.core.standardui.datatable.domain.DataTable;
import com.cct.trn.core.standardui.datatable.domain.DataTableSearch;
import com.cct.trn.core.standardui.datatable.domain.DataTableSearchCriteria;
import com.cct.trn.core.standardui.datatable.domain.PopupSearchCriteria;
import com.cct.trn.core.tutorial.employee.service.EmployeeService;

public class DataTableManager extends AbstractManager<DataTableSearchCriteria, DataTableSearch, DataTable, CommonUser, Local>{

	private DataTableService service = null;
	
	public DataTableManager(CCTConnection conn, CommonUser user, Locale locale) {
		super(conn, user, locale);
		this.service = new DataTableService(conn, user, locale);
	}

	@Override
	public List<DataTableSearch> search(DataTableSearchCriteria criteria)
			throws Exception {
		List<DataTableSearch> listResult;
		try{
			listResult = service.search(conn, criteria);
		} catch(Exception e) {
			throw e;
		}
		return listResult;
	}
	public List<DataTableSearch> search2(PopupSearchCriteria criteriaPopup)
			throws Exception {
		List<DataTableSearch> listResult;
		try{
			listResult = service.search2(conn, criteriaPopup);
		} catch(Exception e) {
			throw e;
		}
		return listResult;
	}

	@Override
	public DataTable searchById(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DataTable searchById2(String id) throws Exception {
		return service.searchById2(conn, id);
	}
	@Override
	public int add(DataTable obj) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int edit(DataTable obj) throws Exception {
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
