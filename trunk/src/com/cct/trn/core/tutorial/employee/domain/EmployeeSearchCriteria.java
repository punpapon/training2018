package com.cct.trn.core.tutorial.employee.domain;

import com.cct.domain.HeaderSorts;
import com.cct.domain.SearchCriteria;

public class EmployeeSearchCriteria extends SearchCriteria{

	private static final long serialVersionUID = 1L;

	private String prefixId;
	private String fullname;
	private String nickname;
	private String sex;
	private String departmentId;
	private String departmentDesc;
	private String positionId;
	private String positionDesc;
	private String startWorkDate;
	private String endWorkDate;
	private String workStatus;
	
	public EmployeeSearchCriteria() {
		HeaderSorts[] arrayHeaderSorts = {
				new HeaderSorts("", HeaderSorts.ASC, "0")
				, new HeaderSorts("", HeaderSorts.ASC, "0")
				, new HeaderSorts("", HeaderSorts.ASC, "0")
				, new HeaderSorts("", HeaderSorts.ASC, "0")
				, new HeaderSorts("PRE.PREFIX_ID", HeaderSorts.ASC, "0")
				, new HeaderSorts("NAME", HeaderSorts.ASC, "0")
				, new HeaderSorts("EMP.SEX", HeaderSorts.ASC, "0")
				, new HeaderSorts("DEP.DEPARTMENT_ID", HeaderSorts.ASC, "0")
				, new HeaderSorts("POS.POSITION_ID", HeaderSorts.ASC, "0")
		};
		setHeaderSorts(arrayHeaderSorts);
		setHeaderSortsSize(arrayHeaderSorts.length);
	}

	public void setDefaultHeaderSorts() {
		for (int i = 0; i < getHeaderSortsSize(); i++) {
			getHeaderSorts()[0].setOrder(HeaderSorts.ASC);
			getHeaderSorts()[i].setSeq("");
		}
		getHeaderSorts()[0].setSeq("0");
		setHeaderSortsSelect("4");
	}
	
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
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
	
}
