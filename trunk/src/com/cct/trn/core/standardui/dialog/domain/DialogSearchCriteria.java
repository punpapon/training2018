package com.cct.trn.core.standardui.dialog.domain;

import com.cct.domain.SearchCriteria;

public class DialogSearchCriteria extends SearchCriteria{


	private static final long serialVersionUID = 1L;
	
	private String Firstname;
	private String lastName;
	
	public String getFirstname() {
		return Firstname;
	}
	public void setFirstname(String firstname) {
		Firstname = firstname;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
