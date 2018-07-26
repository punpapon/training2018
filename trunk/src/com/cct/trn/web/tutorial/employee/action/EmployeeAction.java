 package com.cct.trn.web.tutorial.employee.action;

import java.util.List;

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
import com.cct.trn.core.tutorial.employee.domain.EmployeeModel;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearch;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearchCriteria;
import com.cct.trn.core.tutorial.employee.service.EmployeeManager;
import com.cct.trn.core.tutorial.project.domain.ProjectSearch;
import com.cct.trn.core.tutorial.project.service.ProjectManager;
import com.opensymphony.xwork2.ModelDriven;

public class EmployeeAction extends CommonAction implements InterfaceAction, ModelDriven<EmployeeModel>{

	private static final long serialVersionUID = 4274682537625097647L;
	
	private EmployeeModel model = new EmployeeModel();

	public EmployeeAction() {
		try {
			 //กำหนดค่าสิทธิ์การใช้งานเริ่มต้น
			getAuthorization(PFCode.TRN_EMPLOYEE);
		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
		}
	}
	
	@Override
	public String init() throws Exception {
		CCTConnection conn = null;
		String result = null;
		try {
			LogUtil.TRAINING.debug("init");
			
			//1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//2.ตรวจสอบสิทธิ์การใช้งาน และกำหนดค่าเริ่มต้นให้กับหน้าค้นหาของระบบ
			result = manageInitial(conn, model, new EmployeeSearchCriteria(), PF_CODE.getSearchFunction(), PageType.SEARCH);
			
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
		// TODO Auto-generated method stub
		String result 		= null;
		CCTConnection conn 	= null;
		EmployeeManager manager = null;
		try {
			LogUtil.TRAINING.debug(getUser());
			
			//[1] สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//[2] ตรวจสอบสิทธิ์การใช้งาน และจัดการเงือนไขการค้นหา
			result = manageSearchAjax(conn, model, model.getCriteria(), PF_CODE.getSearchFunction());
			
			//[3] ค้นหาข้อมูล
			manager = new EmployeeManager(conn, getUser(), getLocale());
			List<EmployeeSearch> lstResult = manager.search(model.getCriteria());   
			
			//[4] จัดการผลลัพธ์และข้อความถ้าไม่พบข้อมูล
			manageSearchResult(model, lstResult);
			
		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
			manageException(conn, PF_CODE.getSearchFunction(), this, e, model);
			
		} finally {
			getComboForSearch(conn);
			CCTConnectionUtil.close(conn);
		}

		LogUtil.TRAINING.debug("[ ###RESULT ] : " + result);
		return result;
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

	@Override
	public EmployeeModel getModel() {
		return model;
	}
}
