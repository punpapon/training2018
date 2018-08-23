package com.cct.trn.core.standardui.dialog.service;

import java.util.List;
import java.util.Locale;

import util.database.CCTConnection;

import com.cct.abstracts.AbstractManager;
import com.cct.common.CommonDomain;
import com.cct.common.CommonUser;
import com.cct.trn.core.standardui.dialog.domain.Dialog;
import com.cct.trn.core.standardui.dialog.domain.DialogSearch;
import com.cct.trn.core.standardui.dialog.domain.DialogSearchCriteria;

public class DialogManager extends AbstractManager<DialogSearchCriteria, DialogSearch, Dialog, CommonUser, Locale>{

	public DialogManager(CCTConnection conn, CommonUser user, Locale locale) {
		super(conn, user, locale);
		this.service = new DialogService(conn, user, locale);
	}

	private DialogService service = null;

	public List<CommonDomain> searchListCustomer(DialogSearchCriteria criteria) throws Exception {	
		return service.searchListCustomer(criteria);
	}
	
	public List<CommonDomain> searchDetail(DialogSearchCriteria criteria) throws Exception {
		return service.searchListCustomer(criteria);
	}
	@Override
	public List<DialogSearch> search(DialogSearchCriteria criteria)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dialog searchById(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int add(Dialog obj) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int edit(Dialog obj) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateActive(String ids, String activeFlag) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	


	
}
