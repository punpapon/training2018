<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<s:include value="/jsp/template/message_popup.jsp"/>
 <html>
 <head>
 <script type="text/javascript">
 function initDialog() {
	    jQuery('div'+ divPopup +' #criteriaIds').val(ids);
	     
	    // clear เงื่อนไขการค้นหา 
	    // ...
	     
	    // เรียกใช้งาน function สำหรับการ clear หน้าจอ
	    clearDialogWindows ('popupResult');
	     
	    jQuery.ajax({
	        type : "POST",
	        url : "/struts2/tutorial/dialog/initCustomerDialog.action",
	        data : null,
	        async : true,
	        success : function(data) {
	            // initial criteria popup for sort header table
	            initialCriteria(data.criteriaPopup);
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
	        url : "/struts2/tutorial/dialog/searchListCustomerDialog.action",
	        data : jQuery(divPopup + ' :input').serialize(),
	        async : true,
	        success : function(data) {
	            renderData("popupResult", data.lstResult, data.messagePopup, data.criteriaPopup, false);
	        }
	    });
	}
 </script>
 </head>
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
        <td class="VALUE"><s:textfield id="firstname" name="criteriaPopup.customer.cusFirstname" /></td>
        <td class="LABEL">Lastname :</td>
        <td class="VALUE"><s:textfield id="lastname" name="criteriaPopup.customer.cusLastname" /></td>
        <td class="BORDER"></td>
    </tr>
</table>
 
<s:include value="/jsp/template/button.jsp">
    <s:param name="buttonType" value="%{'searchdialog,enable'}" />
    <s:param name="function" value="%{'searchPopupCus(), initDialog(), dialogClose()'}" />
</s:include>
 
<!-- =============== Start Table Template Section ======================== -->
<!-- div ผลลัพธ์จากการค้นหาที่หน้า Popup  -->
<div id="popupResult" style="display:none; width: 100%; height:280px; " ></div>
<!--ส่วนของการกำหนด parameters -->
<s:set var="divresult" value='{"popupResult"}'/> 
<s:set var="columnName" value='{getText("Code"),getText("Firstname"),getText("Lastname"),getText("E-Mail"),getText("Login"),getText("mobile")}'/>
<s:set var="columnData" value='{"cusCode","cusFirstname","cusLastname","cusEmail","user.login","cusMobile"}'/>
<s:set var="columnCSSClass" value='{"col-width-75px","col-width-100px","col-width-100px","col-width-100px","col-width-100px","col-width-100px"}'/>
<s:set var="ajaxFunction" value='%{"reSearchPopupCus"}'/>
<s:set var="criteriaName" value='%{"criteriaPopup"}'/>
<s:set var="settingTable" value='{"1000:false:true"}'/>
<!-- include table template -->
<s:include value="/jsp/template/tableDialogWindows.jsp"/>
<!-- ================ End Table Template Section ====================== -->
 
<s:hidden id="criteriaIds" name="criteriaPopup.customer.ids"/>
</html>