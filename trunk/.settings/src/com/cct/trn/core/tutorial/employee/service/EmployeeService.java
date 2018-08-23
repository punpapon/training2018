package com.cct.trn.core.tutorial.employee.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.database.CCTConnection;
import util.log.LogUtil;
import util.report.ManageDataPOI;
import util.report.ReportPOIManagerUtil;

import com.aspose.cells.PageSetup;
import com.aspose.cells.Worksheet;
import com.cct.abstracts.AbstractService;
import com.cct.common.CommonUser;
import com.cct.trn.core.config.parameter.domain.SQLPath;
import com.cct.trn.core.tutorial.employee.domain.Employee;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearch;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearchCriteria;
import com.cct.trn.core.tutorial.employee.domain.Person;
import com.sun.glass.ui.Pixels.Format;


public class EmployeeService extends AbstractService{

	private EmployeeDAO dao = null;
	
	public EmployeeService(CCTConnection conn, CommonUser user, Locale locale) {
		super(conn, user, locale);
		this.dao = new EmployeeDAO();
		this.dao.setSqlPath(SQLPath.EMPLOYEE_SQL);

	}
	
	//service ของการนับข้อมูล
	protected long countData(EmployeeSearchCriteria criteria) throws Exception {
		try {
			return dao.countData(conn, criteria, user, locale);
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
	
	protected Employee searchById(CCTConnection conn,String Id) throws Exception{
	    try {
	    	// ค้นหาข้อมูลโดยระบุ id
	        return dao.searchById(conn, Id, user, locale);
	    } catch (Exception e) {
	        throw e;
	    }
	}
	
	/* ตรวจสอบบันทึกข้อมูลโครงการซ้ำ  */
	protected boolean checkDup(CCTConnection conn, Employee obj) throws Exception{
	    try {
	    	// ตรวจสอบบันทึกข้อมูลโครงการซ้ำ
	        return  dao.checkDup(conn, obj, user, locale);
	    } catch (Exception e) {
	        throw e;
	    }
	}
	
	/* เพิ่มข้อมูล  */
	protected int addEmployee(CCTConnection conn, Employee obj) throws Exception{
	    try {
	        // เพิ่มข้อมูลผู้ใช้งาน
	        return dao.add(conn, obj, user, locale);
	 
	    } catch (Exception e) {
	        throw e;
	    }
	}
	
	/* แก้ไขข้อมูล  */
	protected int edit(Employee obj) throws Exception {
		try {
			// แก้ไขข้อมูลโครงการ  
	        return dao.edit(conn, obj, user, locale);
	    } catch (Exception e) {
	        throw e;
	    }
	}
	
	protected int delete(CCTConnection conn , String ids) throws Exception {
		 try {
		        return dao.delete(conn, ids, user, locale);
		    } catch (Exception e) {
		        LogUtil.SEC.error("", e);
		        throw e;
		    }
	}
	/* กำหนดวันที่ปัจจุบัน */
	protected String convertDate(Employee obj, int i) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		String dat = dtf.format(localDate);
		return dat;
	}
	
	protected String DateNow() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		
		String dat = dtf.format(localDate);
		return dat;
	}
	protected String TimeNow() {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(cal.getTime());
		return time;
	}
	
	protected Collection<Person> ireportEmployee(CCTConnection conn) throws Exception {
		  Collection<Person> listPerson;
		  
		  try {
			  listPerson = dao.ireportEmployee(conn);
		  } catch(Exception e) {
			  LogUtil.SEC.error("",e);
			  throw e;
		  }
			return listPerson;
	}
	
	
	protected XSSFWorkbook export(CCTConnection conn, EmployeeSearchCriteria criteria) throws Exception {
		List<EmployeeSearch> listResult;
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		PrintSetup ps = sheet.getPrintSetup();
		
		XSSFCell cell;
		XSSFRow row;
		
		try {
			 	listResult = dao.searchExport(conn, criteria, user, locale );	
			 	
			 	// Setting Pattern Table
			 	sheet.getPrintSetup().setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);
				//sheet.getPrintSetup().setLandscape(true);
				sheet.setAutobreaks(true);
				ps.setFitHeight((short) 1);
				ps.setFitHeight((short) 1);
				sheet.setFitToPage(true);
			 	sheet.setColumnWidth(0, 8*255);
			 	sheet.setColumnWidth(1, 25*255);
			 	sheet.setColumnWidth(2, 10*255);
			 	sheet.setColumnWidth(3, 15*255);
			 	sheet.setColumnWidth(4, 15*255);
			 	sheet.setColumnWidth(5, 15*255);
			 	sheet.setColumnWidth(6, 15*255);
			 	sheet.setColumnWidth(7, 14*255);
			 	sheet.setColumnWidth(8, 14*255);
			 	sheet.setColumnWidth(9, 14*255);
			 	sheet.setColumnWidth(10, 14*255);
			 	
			 	/*************************** Header ************************************/
			 	Header header = (Header) sheet.getOddHeader();
			 	 header.setCenter("&B รายงานข้อมูลพนักงาน");
			 	 header.setRight("หน้า &P / &N");
			 	 
			 	 Footer footer = (Footer) sheet.getFooter();
			 	  footer.setLeft("REP08260001");
			 	  footer.setRight("ชื่อผู้พิมพ์ " + user.getUserName());
			 	  
			 	 /*********************************************************************/
			 	 int cs = 0;  // ลำดับแถว
			 	// หัวตาราง
			 	row = sheet.createRow((short) cs++);
			 	row.setHeightInPoints(30);
			 	cell = row.createCell((short)0);
			 	cell.setCellValue("รายงานข้อมูลพนักงาน");
			 	cell.setCellStyle(ReportPOIManagerUtil.createHeaderStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,	10 ));			 	
			 	
			 	// ข้อมูลหัวตาราง
			 	row = sheet.createRow((short) cs++);    // Row 1
			 	row.setHeightInPoints(24);
			 	cell = row.createCell((short) 1);
			 	cell.setCellValue("คำนำหน้าชื่อ");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14Format(wb, "TH", 0, 0, 0, 0));
			 	cell = row.createCell((short) 2);
			 	cell.setCellValue(ManageDataPOI.checkNullCriteria(criteria.getPrefixId()));
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(1, 1, 2,	4 ));
			 	cell = row.createCell((short) 5);
			 	cell.setCellValue("ชื่อ-สกุล");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14Format(wb, "TH", 0, 0, 0, 0));
			 	cell = row.createCell((short) 6);
			 	cell.setCellValue(ManageDataPOI.checkNullCriteria(criteria.getFullname()));
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(1, 1, 6,	8 ));
			 	
			 	row = sheet.createRow((short) cs++);	// Row 2
			 	row.setHeightInPoints(24);
			 	cell = row.createCell((short) 1);
			 	cell.setCellValue("ชื่อเล่น");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14Format(wb, "TH", 0, 0, 0, 0));
			 	cell = row.createCell((short) 2);
			 	cell.setCellValue(ManageDataPOI.checkNullCriteria(criteria.getNickname()));
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(2, 2, 2,	4 ));
			 	cell = row.createCell((short) 5);
			 	cell.setCellValue("เพศ");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14Format(wb, "TH", 0, 0, 0, 0));
			 	cell = row.createCell((short) 6);
			 	cell.setCellValue(ManageDataPOI.checkNullCriteria(criteria.getSex()));
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(2, 2, 6,	8 ));
			 	
			 	row = sheet.createRow((short) cs++);	// Row 3
			 	row.setHeightInPoints(24);
			 	cell = row.createCell((short) 1);
			 	cell.setCellValue("สังกัด");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14Format(wb, "TH", 0, 0, 0, 0));
			 	cell = row.createCell((short) 2);
			 	cell.setCellValue(ManageDataPOI.checkNullCriteria(criteria.getDepartmentDesc()));
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(3, 3, 2,	4 ));
			 	cell = row.createCell((short) 5);
			 	cell.setCellValue("แผนก");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14Format(wb, "TH", 0, 0, 0, 0));
			 	cell = row.createCell((short)6);
			 	cell.setCellValue(ManageDataPOI.checkNullCriteria(criteria.getPositionDesc()));
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(3, 3, 6,	8 ));
			 	
			 	row = sheet.createRow((short) cs++);	// Row 4
			 	row.setHeightInPoints(24);
			 	cell = row.createCell((short) 1);
			 	cell.setCellValue("ช่วงเริ่มงานตั้งแต่");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14Format(wb, "TH", 0, 0, 0, 0));
			 	cell = row.createCell((short) 2);
			 	cell.setCellValue(ManageDataPOI.checkNullCriteria(criteria.getStartWorkDate()));
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(4, 4, 2,	4 ));
			 	cell = row.createCell((short) 5);
			 	cell.setCellValue("ถึง");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14Format(wb, "TH", 0, 0, 0, 0));
			 	cell = row.createCell((short)6);
			 	cell.setCellValue(ManageDataPOI.checkNullCriteria(criteria.getEndWorkDate()));
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(4, 4, 6,	8 ));
			 	
			 	row = sheet.createRow((short) cs++);	// Row 5
			 	row.setHeightInPoints(24);
			 	cell = row.createCell((short) 1);
			 	cell.setCellValue("สถานะการทำงาน");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14Format(wb, "TH", 0, 0, 0, 0));
			 	cell = row.createCell((short) 2);
			 	cell.setCellValue(ManageDataPOI.checkNullCriteria(criteria.getWorkStatus()));
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	sheet.addMergedRegion(new CellRangeAddress(5, 5, 2,	4 ));
			 				 						    
			 	row = sheet.createRow((short) cs++);	// Row 6
			 	row.setHeightInPoints(22);
			 	cell = row.createCell((short) 0);			  	
			 	cell.setCellValue("วันที่พิมพ์  " + DateNow() + "  เวลา " + TimeNow() + " น.");
			 	cell.setCellStyle(ReportPOIManagerUtil.createFontLN14(wb));
			 				 	 			 	
			 	cell = row.createCell((short) 10);
			 	cell.setCellValue("Page 1 of 1");
			 	cell.setCellStyle(ReportPOIManagerUtil.createCriteriaValueStyle(wb));
			 	
			 	/***************************** BODY **********************************/
			 	// head body
			 	row = sheet.createRow((short) cs++);
			 	row.setHeightInPoints(22);
			 	cell = row.createCell((short) 0);
			 	cell.setCellValue("ลำดับ");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	sheet.addMergedRegion(new CellRangeAddress(7 , 8 , 0 , 0));
			 	cell = row.createCell((short) 1);
			 	cell.setCellValue("ชื่อสกุล");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	sheet.addMergedRegion(new CellRangeAddress(7 , 8 , 1 , 1));
			 	cell = row.createCell((short) 2);
			 	cell.setCellValue("เพศ");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	sheet.addMergedRegion(new CellRangeAddress(7 , 8 , 2 , 2));
			 	cell = row.createCell((short) 3);
			 	cell.setCellValue("วันที่บันทึกข้อมูล");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	sheet.addMergedRegion(new CellRangeAddress(7 , 8 , 3 , 3));
			 	cell = row.createCell((short) 4);
			 	cell.setCellValue("ผู้บันทึก");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	sheet.addMergedRegion(new CellRangeAddress(7 , 8 , 4 , 4));
			 	cell = row.createCell((short) 5);
			 	cell.setCellValue("วันที่แก้ไขข้อมูล");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	sheet.addMergedRegion(new CellRangeAddress(7 , 8 , 5 , 5));
			 	cell = row.createCell((short) 6);
			 	cell.setCellValue("ผู้แก้ไข");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	sheet.addMergedRegion(new CellRangeAddress(7 , 8 , 6 , 6));
			 	cell = row.createCell((short) 7);
			 	cell.setCellValue("สถานะพนักงาน");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	sheet.addMergedRegion(new CellRangeAddress(7 , 7 , 7 , 10));
			 	cell = row.createCell((short) 8);
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	cell = row.createCell((short) 9);
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	cell = row.createCell((short) 10);
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	
			 	row = sheet.createRow((short) cs++);
			 	row.setHeightInPoints(45);
			 	for(int i=0;i<7;i++) {
			 		cell = row.createCell((short) i);
			 		cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	}
			 	cell = row.createCell((short) 7);
			 	cell.setCellValue("สถานะ");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	cell = row.createCell((short) 8);
			 	cell.setCellValue("วันที่เริ่มงาน");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	cell = row.createCell((short) 9);
			 	cell.setCellValue("วันสุดท้ายที่ \n ทำงาน");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	cell = row.createCell((short) 10);
			 	cell.setCellValue("หมายเหตุ");
			 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCBW14(wb, 1, 1, 1, 1));
			 	
			 	// ตัวเช็คสถานะ สังกัดและแผนก
			 	Boolean chkRowCS = false;
			 	Boolean chkRowSC = false;
			 	Boolean chkRowPG = false;
			 	Boolean chkDescSIT = false;
			 	Boolean chkDescCOM = false;
			 	
			 	// แถว
			 	int num1 = 0;
			 	int num2 = 0;
			 	int num3 = 0;
			 	int pg = 0;
			 	int sd = 0;
			 	int csit = 0;
			 	
			 	for(EmployeeSearch semp : listResult){
			 		if(semp.getPositionDesc().equals("Programer")){
			 			num1 = num1 +1;
			 			pg = num1;
			 		}
			 		else if(semp.getPositionDesc().equals("System Design")) {
			 			num2 = num2 +1;
			 			sd = num2;
			 		} 			 		
			 		else if(semp.getPositionDesc().equals("Call Center")) {
			 			num3 = num3 +1;
			 			csit = num3;
			 		} 				 		
			 	}
			 	 num1 = ManageDataPOI.checknumber1(num1);	// ตรวจสอบไม่ให้ข้อมูลทีค่าเป็น 0 	
			 	 num2 = ManageDataPOI.checknumber2(num2);	// ตรวจสอบไม่ให้ข้อมูลทีค่าเป็น 0
			 	 num3 = ManageDataPOI.checknumber3(num3);	// ตรวจสอบไม่ให้ข้อมูลทีค่าเป็น 0
			 	 
			 	// นำข้อมูลใส่ลงในตาราง			 				 	
			 	int no = 1;		 	
			 	for(EmployeeSearch semp : listResult){
			 		/********************** ตั้งค่าข้อมูล *******************************/
			 		if(semp.getSex().equals("M")) {
			 			semp.setSex("ชาย");
			 		}
			 		if(semp.getSex().equals("F")){
			 			semp.setSex("หญิง");
			 		}
			 		if(semp.getWorkStatus().equals("C")) {
			 			semp.setWorkStatus("พนักงานประจำ");
			 		}
			 		if(semp.getWorkStatus().equals("R")) {
			 			semp.setWorkStatus("อดีตพนักงาน");
			 		}
			 		 	
			 		/***************************************** COMMAND CONTROL ***********************************************/
			 		if(semp.getDepartmentDesc().equals("COMMAND CONTROL")){
			 			if(!chkDescCOM){
			 				row = sheet.createRow((short) cs);
						 	row.setHeightInPoints(22);
						 	sheet.addMergedRegion(new CellRangeAddress(cs , cs , 0 , 10));
						 	cs++;
						 	for(int c=0;c<11;c++) {
						 		cell = row.createCell((short) c);	 	
						 		cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
						 	}
						 	cell = row.createCell((short) 0);
						 	cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
						 	cell.setCellValue("สังกัด : " + "โสมาภา อินฟอร์เมชั่น เทคโนโลยี");
						 	chkDescCOM = true;
			 			}
			 		}
			 		/************************************** COMMAND CONTROL PROGRAMER ******************************************/
			 		if(semp.getDepartmentDesc().equals("COMMAND CONTROL") && semp.getPositionDesc().equals("Programer")){
			 			if(!chkRowPG){
			 				row = sheet.createRow((short) cs);
						 	row.setHeightInPoints(22);
						 	sheet.addMergedRegion(new CellRangeAddress(cs ,cs , 0 , 10));
						 	cs++;
						 	for(int c=0;c<11;c++) {
						 		cell = row.createCell((short) c);	 	
						 		cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
						 	}
						 	cell = row.createCell((short) 0);	
						 	cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
						 	cell.setCellValue("แผนก : " + "โปรแกรมเมอร์");
			 				chkRowPG = true;
			 			}
			 			row = sheet.createRow((short) cs++);
			 			row.setHeightInPoints(22);
			 			num1--;
			 			num2 = ManageDataPOI.checknumber2(num2);
			 			num3 = ManageDataPOI.checknumber3(num3); 
			 		}
			 		/************************************** COMMAND CONTROL SYSTEMDESIGN ******************************************/
			 		else if(semp.getDepartmentDesc().equals("COMMAND CONTROL") && semp.getPositionDesc().equals("System Design")){
			 			if(!chkRowCS){
			 				row = sheet.createRow((short) cs);
						 	row.setHeightInPoints(22);
						 	sheet.addMergedRegion(new CellRangeAddress(cs ,cs , 0 , 10));
						 	cs++;
						 	for(int c=0;c<11;c++) {
						 		cell = row.createCell((short) c);	 	
						 		cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1)); 
						 	cell = row.createCell((short) 0);						 	
						 	cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
						 	cell.setCellValue("แผนก : " + "ซอฟต์แวร์ดีไซน์");
						 	chkRowCS = true;						 							 	
						 	}
			 			}
			 			row = sheet.createRow((short) cs++);
			 			row.setHeightInPoints(22);		 			
			 			num2--;	 		
			 			num1 = ManageDataPOI.checknumber1(num1);
			 			num3 = ManageDataPOI.checknumber3(num3);
			 		}
			 		/************************************************ SOMAPAIT *********************************************/
			 		if(semp.getDepartmentDesc().equals("SOMAPA IT")) {
			 			if(!chkDescSIT) {
			 				row = sheet.createRow((short) cs);
						 	row.setHeightInPoints(22);				 	
						 	sheet.addMergedRegion(new CellRangeAddress(cs ,cs , 0 , 10));
						 	cs++;
						 	for(int c=0;c<11;c++) {
						 		cell = row.createCell((short) c);	 	
						 		cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
						 	}
						 	cell = row.createCell((short) 0);
						 	cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
						 	cell.setCellValue("สังกัด : " + "คอมมาน คอนโทรล");
						 	chkDescSIT = true;
			 			}
			 		}
			 		if(semp.getDepartmentDesc().equals("SOMAPA IT") && semp.getPositionDesc().equals("Call Center")){		 		
			 			if(!chkRowSC){
			 				row = sheet.createRow((short) cs);
						 	row.setHeightInPoints(22);
						 	sheet.addMergedRegion(new CellRangeAddress(cs ,cs , 0 , 10));
						 	cs++;
						 	for(int c=0;c<11;c++) {
						 		cell = row.createCell((short) c);	 	
						 		cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
						 	}
						 	cell = row.createCell((short) 0);
						 	cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
						 	cell.setCellValue("แผนก : " + "CSIT");
						 	chkRowSC = true;
			 			}
			 			row = sheet.createRow((short) cs++);
			 			row.setHeightInPoints(22);
			 			num3--;
			 			num1 = ManageDataPOI.checknumber1(num1);
			 			num2 = ManageDataPOI.checknumber2(num2);
			 		}
			 		/*else {
			 			row = sheet.createRow((short) cs++);
			 		}*/
			 		
			 		int i = 0;	
			 		cell = row.createCell((short)i++);
				 	cell.setCellValue(no++);
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCB14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(semp.getFullname()));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(semp.getSex()));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(ManageDataPOI.convertDate(semp.getTransaction().getCreateDate())));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(semp.getTransaction().getCreateUser()));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(ManageDataPOI.convertDate(semp.getTransaction().getUpdateDate())));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(semp.getTransaction().getUpdateUser()));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(semp.getWorkStatus()));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(ManageDataPOI.convertDate(semp.getStartWorkDate())));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(ManageDataPOI.convertDate(semp.getEndWorkDate())));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	cell = row.createCell((short)i++);
				 	cell.setCellValue(ManageDataPOI.checkNullCriteria(semp.getTransaction().getCreateRemark()));
				 	cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 0, 0, 1, 1));
				 	
				 	if(num1==0 || num2==0 || num3==0) {
				 		String positionName = "";
				 		int countPosition =0 ;;
				 		if(num1==0){
				 			positionName = "โปรแกรมเมอร์";
				 			countPosition = pg;
				 			no = 1;
				 		}
				 		else if(num2==0){
				 			positionName = "ซอฟต์แวร์ดีไซน์";
				 			countPosition = sd;
				 			no = 1;
				 		}
				 		else if(num3==0){
				 			positionName = "Call Center";
				 			countPosition = csit;
				 			no = 1;
				 		}
			 			row = sheet.createRow((short) cs);
			 			row.setHeightInPoints(22);
			 			sheet.addMergedRegion(new CellRangeAddress(cs ,cs , 0 , 8));
			 			cs++;
			 			for(int c=0;c<9;c++) {
					 		cell = row.createCell((short) c);	 	
					 		cell.setCellStyle(ReportPOIManagerUtil.createStyleCN14(wb, 1, 1, 1, 1));
					 	}
				 		cell = row.createCell((short) 0);				 		
				 		cell.setCellValue("รวมแผนก : " + positionName +"  จำนวน");
				 		cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14(wb, 1, 1, 1, 1));
				 		cell = row.createCell((short) 9);
				 		cell.setCellValue("" + countPosition);
				 		cell.setCellStyle(ReportPOIManagerUtil.createStyleRB14(wb, 1, 1, 1, 1));
				 		cell = row.createCell((short) 10);
				 		cell.setCellValue("(คน)");
				 		cell.setCellStyle(ReportPOIManagerUtil.createStyleLB14(wb, 1, 1, 1, 1));
			 		}
				 	
										 						 					 	
				 	System.out.println("Name : " + semp.getFullname());
					System.out.println("Sex : " + semp.getSex());
					System.out.println("Department : " + semp.getDepartmentDesc());
					System.out.println("Position : " + semp.getPositionDesc());
					System.out.println("StartDate : " + semp.getStartWorkDate());
					System.out.println("EndDate : " + semp.getEndWorkDate());
					System.out.println("WorkStatus : " + semp.getWorkStatus());
					System.out.println("Remark : " + semp.getRemark());	
					System.out.println("Remark : " + semp.getTransaction().getCreateUser());	
					System.out.println("================================");										
			 	}
			 	
		  } catch(Exception e) {
			  LogUtil.SEC.error("",e);
			  throw e;
		  }
		return wb;
	}
}

