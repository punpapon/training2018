
package com.cct.trn.web.tutorial.employee.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.bundle.BundleUtil;
import util.database.CCTConnection;
import util.database.CCTConnectionProvider;
import util.database.CCTConnectionUtil;
import util.log.LogUtil;

import com.cct.common.CommonAction;
import com.cct.common.CommonAction.ResultType;
import com.cct.common.CommonAction.ReturnType;
import com.cct.common.CommonModel.PageType;
import com.cct.domain.GlobalType;
import com.cct.domain.GlobalVariable;
import com.cct.domain.Transaction;
import com.cct.interfaces.InterfaceAction;
import com.cct.trn.core.config.parameter.domain.DBLookup;
import com.cct.trn.core.security.authorization.domain.PFCode;
import com.cct.trn.core.selectitem.service.SelectItemManager;
import com.cct.trn.core.tutorial.employee.domain.Employee;
import com.cct.trn.core.tutorial.employee.domain.EmployeeModel;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearch;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearchCriteria;
import com.cct.trn.core.tutorial.employee.service.EmployeeManager;
import com.cct.trn.core.tutorial.project.domain.Project;
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
		String result = null;
		CCTConnection conn = null;

		try {
			// 1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			// 2.ตรวจสอบสิทธิ์ หน้าเพิ่ม
			result = manageGotoAdd(conn, model);
			
			model.setEmployee(new Employee());
			
			// กำหนดค่าเริ่มต้น
			
	        EmployeeManager manager = new EmployeeManager(conn, getUser(), getLocale());
	         model.setEmployee(manager.defaultValue());
	         
	         
			// กำหนดค่า checkbox ให้เป็น active (checked)
			model.getEmployee().getActive().setCode(GlobalVariable.FLAG_ACTIVE);
			ResourceBundle bundle = BundleUtil.getBundle("resources.bundle.tutorial.MessageProject", getLocale());
			model.getEmployee().getActive().setDesc(bundle.getString("pro.activeDesc"));
			

		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
			// 3.จัดการ exception กรณีที่มี exception เกิดขึ้นในระบบ
			manageException(conn, PF_CODE.getAddFunction(), this, e, model);
		} finally {
			getComboForAddEdit(conn);
			// 5.Close connection หลังเลิกใช้งาน
			CCTConnectionUtil.close(conn);
		}
		
		return result;
	}

	@Override
	public String add() throws Exception {
		String result = null;

		CCTConnection conn = null;

		try {
			//1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//2.ตรวจสอบสิทธิ์ หน้าเพิ่ม
			result = manageAdd(conn, model);
			
			//3.save Data
			EmployeeManager manager = new EmployeeManager(conn, getUser(), getLocale());
			manager.add(model.getEmployee());
			
			model.setEmployee(new Employee());
			

		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
			//5.จัดการ exception กรณีที่มี exception เกิดขึ้นในระบบ
			manageException(conn, PF_CODE.getAddFunction(), this, e, model);
		} finally {
			//6.Load combo ทั้งหมดที่ใช้ในหน้าเพิ่ม
			getComboForAddEdit(conn);

			//7.Close connection หลังเลิกใช้งาน
			CCTConnectionUtil.close(conn);
		}

		return result;
	}

	@Override
	public String edit() throws Exception {
		String result = null;

		CCTConnection conn = null;

		try {
			//1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//2.ตรวจสอบสิทธิ์ หน้าแก้ไข
	        result = manageEdit(conn, model,ResultType.BASIC);
	        
	        //3.บันทึกแก้ไขข้อมูล
	        EmployeeManager manager = new EmployeeManager(conn, getUser(), getLocale());
			manager.edit(model.getEmployee());
	        
	        //model.setProjects(new Projects());
			manageSearchAjax(conn, model, model.getCriteria(), getPF_CODE().getSearchFunction());
	        result = ReturnType.SEARCH.getResult();
			
		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
			//4.จัดการ exception กรณีที่มี exception เกิดขึ้นในระบบ
			manageException(conn, PF_CODE.getEditFunction(), this, e, model);
			
			//5.กรณีที่เกิด exception ขึ้นในระบบ จะต้องแสดง message error และคงข้อมูลที่กรอกไว้ในหน้าแก้ไขเช่นเดิม
	        result = ReturnType.ADD_EDIT.getResult();
	        
		} finally {
			getComboForAddEdit(conn);
			CCTConnectionUtil.close(conn);
		}

		return result;
	}

	@Override
	public String gotoEdit() throws Exception {
		String result = null;
		CCTConnection conn = null;

		try {
			//1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//2.ตรวจสอบสิทธิ์ หน้าแก้ไข
			result = manageGotoEdit(conn, model);
			
			//3.ค้นหาข้อมูลผู้ใช้ ตาม id ที่เลือกมาจากหน้าจอ
	        EmployeeManager manager = new EmployeeManager(conn, getUser(), getLocale());
	        LogUtil.TRAINING.debug("Edit id: " + model.getEmployee().getId());
	        Employee employees = manager.searchById(model.getEmployee().getId());
	        model.setEmployee(employees);
	        
			//4.กำหนดให้แสดง user transaction
			showTransaction(model.getEmployee().getTransaction());
			
		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
			//5.จัดการ exception กรณีที่มี exception เกิดขึ้นในระบบ
			manageException(conn, PF_CODE.getEditFunction(), this, e, model);
		} finally {
			//6.Load combo ทั้งหมดที่ใช้ในหน้าเพิ่ม
			getComboForAddEdit(conn);
			
			//7.Close connection หลังเลิกใช้งาน
			CCTConnectionUtil.close(conn);
		}

		return result;
	}

	@Override
	public String gotoView() throws Exception {
		String result = null;
		CCTConnection conn = null;

		try {
			//1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());

			//2.ตรวจสอบสิทธิ์ 
			result = manageGotoView(conn, model);
			
			//3.ค้นหาข้อมูลผู้ใช้ ตาม id ที่เลือกมาจากหน้าจอ
	        EmployeeManager manager = new EmployeeManager(conn, getUser(), getLocale());
	        LogUtil.TRAINING.debug("View id: " + model.getEmployee().getId());
	        Employee employees = manager.searchById(model.getEmployee().getId());
	        model.setEmployee(employees);
	        
			//4.กำหนดให้แสดง user transaction
			showTransaction(model.getEmployee().getTransaction());
			
		} catch (Exception e) {
			LogUtil.TRAINING.error("", e);
			//5.จัดการ exception กรณีที่มี exception เกิดขึ้นในระบบ
			manageException(conn, PF_CODE.getViewFunction(), this, e, model);
		} finally {
			getComboForAddEdit(conn);
			
			//6.Close connection หลังเลิกใช้งาน
			CCTConnectionUtil.close(conn);
		}

		return result;
	}

	@Override
	public String updateActive() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		String result = null;
	    CCTConnection conn = null;
	 
	    try {
	        //1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
	        conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());
	         
	        //2.ตรวจสอบสิทธิ์
	        result = ReturnType.DELETE.getResult();
	 
	        //3.Delete process
	        EmployeeManager manager = new EmployeeManager(conn, getUser(), getLocale());
	        manager.delete(model.getCriteria().getSelectedIds());
	 
	    } catch (Exception e) {
	        //4.จัดการ exception กรณีที่มี exception เกิดขึ้นในระบบ
	        manageException(conn, PF_CODE.getDeleteFunction(), this, e, model);
	    } finally {
	        //5.Close connection หลังเลิกใช้งาน
	        CCTConnectionUtil.close(conn);
	    }
	    return result;
	}

	@Override
	public String export() throws Exception {
		String result = null;
	    CCTConnection conn = null;
	    try {
	        //1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
	        conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());
	 
	        //2.ตรวจสอบสิทธิ์การ export
	        result = manageExport(conn, model);
	         
	        EmployeeManager manager = new EmployeeManager(conn, getUser(), getLocale());
			XSSFWorkbook wb = manager.export(model.getCriteria());        
	        
	        //3.ตัวอย่าง การ export 
	        exportExcelFile(wb, "reportACR10800310.xlsx");
	 
	    } catch (Exception e) {
	        //4.จัดการ exception กรณีที่มี exception เกิดขึ้นในระบบ
	        manageException(conn, PF_CODE.getChangeFunction(), this, e, model);
	    } finally {
	        //5.Close connection หลังเลิกใช้งาน
	        CCTConnectionUtil.close(conn);
	    }
	    return result;      //6.return "search"
	}
	
	public String report() throws Exception {
		String result = null;
		CCTConnection conn = null;
		try{
			//1.สร้าง connection โดยจะต้องระบุ lookup ที่ใช้ด้วย
			conn = new CCTConnectionProvider().getConnection(conn, DBLookup.MYSQL_TRAINING.getLookup());
			
			 //2.ตรวจสอบสิทธิ์การ export
			result = manageExport(conn, model);
	        
	        EmployeeManager manager = new EmployeeManager(conn, getUser(), getLocale());
	        
	        /*  ----------------------------------------------*/
	        HashMap<String, Object> parameterMap = new HashMap<String, Object>();			
			parameterMap.put("currentDate", new Date());
			parameterMap.put("reportName", "Test Report");
			parameterMap.put("reportDetail", "ทดสอบรายงาน");
			
//			JRDataSource jrDataSource = createDataSource(findReportDate());
			JRDataSource jrDataSource = new JRBeanCollectionDataSource(manager.ireportEmployee());
			
			// get ที่อยู่ของไฟล์  jasper
			String reportTemplateUrl = "D:/Training-2018/SIT UI Framework/Source Code/trunk/trn/report/ireport/report1.jasper";
			File rptFile = new File(reportTemplateUrl);
			
			//compile the jasperDesign
			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(rptFile.getPath());
			
			//fill the ready report with data and parameter
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap , jrDataSource);
			
			//view the report using JasperViewer
			JasperViewer.viewReport(jasperPrint,false);
			       
		}catch (Exception e) {
			throw e;
		}finally {
			CCTConnectionUtil.close(conn);
		}
		return result;
	}

	@Override
	public void showTransaction(Transaction transaction) {
		model.setTransaction(transaction);
		
	}

	@Override
	public EmployeeModel getModel() {
		return model;
	}
}
