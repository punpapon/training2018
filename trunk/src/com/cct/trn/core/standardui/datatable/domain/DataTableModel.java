package com.cct.trn.core.standardui.datatable.domain;

import java.util.ArrayList;
import java.util.List;

import com.cct.common.CommonModel;
import com.cct.common.CommonSelectItem;


public class DataTableModel extends CommonModel {

	private static final long serialVersionUID = -8383912104127320200L;

	 // Object สำหรับเก็บเงื่อนการค้นหา
    private DataTableSearchCriteria criteria = new DataTableSearchCriteria();
    private PopupSearchCriteria criteriaPopup = new PopupSearchCriteria();
    
 // Object สำหรับใช้ในการ add edit view
    private DataTable dataTable = new DataTable();
    
    // Object สำหรับการเก็บข้อมูล combo ที่อยู่บนหน้าจอ
    private List<CommonSelectItem> listPrefix = new ArrayList<CommonSelectItem>();// เก็บคำนำหน้าชื่อ
    private List<CommonSelectItem> listSex = new ArrayList<CommonSelectItem>();// เก็บเพศ
    private List<CommonSelectItem> listWorkStatus = new ArrayList<CommonSelectItem>();// เก็บสถานะการทำงาน
    
    // Object List Result Search
    private List<DataTableSearch> listResult = new ArrayList<DataTableSearch>();
    
	public DataTableSearchCriteria getCriteria() {
		return criteria;
	}
	public void setCriteria(DataTableSearchCriteria criteria) {
		this.criteria = criteria;
	}
	public DataTable getDataTable() {
		return dataTable;
	}
	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}
	public List<CommonSelectItem> getListPrefix() {
		return listPrefix;
	}
	public void setListPrefix(List<CommonSelectItem> listPrefix) {
		this.listPrefix = listPrefix;
	}
	public List<CommonSelectItem> getListSex() {
		return listSex;
	}
	public void setListSex(List<CommonSelectItem> listSex) {
		this.listSex = listSex;
	}
	public List<CommonSelectItem> getListWorkStatus() {
		return listWorkStatus;
	}
	public void setListWorkStatus(List<CommonSelectItem> listWorkStatus) {
		this.listWorkStatus = listWorkStatus;
	}
	public List<DataTableSearch> getListResult() {
		return listResult;
	}
	public void setListResult(List<DataTableSearch> listResult) {
		this.listResult = listResult;
	}
	public PopupSearchCriteria getCriteriaPopup() {
		return criteriaPopup;
	}
	public void setCriteriaPopup(PopupSearchCriteria criteriaPopup) {
		this.criteriaPopup = criteriaPopup;
	}
}
