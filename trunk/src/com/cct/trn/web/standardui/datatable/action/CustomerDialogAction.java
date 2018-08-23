package com.cct.trn.web.standardui.datatable.action;

import java.util.List;

import org.apache.log4j.Logger;

import util.database.CCTConnection;
import util.log.LogUtil;

import com.cct.common.CommonDialogAction;
import com.cct.common.CommonDomain;
import com.cct.domain.SearchCriteria;
import com.cct.trn.core.standardui.dialog.domain.DialogModel;
import com.cct.trn.core.standardui.dialog.domain.DialogSearchCriteria;
import com.cct.trn.core.standardui.dialog.service.DialogManager;

public class CustomerDialogAction extends CommonDialogAction{

	private static final long serialVersionUID = -2560262208110737222L;

	private DialogModel model = new DialogModel();
	
	@Override
	public List<CommonDomain> search(CCTConnection conn) throws Exception {
		return new DialogManager(conn, getUser(), getLocale()).searchListCustomer((DialogSearchCriteria)getModel().getCriteriaPopup());
	}
	
	@Override
	public List<CommonDomain> searchById(CCTConnection conn, String id)	throws Exception {
		// TODO Auto-generated method stub
		return super.searchById(conn, id);
	}
	
	public DialogModel getModel() {
		return model;
	}
	
	@Override
	protected Logger loggerInititial() {
		return LogUtil.DIALOG;
	}
	
	@Override
	public SearchCriteria initSearchCriteria() {
		// TODO Auto-generated method stub
		return new DialogSearchCriteria();
	}
}
