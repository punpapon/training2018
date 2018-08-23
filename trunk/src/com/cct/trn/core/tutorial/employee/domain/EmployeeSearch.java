package com.cct.trn.core.tutorial.employee.domain;

import com.cct.common.CommonDomain;
import com.cct.domain.Transaction;

public class EmployeeSearch extends CommonDomain{

	private static final long serialVersionUID = 1L;
	
//	SELECT EMP.EMPLOYEE_ID
	
	 private String fullname;
	 private String sex;
	 private String departmentDesc;
	 private String positionDesc;
	 private String startWorkDate;
	 private String endWorkDate;
	 private String workStatus;
	 private String remark;
	 
	 
//	, PRE.PREFIX_ID
//	, PRE.PREFIX_NAME
//	, CONCAT(NAME, ' ', SURNAME, ' (', NICK_NAME , ')') AS FULLNAME
//	, EMP.SEX
//	, DEP.DEPARTMENT_ID
//	, DEP.DEPARTMENT_NAME
//	, POS.POSITION_ID
//	, POS.POSITION_NAME
//	, EMP.START_WORK_DATE
//	, EMP.END_WORK_DATE
//	, EMP.WORK_STATUS
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDepartmentDesc() {
		return departmentDesc;
	}
	public void setDepartmentDesc(String departmentDesc) {
		this.departmentDesc = departmentDesc;
	}
	public String getPositionDesc() {
		return positionDesc;
	}
	public void setPositionDesc(String positionDesc) {
		this.positionDesc = positionDesc;
	}
	public String getStartWorkDate() {
		return startWorkDate;
	}
	public void setStartWorkDate(String startWorkDate) {
		this.startWorkDate = startWorkDate;
	}
	public String getEndWorkDate() {
		return endWorkDate;
	}
	public void setEndWorkDate(String endWorkDate) {
		this.endWorkDate = endWorkDate;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
