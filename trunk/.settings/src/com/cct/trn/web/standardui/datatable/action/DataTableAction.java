package com.cct.trn.web.standardui.datatable.action;

import java.util.List;

import org.apache.log4j.Logger;

import util.database.CCTConnection;
import util.database.CCTConnectionProvider;
import util.database.CCTConnectionUtil;
import util.log.LogUtil;

import com.cct.common.CommonAction;
import com.cct.common.CommonModel.PageType;
import com.cct.domain.GlobalType;
import com.cct.domain.Transaction;
import com.cct.interfaces.InterfaceAction;
import com.cct.trn.core.config.parameter.domain.DBLookup;
import com.cct.trn.core.security.authorization.domain.PFCode;
import com.cct.trn.core.selectitem.service.SelectItemManager;
import com.cct.trn.core.standardui.datatable.domain.DataTable;
import com.cct.trn.core.standardui.datatable.domain.DataTableModel;
import com.cct.trn.core.standardui.datatable.domain.DataTableSearch;
import com.cct.trn.core.standardui.datatable.domain.DataTableSearchCriteria;
import com.cct.trn.core.standardui.datatable.service.DataTableManager;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearch;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearchCriteria;
import com.cct.trn.core.tutorial.employee.service.EmployeeManager;
import com.opensymphony.xwork2.ModelDriven;

public class DataTableAction extends CommonAction implements InterfaceAction,ModelDriven<DataTableModel> {

	private static final long serialVersionUID = -8937836769224842997L;

	private DataTableModel model = new DataTableModel();

	public DataTableAction() {
		try {
			 //กำหนดค่าสิทธิ์การใช้งานเริ่มต้น
			getAuthorization(PFCode.TRN_EMPLOYEE);
		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
		}
	}

	public String init() throws Exception {
		CCTConnection conn = null;
		String result = null;
		LogUtil.TRAINING.debug("init");
		try {
			LogUtil.TRAINING.debug("init");
			
			//1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//2.ตรวจสอบสิทธิ์การใช้งาน และกำหนดค่าเริ่มต้นให้กับหน้าค้นหาของระบบ
			result = manageInitial(conn, model, new DataTableSearchCriteria(), PF_CODE.getSearchFunction(), PageType.SEARCH);
			
		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
			//3.จัดการ exception กรณีที่มี exception เกิดขึ้นในระบบ
	        manageException(conn, PF_CODE.getSearchFunction(), this, e, model);
		}finally{
			
			//4.Load combo ทั้งหมดที่ใช้ในหน้าค้นหา เพื่อเตรียม binding เข้ากับ criteria
	        getComboForSearch(conn);
	         
	        //5.Close connection หลังเลิกใช้งาน
	        CCTConnectionUtil.close(conn);
		}
		return result;
	}
	
	@Override
	public DataTableModel getModel() {
		return model;
	}
	
	@Override
	protected Logger loggerInititial() {
		return LogUtil.TRAINING;
	}

	@Override
	public void getComboForSearch(CCTConnection conn) {
		try {
			model.setListSex(SelectItemManager.getMapGlobalData().get(getLocale()).get(GlobalType.SEX.getValue()));
			model.setListWorkStatus(SelectItemManager.getMapGlobalData().get(getLocale()).get(GlobalType.WORK_STATUS.getValue()));
			
			SelectItemManager manager = new SelectItemManager(conn, getUser(), getLocale());
			model.setListPrefix(manager.searchPrefixSelectItem(getLocale())); 	
		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
		}
	}

	@Override
	public void getComboForAddEdit(CCTConnection conn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String search() throws Exception {
		String result 		= null;
		CCTConnection conn 	= null;
		DataTableManager manager = null;
		try{
			getAuthorization(PFCode.TRN_EMPLOYEE);
			//[1] สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//[2] ตรวจสอบสิทธิ์การใช้งาน และจัดการเงือนไขการค้นหา
			//result = manageSearchAjax(conn, model, model.getCriteria(), PF_CODE.getSearchFunction());
			result = "serach";
			//[3] ค้นหาข้อมูล
			manager = new DataTableManager(conn, getUser(), getLocale());
			List<DataTableSearch> listResult = manager.search(model.getCriteria());
			model.setListResult(listResult);
			
			
			for(DataTableSearch data : listResult){
				System.out.println("DATA : " + data);
			}
			//[4] จัดการผลลัพธ์และข้อความถ้าไม่พบข้อมูล
			manageSearchResult(model, listResult);
			
		} catch(Exception e) {
			LogUtil.TRAINING.error("", e);
			manageException(conn, PF_CODE.getSearchFunction(), this, e, model);
		} finally {
			getComboForSearch(conn);
			CCTConnectionUtil.close(conn);
		}
		return "search";
	}
	public String search2() throws Exception {
		String result 		= null;
		CCTConnection conn 	= null;
		DataTableManager manager = null;
		try{
			getAuthorization(PFCode.TRN_EMPLOYEE);
			//[1] สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//[2] ตรวจสอบสิทธิ์การใช้งาน และจัดการเงือนไขการค้นหา
			//result = manageSearchAjax(conn, model, model.getCriteria(), PF_CODE.getSearchFunction());
			result = "initialDialog";
			//[3] ค้นหาข้อมูล
			manager = new DataTableManager(conn, getUser(), getLocale());
			List<DataTableSearch> listResult = manager.search2(model.getCriteriaPopup());
			model.setListResult(listResult);
			
			
			for(DataTableSearch data : listResult){
				System.out.println("DATA : " + data);
			}
			//[4] จัดการผลลัพธ์และข้อความถ้าไม่พบข้อมูล
			manageSearchResult(model, listResult);
			
		} catch(Exception e) {
			LogUtil.TRAINING.error("", e);
			manageException(conn, PF_CODE.getSearchFunction(), this, e, model);
		} finally {
			getComboForSearch(conn);
			CCTConnectionUtil.close(conn);
		}
		return "search";
	}
	public String searchById2() throws Exception {
		String result 		= null;
		CCTConnection conn 	= null;
		DataTableManager manager = null;
		try{
			getAuthorization(PFCode.TRN_EMPLOYEE);
			//[1] สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//[2] ตรวจสอบสิทธิ์การใช้งาน และจัดการเงือนไขการค้นหา
			//result = manageSearchAjax(conn, model, model.getCriteria(), PF_CODE.getSearchFunction());
			result = "serach";
			//[3] ค้นหาข้อมูล
			manager = new DataTableManager(conn, getUser(), getLocale());
			DataTable dataTable = manager.searchById2(model.getCriteriaPopup().getId());
			model.setDataTable(dataTable);
			
			
		} catch(Exception e) {
			LogUtil.TRAINING.error("", e);
			manageException(conn, PF_CODE.getSearchFunction(), this, e, model);
		} finally {
			CCTConnectionUtil.close(conn);
		}
		return "searchListById";
	}

	@Override
	public String gotoAdd() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String add() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String edit() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gotoEdit() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gotoView() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateActive() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String export() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		
	}
	
	public String showDialog() throws Exception {
		return null;
	}
}
