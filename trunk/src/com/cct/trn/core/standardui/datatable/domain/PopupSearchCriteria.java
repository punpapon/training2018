package com.cct.trn.core.standardui.datatable.domain;

import com.cct.domain.SearchCriteria;

public class PopupSearchCriteria extends SearchCriteria{
	
	private static final long serialVersionUID = 1L;
	
	private String ids;
	private String firstName;
	private String lastName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getId() {
		return ids;
	}
	public void setId(String ids) {
		this.ids = ids;
	}
}
