package com.cct.trn.core.standardui.dialog.domain;

import com.cct.common.CommonModel;

public class DialogModel extends CommonModel {

	private static final long serialVersionUID = -8383912104127320200L;

	private DialogSearchCriteria criteriaPopup = new DialogSearchCriteria();

	public DialogSearchCriteria getCriteriaPopup() {
		return criteriaPopup;
	}

	public void setCriteriaPopup(DialogSearchCriteria criteriaPopup) {
		this.criteriaPopup = criteriaPopup;
	}
	
}
