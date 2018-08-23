<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<head>
<script type="text/javascript">
	function initDialog() {
	    jQuery('div'+ divPopup +' #criteriaIds').val(ids);
	     
	    document.getElementsByName('criteriaPopup.criteriaKey')[0].value = '';
	    // ...
	     
	    // เรียกใช้งาน function สำหรับการ clear หน้าจอ
	    clearDialogWindows ('popupResult');
	     
	    jQuery.ajax({
	        type : "POST",
	        url : "/training2018/jsp/standardui/initViewDialog.action",
	        data : null,
	        async : false,
	        success : function(data) {
	        	initialCriteria(data.criteriaPopup);
	           /*  if(data != null){
	            	initialCriteria(criteriaPopup);
	            	renderData("popupResult", data.lstResult, data.messagePopup, data.criteriaPopup, true);	            
	            }
	            else {
	            	alert('ไม่พบข้อมูล');
	            } */
	        }
	    });
	}
 
	function searchPopupCus() {
	    jQuery('div'+ divPopup +' #criteriaPopupStart').val(1); // ทำการกำหนดค่า start = 1 เสมอ เมื่อมีการ click ปุ่มค้นหา 
	    reSearchPopupCus();
	}
	 
	function reSearchPopupCus() {
	    // clear message ที่แสดง
	    clearMessage ();
	 
	    jQuery.ajax({
	        type : "POST",
	        url : "/training2018/jsp/standardui/searchListViewDialog.action",
	        data : jQuery(divPopup + ' :input').serialize(),
	        async : true,
	        success : function(data) {
	            renderData("popupResult", data.lstResult, data.messagePopup, data.criteriaPopup, false);
	        }
	    });
	}
</script>
</head>
  <!-- Popup -->       
        <div id="dialog-popup" title="รายชื่อพนักงาน" style="display: none;">
            
            <s:include value="/jsp/template/message_popup.jsp"/>
			 <table class="FORM">
			    <tr style="display: none;">
			        <td class="BORDER"></td>
			        <td class="LABEL"></td>
			        <td class="VALUE"></td>
			        <td class="LABEL"></td>
			        <td class="VALUE"></td>
			        <td class="BORDER"></td>
			    </tr>
			    <tr>
			        <td class="BORDER"></td>
			        <td class="LABEL">Firstname:</td>
			        <td class="VALUE"><s:textfield id="firstname" name="criteriaPopup.firstName" /></td>
			        <td class="LABEL">Lastname :</td>
			        <td class="VALUE"><s:textfield id="lastname" name="criteriaPopup.lastName" /></td>
			        <td class="BORDER"></td>
			    </tr>
			</table>
			 
			<s:include value="/jsp/template/button.jsp">
			    <s:param name="buttonType" value="%{'search_dialog,enable'}" />
			   <s:param name="function" value="%{'searchPopupCus(), initDialog(), dialogClose()'}" /> 
			</s:include>
			
			<!-- =============== Start Table Template Section ======================== -->
				<!-- div ผลลัพธ์จากการค้นหาที่หน้า Popup  -->
				<div id="popupResult" style="width: 150%; height:280px; " ></div>
				<!--ส่วนของการกำหนด parameters -->
				<s:set var="divresult" value='{"popupResult"}'/> 
				<s:set var="columnName" value='{getText("FullName"),getText("Sex"),getText("Department")}'/>
				<s:set var="columnData" value='{"fullname","sex","departmentDesc"}'/>
				<s:set var="columnCSSClass" value='{"col-width-175px","col-width-175px","col-width-175px"}'/>
				<s:set var="ajaxFunction" value='%{"reSearchPopupCus"}'/>
				<s:set var="criteriaName" value='%{"criteriaPopup"}'/>
				<s:set var="settingTable" value='{"1000:false:true"}'/>
				<!-- include table template -->
				<s:include value="/jsp/template/tableDialogWindows.jsp"/>
				<!-- ================ End Table Template Section ====================== -->
				 
				<s:hidden id="criteriaIds" name="criteriaPopup.ids"/>
				<script type="text/javascript">
			/* 	<!--crate table -->
				jQuery('div#popupResult').ready(function(){
					var table = new Object();
						table.width = "855";
			
					var tOverX = "false"; //scoll ล่าง
					var tOverY = "true"; //scoll ข้าง
			
					if (tOverX == "true") {
						table.overX = true;
				    } else {
				    	table.overX = false;
					}
				
					if  (tOverY == "true") {
						table.overY = true;
				    } else {
				    	table.overY = false;
					}
				
					//1. apply style table
					genTable('popupResult', table, true);
					
					//2. move button popup to div table
					var buttonPopup = jQuery("#buttonPopup_popupResult");
					jQuery("#popupResult").append(buttonPopup);
				}); */
			
				</script>
        </div>

