<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<s:include value="/jsp/template/jquery-inputdatetimeformat.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript">

	function sf() {
		jQuery("div.tabs").parent().attr("style", "width: 97%; float: left;  margin-left: 20px");
        if(jQuery("[name='criteria.criteriaKey']").val() != ""){
            searchAjax();
        }

		jQuery("#criteria_departmentId").autocompletezAjax([{
			url: "<s:url value='/departmentAutoSelectItemServlet'/>",
            defaultKey: "",     // กำหนดค่า key ตัวแรกของ Autocomplete
            defaultValue: ""    // กำหนดค่า value ตัวแรกของ Autocomplete
        },{
            inputModelId: 'criteria_positionId',
            url: "<s:url value='/positionAutoSelectItemServlet'/>",
            postParamsId: {departmentId: "criteria_departmentId"},
            defaultKey: "",     // กำหนดค่า key ตัวแรกของ Autocomplete
            defaultValue: ""    // กำหนดค่า value ตัวแรกของ Autocomplete
        }]);
		
	}
	function searchPage() {	
        document.getElementsByName('criteria.criteriaKey')[0].value = '';
        searchAjax();
    }
	function clearPage() {
		submitPage("<s:url value='/jsp/tutorial/initEmployee.action' />");
	}
	function addPage() {
		submitPage("<s:url value='/jsp/tutorial/gotoAddEmployee.action' />");
	}
	function exportExcel() {
		submitPage("<s:url value='/jsp/tutorial/exportEmployee.action' />");
	}
	function print() {		
		submitPageReport("<s:url value='/jsp/tutorial/exportEmployee.action' />");	
	}
	function report() {
		submitPageReport("<s:url value='/jsp/tutorial/reportEmployee.action' />");	
	}
	function deleteData() {
		if(!validateSelectOne('criteria.selectedIds')){
	         return false;
	     }     
		if(!confirm("<s:text name='50005X' />")){
			return false;
		}
	     submitPage("<s:url value='/jsp/tutorial/deleteEmployee.action' />");
	    }
     
    function searchAjax(){
        var aOption = {
            divResultId: "div_datatable",
            tableId: "tableResult",
            checkbox:"Y",
            urlSearch: "<s:url value='/jsp/tutorial/searchEmployee.action' />",
            urlEdit: {
            	url : "<s:url value='/jsp/tutorial/gotoEditEmployee.action' />" ,
            	authen: "<s:property value='ATH.edit' />"
            } ,
            urlView: {
                url: "<s:url value='/jsp/tutorial/gotoViewEmployee.action' />",
                authen: "<s:property value='ATH.view' />"
            },
            pk: "employee.id", //input hidden in footer
            fixedCoumnsLeft : 6, 
            fixedCoumnsRight : 0,
            footerHtml: '<img src="<s:url value='/images/icon/i_deletenew.png' />" height="25" size="100" alt="Johnson Pond" onclick="deleteData()">&nbsp;&nbsp;&nbsp;<img src="<s:url value='/images/menu/report.png' />" height="25" size="100" alt="Johnson Pond" onclick="report()">&nbsp;&nbsp;&nbsp;<img src="<s:url value='/images/menu/i_excel.png' />" height="25" size="100" alt="Johnson Pond" onclick="exportExcel()">',
            createdRowFunc: "manageRow"
        };
         
        var colData = [
			{ data: null,class: "order", orderable: false, defaultContent: "", "width":"60px"},
			{ data: null, class: "d_checkbox center", orderable: false, defaultContent: "", "width":"35px"},
			{ data : null, class: "d_edit center" , orderable: false, defaultContent: "", "width":"35px"},
			{ data: null, class: "d_view center col_view", orderable: false, defaultContent: "", "width":"35px"},
			{ data: "fullname", class: "", "width":"120px"},
			{ data: "sex",class: "col-width-auto"},
			{ data: "departmentDesc", class: "", "width":"230px"},
			{ data: "positionDesc", class: "center", "width":"150px"},
			{ data: "startWorkDate",class: "center", "width":"60px"},
			{ data: "endWorkDate" , class: "center", "width":"60px"},
		 	{ data: "workStatus" , class: "center", "width":"150px"},
			{ data: "transaction.createUser" , class: "center", "width":"50px"},
			{ data: "transaction.createDate" , class: "center", "width":"50px"},
			{ data: "transaction.updateUser" , class: "center", "width":"50px"}, 
			{ data: "transaction.updateDate", class: "center", "width":"50px"},
			{ data: "transaction.createRemark", class: "center", "width":"50px"}
			];
        fixedColumns("<%=request.getContextPath()%>", colData , aOption);
        <%-- dataTable("<%=request.getContextPath()%>", colData, aOption); --%>
    }
    /**
     * ใช้สำหรับ submit กรณีเพิ่ม, แก้ไข, แสดง
     * ต้องใช้ร่วมกับไฟล์ inputmethod.js
     * @param action
     * @param emName (hidden name ของ id ใน model)
     * @param valId
     */
    function submitAction(action, elName, valId){
        if(valId != null && valId != ""){
            document.getElementsByName(elName)[0].value = valId;
        }
        submitPage(action);
    }
    function manageRow(row, data) { 	
        var htmlIconView = "";
        
        /*     
         *  เมื่อวาดตารางเสร็จแล้วต้องการเปลี่ยนแปลงหรือจัดการกับข้อมูลในตารางต้องมาทำใน function managerRow
        */          
                //Select F,M
        var gender = jQuery(row).find("td").eq(5).html();
        	if(gender === "M"){
            jQuery(row).find("td").eq(5).html("Male");
           } if(gender === "F") {
            jQuery(row).find("td").eq(5).html("Female");
           }
           
         var statusWork = jQuery(row).find("td").eq(10).html();
        	if(statusWork == "R"){
        		jQuery(row).find("td").eq(10).html("พนังงานทดลองงาน");
        	}
        	if(statusWork == "C"){
        		jQuery(row).find("td").eq(10).html("พนังงานปัจจุบัน");
        	}
        	if(statusWork == "T"){
        		jQuery(row).find("td").eq(10).html("อดีตพนักงาน");
        	} 
        	
		 if(data.workStatus == "X") {
			
			 htmlIconView = jQuery("#tempIconEditDisable").html(); 
		} 
		 else
			 {
			 	htmlIconView = jQuery("#tempIconEditEnable").html(); 
			 }
		jQuery(row).find("td").eq(2).html(htmlIconView); 
    }
	$(function(){
		jQuery("#criteria_startWorkDate").input_dateformat({
			dateformat : "dd_sl_mm_sl_yyyy" ,
			selectDateRange: { 
	            dateTo: "criteria_endWorkDate" // กำหนดวันที่สิ้นสุด
	        }
	    });
		jQuery("#criteria_endWorkDate").input_dateformat({
			dateformat : "dd_sl_mm_sl_yyyy" ,
			selectDateRange: {
				dateFrom: "criteria_startWorkDate" 	// กำหนดวันเริ่มต้น	
			} 
		});
	});
</script>
</head>

<body>
	<s:form id="searchForm" name="searchForm" method="post" namespace="/jsp/tutorial" action="initAction" cssClass="margin-zero" onsubmit="return false;">
	
	<!--------------------------------------- divSearchCriteria --------------------------------------->
	<div id="divSerachForm" class="CRITERIA">
	
		<div id="divCriteria" class="RESULT_BOX BORDER_COLOR">
			<div class="RESULT_BOX_TITAL ">
				<table class="TOTAL_RECORD">
					<tr>
						<td class="LEFT" style="width: 10%;">
							<s:text name="criteria"/>
						</td>
					</tr>
				</table>
			</div>
			
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
					<td class="LABEL">
						<s:text name="emp.beforename" /><em>&nbsp;&nbsp;</em>
					</td>
					<td class="VALUE">
						<s:select name="criteria.prefixId"
							list="listPrefix" 
							headerKey=""
							headerValue="%{getText('all')}" 
							listKey="key" 
							listValue="value"
							cssClass = "combox"
							/>
					</td>
					<td class="LABEL">
						<s:text name="emp.nameEmp" />
					</td>
					<td class="VALUE">
						<s:textfield id="criteria_fullname" name="criteria.fullname" maxlength="120" cssClass="combox" />
					</td>					
					<td class="BORDER"></td>
				</tr>
				<tr>
					<td class="BORDER"></td>
					<td class="LABEL">
						<s:text name="emp.nickname" />
					</td>
					<td class="VALUE">
						<s:textfield id="criteria_nickname" name="criteria.nickname" maxlength="120" cssClass="combox" />
					</td>
					<td class="LABEL">
						<s:text name="emp.sex" /><em>&nbsp;&nbsp;</em>
					</td>
					<td class="VALUE">
						<s:select name="criteria.sex"
							list="listSex" 
							headerKey=""
							headerValue="%{getText('all')}" 
							listKey="key" 
							listValue="value"
							cssClass = "combox"
							/>
						
					</td>		
					<td class="BORDER"></td>
				</tr>
				<tr>
					<td class="BORDER"></td>
					<td class="LABEL">
						<s:text name="emp.groupEmp" />
					</td>
					<td class="VALUE">
							<s:textfield id="criteria_departmentId" name="criteria.departmentId" code-of="criteria_department_autocomplete" cssClass = "autocomplete"/>
							<s:textfield id="criteria_departmentDesc" name="criteria.departmentDesc" text-of="criteria_department_autocomplete" cssClass = "autocomplete" />
				
					</td>
					<td class="LABEL">
						<s:text name="emp.department"/>
					</td>
					<td class="VALUE">
							<s:textfield id="criteria_positionId" name="criteria.positionId" code-of="criteria_position_autocomplete" cssClass = "autocomplete" />
							<s:textfield id="criteria_positionDesc" name="criteria.positionDesc" text-of="criteria_position_autocomplete" cssClass = "autocomplete"/>
				</td>
					<td class="BORDER"></td>
				</tr>
				<tr>
					<td class="BORDER"></td>
					<td class="LABEL">
						<s:text name="emp.startdate" />
					</td>
					<td class="VALUE">
						<s:textfield id="criteria_startWorkDate" name="criteria.startWorkDate" maxlength="200" cssClass="input_datapicker"/>
					</td>
					<td class="LABEL">
						<s:text name="emp.startDateTo" />
					</td>
					<td class="VALUE">
						<s:textfield id="criteria_endWorkDate" name="criteria.endWorkDate" maxlength="200" cssClass="input_datapicker"/>
					</td>
					<td class="BORDER"></td>
				</tr>
				<tr>
						<td class="BORDER"></td>
						<td class="LABEL">
							<s:text name="emp.statusEmp" />
						</td>
						<td class="VALUE">
							<s:select name="criteria.workStatus"
							list="listWorkStatus" 
							headerKey=""
							headerValue="" 
							listKey="key" 
							listValue="value"
							cssClass = "combox"
							/>
						</td>
						<td class="LABEL">
							<s:text name="emp.pageCount"/>
						</td>
						<td class="VALUE">
							<s:select id="criteria_linePerPage" cssClass="lpp-style clearform" name="criteria.linePerPage"  list="LPP" />
						</td>
						<td class="BORDER"></td>
					</tr>
			
			</table>
			
				 <s:include value="/jsp/template/button.jsp">
					<s:param name="buttonType" value="%{'search,enable'}" />
				</s:include> 
				<s:include value="/jsp/template/button.jsp">
					<s:param name="buttonType" value="%{'report,enable'}" />
				</s:include> 
			
				
			
		</div>
	</div>
		
	
	<!--------------------------------------- divSearchCriteria --------------------------------------->
	
	<!--------------------------------------- divResult --------------------------------------->
	<div class="RESULT">	
		<!-- code for display table -->
		<div id="div_datatable" class="ex_highlight_row" style="display: none;">  
            <table class="display" id="tableResult">
                <thead>
                    <tr>
                        <th><s:text name="emp.code"/></th>
                        <th><input id="criteria_selectedIds" type="checkbox" name="criteria.selectedIds" onClick="checkboxToggle('criteria.selectedIds',this.checked)" /></th>
                        <th></th> 
                        <th></th>                   
                        <th><s:text name="emp.nameEmp"/></th>
                        <th><s:text name="emp.sex"/></th>
                        <th><s:text name="emp.groupEmp"/></th>
                        <th><s:text name="emp.department"/></th>
                                         
                        <th><s:text name="emp.startWorkDate"/></th>
                        <th><s:text name="emp.endWorkDate"/></th>
                        <th><s:text name="emp.statusWork" /></th>
                        <th><s:text name="emp.createUser"/></th>
                        <th><s:text name="emp.createdate"/></th>
                        <th><s:text name="emp.updateUser"/></th>
                        <th><s:text name="emp.updateDate" /></th>
                        <th><s:text name="emp.remark" /></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="16" class="dataTables_empty">Loading data from server</td>
                    </tr>
                </tbody>  
            </table>            
       	</div>
   	</div>
       	
		<div id="tempIconEditDisable" style="display: none;">
			<a href="javascript:void(0)" >
			    <img src="<s:url value='/images/icon/i_edit_dis.png' />" border="0">
			</a>
		</div>
   	<!--------------------------------------- include --------------------------------------->
   	 <s:hidden><s:include value="/jsp/template/hiddenSearchForDatatable.jsp" /></s:hidden>
   	
   	<s:hidden name="employee.id" />
   	<s:token />
   	<!--------------------------------------- include --------------------------------------->
	
	</s:form>
</body>
</html>