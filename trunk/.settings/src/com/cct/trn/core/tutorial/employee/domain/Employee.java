package com.cct.trn.core.tutorial.employee.domain;

import com.cct.common.CommonDomain;

public class Employee extends CommonDomain{

	private static final long serialVersionUID = -5377136574847222390L;
		private String employeeId;
		private String prefixId;
		private String prefixName;
		private String name;
		private String lastName;
		private String fullName;
		private String nickName;
		private String sex;
		private String departmentId;
		private String departmentDesc;
		private String positionId;
		private String positionDesc;
		private String startWorkDate;
		private String endWorkDate;
		private String workStatus;
		private String remark;
		
		public String getPrefixId() {
			return prefixId;
		}
		public void setPrefixId(String prefixId) {
			this.prefixId = prefixId;
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
		public String getEmployeeId() {
			return employeeId;
		}
		public void setEmployeeId(String employeeId) {
			this.employeeId = employeeId;
		}
		
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getFullName() {
			return fullName;
		}
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
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
		public String getPrefixName() {
			return prefixName;
		}
		public void setPrefixName(String prefixName) {
			this.prefixName = prefixName;
		}		
	}
