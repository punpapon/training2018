<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
<script type="text/javascript" src="<s:url value='/js/table/table.js' />"></script>
	<script type="text/javascript">
		function sf(){
			
		}

		function initMenu () {
			submitPage("<s:url value='/jsp/security/initMainpage.action' />");
			
		}
		function searchPage() {	
			
	       // document.getElementsByName('criteria.criteriaKey')[0].value = '';
	        submitPage("<s:url value='/jsp/standardui/searchDataTable.action' />");
	       // searchAjax(); 
	    }
		function backPage () {
			submitPage("<s:url value='/jsp/standardui/initMiscellaneous.action' />");
		}
		function edit(){
		        if(confirm('<s:text name="50004" />') == false){ 
		            return false;
		        }   
		        submitPage("<s:url value='/tutorial/editTableAddDeleteRow.action'/>");
		 }
		function validate() {
			if(!validateAll()){ 
		        return false;
		    }
		}
		
		function chooseRow(obj){
	        //1. หาค่า index ก่อนหน้า
	        var tableId = "tableId_divTableAdd";
	        var index = jQuery('table#'+tableId+' tbody tr').length;    
	         
	        var clArr = ["checkbox", "col-width-150px", "col-width-150px", ""];
	        var elmArr = new Array();
	        //elemnet 3 element ที่ต้องวาดคือ  input checkbox, hidden deleteFlag, hidden id
	        elmArr[0] = "<input type='checkbox' id='cnkColumnId' name='cnkColumnId' value='"+ obj.provinceId +"'/>"
	            + "<input type='hidden' name='hideColumn.lstTableAddDeleteRow["+index+"].deleteFlag' value=''/>"
	            + "<input type='hidden' name='hideColumn.lstTableAddDeleteRow["+index+"].id' value=''/>"
	            //other hidden data
	            + "<input type='hidden' name='hideColumn.lstTableAddDeleteRow["+index+"].provinceId' value='"+ obj.provinceId +"'/>"
	            + "<input type='hidden' name='hideColumn.lstTableAddDeleteRow["+index+"].provinceCode' value='"+ obj.provinceCode +"'/>"
	            + "<input type='hidden' name='hideColumn.lstTableAddDeleteRow["+index+"].provinceName' value='"+ obj.provinceName +"'/>";
	 
	        //other column 1, 2, 3, ..., n
	        elmArr[1] = obj.provinceId;
	        elmArr[2] = obj.provinceCode;
	        elmArr[3] = obj.provinceName;
	         
	        //2. create row 
	        tableCreateTableRow(tableId, clArr, elmArr);
	     
	        //3. เก็บค่า id ไว้ใน textfield เวลาค้นหาจะเอาไป where not in
	        var ids = jQuery('div#divTableAdd  #idsSelectedRow').val(); //idsSelectedRow ถูกวาดมาจาก Table Template การ Seletc ต้องทำผ่าน DIV
	        if(ids != ""){
	            ids = ids + "," +obj.provinceId;
	        }else{
	            ids = obj.provinceId;
	        }
	        jQuery('div#divTableAdd  #idsSelectedRow').val(ids);
	     
	        //4. close dialog
	        //Dialog will close itselft.
	    }
		
		
</script>
</head>
<body onload="sf()">


<s:form id="addEditForm" name="addEditForm" method="post" cssClass="margin-zero">
	<!--------------------------------------- divSearchCriteria --------------------------------------->
				
				 <s:include value="/jsp/template/button.jsp">
					<s:param name="buttonType" value="%{'search,enable'}" />
				</s:include> 		
	
	
	<!-------------------------------------- div Result ----------------------------------------->
		<!-- div สำหรับแสดงผล  -->
	<div id="divTableAdd" class="RESULT" style="width: 70%; height:480px;" ></div>
	<!--ส่วนของการกำหนด parameters -->
		<s:set var="divresult" value='{"divTableAdd"}'/> 
		<s:set var="listTableData" value='%{"listResult"}'/> <!-- domain list เช่น userData.listPermission -->
		<s:set var="columnName" value='{getText("emp.nameEmp"), getText("emp.sex"), getText("emp.groupEmp"), getText("emp.department"), getText("emp.startWorkDate"), getText("emp.statusWork")}'/>
		<s:set var="columnID" value='{"provinceId"}'/> <!-- PK popup -->
		<s:set var="columnData" value='{"fullname","sex","departmentDesc","positionDesc","startWorkDate","workStatus"}'/>
		<s:set var="columnCSSClass" value='{"col-width-175px","col-width-175px","col-width-175px","col-width-175px","col-width-175px","col-width-175px"}'/>
		<s:set var="hiddenData" value='{"fullname", "sex", "departmentDesc","positionDesc","startWorkDate","workStatus"}'/> <!-- domain properties สำหรับดึงข้อมูลมาเก็บเป็น hidden -->
		<s:set var="settingTable" value='{"1000:false:true"}'/>
		<!-- include table template -->
	<s:include value="/jsp/template/tableAddDeleteRow.jsp"/>
	
	   <script>
            var iconPopup ="<a href='javascript:void(0)' onclick='dialogSingleOpen(\"dialog-popup\", 900, 500, true, initDialog, \"chooseRow\", jQuery(\"div#divTableAdd  #idsSelectedRow\").val())'><img src='<s:url value='/images/icon/i_add.png' />'/> เพิ่มข้อมูล</a>";
            jQuery("#headerTable_<s:property value='#divresult[0]'/> .RIGHT").append(iconPopup);
        </script>
      
      	<s:include value="/jsp/standardui/datatable/popup.jsp"></s:include>
   
</s:form>
</body>
</html>