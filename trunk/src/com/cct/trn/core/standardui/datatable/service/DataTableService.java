package com.cct.trn.core.standardui.datatable.service;

import java.util.List;
import java.util.Locale;

import util.database.CCTConnection;
import util.log.LogUtil;

import com.cct.abstracts.AbstractService;
import com.cct.common.CommonUser;
import com.cct.trn.core.config.parameter.domain.SQLPath;
import com.cct.trn.core.standardui.datatable.domain.DataTable;
import com.cct.trn.core.standardui.datatable.domain.DataTableSearch;
import com.cct.trn.core.standardui.datatable.domain.DataTableSearchCriteria;
import com.cct.trn.core.standardui.datatable.domain.PopupSearchCriteria;

public class DataTableService extends AbstractService{

	private DataTableDAO dao = null;
	
	public DataTableService(CCTConnection conn, CommonUser user, Locale locale) {
		super(conn, user, locale);
		this.dao = new DataTableDAO();
		this.dao.setSqlPath(SQLPath.EMPLOYEE_SQL);
	}
	
	//service ของการสืบค้นข้อมูล
		protected List<DataTableSearch> search(CCTConnection conn, DataTableSearchCriteria criteria) throws Exception {
		  List<DataTableSearch> listResult;
		  
		  try {
			  listResult = dao.search(conn, criteria, user, locale);
		  } catch(Exception e) {
			  LogUtil.SEC.error("",e);
			  throw e;
		  }
			return listResult;
		 }	
		
		//service ค้นหาข้อมูล ใน Popup
		protected List<DataTableSearch> search2(CCTConnection conn, PopupSearchCriteria criteriaPopup) throws Exception {
			  List<DataTableSearch> listResult;
			  
			  try {
				  listResult = dao.search2(conn, criteriaPopup, user, locale);
			  } catch(Exception e) {
				  LogUtil.SEC.error("",e);
				  throw e;
			  }
				return listResult;
			 }	
		//service searchByid Popup
		protected DataTable searchById2(CCTConnection conn,String Id) throws Exception {
			try {
		    	// ค้นหาข้อมูลโดยระบุ id
		        return dao.searchById2(conn, Id, user, locale);
		    } catch (Exception e) {
		        throw e;
		    }
		}
}


