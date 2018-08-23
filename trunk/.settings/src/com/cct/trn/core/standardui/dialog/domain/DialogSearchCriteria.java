package com.cct.trn.core.standardui.dialog.domain;

import com.cct.domain.SearchCriteria;

public class DialogSearchCriteria extends SearchCriteria{


	private static final long serialVersionUID = 1L;
	
	private String Id2;
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
	public String getId2() {
		return Id2;
	}
	public void setId2(String id2) {
		Id2 = id2;
	}

}
