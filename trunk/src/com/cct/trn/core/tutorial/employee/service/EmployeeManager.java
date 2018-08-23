package com.cct.trn.core.tutorial.employee.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.database.CCTConnection;
import util.log.LogUtil;

import com.cct.abstracts.AbstractManager;
import com.cct.common.CommonUser;
import com.cct.exception.DuplicateException;
import com.cct.exception.MaxExceedException;
import com.cct.trn.core.config.parameter.domain.ParameterConfig;
import com.cct.trn.core.tutorial.employee.domain.Employee;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearch;
import com.cct.trn.core.tutorial.employee.domain.EmployeeSearchCriteria;
import com.cct.trn.core.tutorial.employee.domain.Person;

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
		criteria.setTotalResult(service.countData(criteria));
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
		return service.searchById(conn, id);
	}

	@Override
	public int add(Employee obj) throws Exception {
		try {
	        
	        //1.Begin transaction
	        conn.setAutoCommit(false);
	        
	        //2.ตรวจสอบบันทึกข้อมูลผู้ใช้ซ้ำ
	        if (service.checkDup(conn, obj)) {
	            throw new DuplicateException(); //Throw DuplicateException ถ้าพบว่าข้อมูลซ้ำ
	        }
	        
	        //3.เพิ่มข้อมูลผู้ใช้งาน
			service.addEmployee(conn, obj);
			
			//รอดำเนินการ. ...การเพิ่มข้อมูล  path
			
			//4. Commit transaction
	        conn.commit();
	 
	    } catch (Exception e) {
	    	//5. Rollback transaction เมื่อเกิด Error
	        conn.rollback();
	        throw e;
	    } finally {
	        //6. Set AutoCommit กลับคืนเป็น True
	        conn.setAutoCommit(true);
	    }
	    return 0;
	}

	@Override
	public int edit(Employee obj) throws Exception {
		try {
	        //1.ตรวจสอบบันทึกข้อมูลผู้ใช้ซ้ำ
			if (service.checkDup(conn, obj)) {
	            throw new DuplicateException(); //Throw DuplicateException ถ้าพบว่าข้อมูลซ้ำ
	        }
	 
	        //2.แก้ไขข้อมูลผู้ใช้งาน
	        service.edit(obj);
	 
	 
	    } catch (Exception e) {
	        throw e;
	    }
	    return 0;
	}

	
	
	@Override
	public int delete(String ids) throws Exception {		
		return service.delete(conn , ids);
	}

	@Override
	public int updateActive(String ids, String activeFlag) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Employee defaultValue(){
		  Employee emp = new Employee();
		  try {
		         emp.setSex("M");
		         emp.setStartWorkDate(service.convertDate(null, 6));
		         emp.setWorkStatus("T");
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		  return emp;
		 }

	
	public XSSFWorkbook export(EmployeeSearchCriteria criteria)
			throws Exception {
		//List<EmployeeSearch> listResult;
		XSSFWorkbook workbook;
		try{
			//นับจำนวนรายการที่ค้นพบ
		//criteria.setTotalResult(service.countData(criteria));
	//	LogUtil.SEC.debug("Count data[" +criteria.getTotalResult() + "]record.");
		
			//ตรวจสอบจำนวนที่ค้นพบเกินจำนวนที่ระบบกำหนดหรือไม่
		if( (criteria.isCheckMaxExceed()) && (criteria.getTotalResult() > ParameterConfig.getParameter().getApplication().getMaxExceed())) {
				//ถ้าใช่ให้ confirm เพื่อยืนยันการค้นหา
			throw new MaxExceedException();
		}
		else {
				//ถ้าไม่ใช่ให้ค้นหาข้อมูล
			//listResult = service.search(conn, criteria);
			workbook = service.export(conn, criteria);
			
		}
		} catch(Exception e) {
			throw e;
		}
		return workbook;
	}
	
	public Collection ireportEmployee() throws Exception {
		Collection<Person> listPerson ;
		try{
			listPerson = service.ireportEmployee(conn);
					
		}catch(Exception e){
			throw e;
		}
		return listPerson;
	}
	
	public static Collection listData() {
		ArrayList<Person> data = new ArrayList<Person>();
		Person p1 = new Person();
		p1.setName("Saranyu");
		p1.setLastName("S");
		
		p1.setDepartmentId("SD");
		data.add(p1);
		
		p1 = new Person();
		p1.setName("Saranyu");
		p1.setLastName("S");
		
		p1.setDepartmentId("SD");
		data.add(p1);
		
		p1 = new Person();
		p1.setName("Saranyu");
		p1.setLastName("S");
		
		p1.setDepartmentId("SD");
		data.add(p1);
		
		Person p2 = new Person();
		p2.setName("Konchawan");
		p2.setLastName("S");
		
		p2.setDepartmentId("PG");
		data.add(p2);
		
		p2 = new Person();
		p2.setName("Konchawan");
		
		p2.setDepartmentId("PG");
		data.add(p2);
		
		p2 = new Person();
		p2.setName("Konchawan");
		p2.setLastName("S");
		
		p2.setDepartmentId("SD");
		data.add(p2);
		
		return data;
	}
}
