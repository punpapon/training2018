package com.cct.trn.core.standardui.dialog.service;

import java.util.List;
import java.util.Locale;

import util.database.CCTConnection;

import com.cct.abstracts.AbstractService;
import com.cct.common.CommonDomain;
import com.cct.common.CommonUser;
import com.cct.trn.core.config.parameter.domain.SQLPath;
import com.cct.trn.core.standardui.dialog.domain.DialogSearchCriteria;

public class DialogService extends AbstractService{
	
	private DialogDAO dao = null;
	
	public DialogService(CCTConnection conn, CommonUser user, Locale locale) {
		super(conn, user, locale);
		this.dao = new DialogDAO();
		this.dao.setSqlPath(SQLPath.EMPLOYEE_SQL);
	}
	// service search dialog
	public List<CommonDomain> searchListCustomer(DialogSearchCriteria criteria) throws Exception {
			return dao.search(conn, criteria, user, locale);
	}
	
	
	
	

}
