package com.cct.trn.core.tutorial.employee.domain;

import com.cct.common.CommonDomain;

public class Employee extends CommonDomain{

	private static final long serialVersionUID = -5377136574847222390L;
		private String prefixId;
		private String fullname;
		private String nickname;
		private String sex;
		private String departmentId;
		private String positionId;
		private String startWorkDate;
		private String endWorkDate;
		private String workStatus;
		
		
		
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
		
	}
