<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript">

	function sf() {
		
		jQuery("div.tabs").parent().attr("style", "width: 97%; float: left;  margin-left: 20px");
        if(jQuery("[name='criteria.criteriaKey']").val() != ""){
            searchAjax();
        }
		jQuery("#criteria_beforenameId").autocompletezAjax([{
			url: "<s:url value='/departmentAutoSelectItemServlet'/>",
            defaultKey: "",     // กำหนดค่า key ตัวแรกของ Autocomplete
            defaultValue: ""    // กำหนดค่า value ตัวแรกของ Autocomplete
        },{
            inputModelId: 'criteria_spmCode',
            url: "<s:url value='/userAutoSelectItemServlet'/>",
            postParamsId: {beforenameId: "criteria_beforenameId"},
            defaultKey: "",     // กำหนดค่า key ตัวแรกของ Autocomplete
            defaultValue: ""    // กำหนดค่า value ตัวแรกของ Autocomplete
        }]);
		
		jQuery("#criteria_departmentId").autocompletezAjax([{
			url: "<s:url value='/departmentAutoSelectItemServlet'/>",
            defaultKey: "",     // กำหนดค่า key ตัวแรกของ Autocomplete
            defaultValue: ""    // กำหนดค่า value ตัวแรกของ Autocomplete
        },{
            inputModelId: 'criteria_spmCode',
            url: "<s:url value='/userAutoSelectItemServlet'/>",
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
     
    function searchAjax(){
        var aOption = {
            divResultId: "div_datatable",
            tableId: "tableResult",
            checkbox:"N",
            urlSearch: "<s:url value='/tutorial/employee/searchEmployee.action' />",
           /*urlView: {
                url: "<s:url value='/tutorial/employee/employeeSearch.action' />",
                authen: "<s:property value='ATH.view' />"
            },*/
            pk: "projects.id", //input hidden in footer
            createdRowFunc: "manageRow"
        };
         
        var colData = [
			{ data: null,class: "order", orderable: false, defaultContent: "", "width":"60px"},
			{ data: null, class: "d_checkbox center", orderable: false, defaultContent: "", "width":"35px"},
			{ data: null, class: "d_view center col_view", orderable: false, defaultContent: "", "width":"35px"},
			{ data: "code", class: "", "width":"120px"},
			{ data: "fullname",class: "col-width-auto"},
			{ data: "sex", class: "", "width":"230px"},
			{ data: "groupEmp", class: "center", "width":"150px"},
			{ data: "department",class: "d_status center", "width":"60px"}
			];
             
        dataTable("<%=request.getContextPath()%>", colData, aOption);
    }
    function clearPage() {
        submitPage("<s:url value='/tutorial/initEmployee.action' />");
    }
    function manageRow(row, data) {
        jQuery(".code", row).html("<span title='"+data.fullThaiName+"'>"+data.thaiName+"</span>");
        jQuery(".documentType", row).html("<span title='"+data.fullDocumentType+"'>"+data.documentType+"</span>");
        jQuery(".engName", row).html("<span title='"+data.fullEngName+"'>"+data.engName+"</span>");
    }
	$(function(){
		jQuery("#criteria_startDate").input_dateformat({
			dateformat : "dd_sl_mm_sl_yyyy" ,
			selectDateRange: { 
	            dateTo: "criteria_startDateTo" // กำหนดวันที่สิ้นสุด
	        }
	    });
		jQuery("#criteria_startDateTo").input_dateformat({
			dateformat : "dd_sl_mm_sl_yyyy" ,
			selectDateRange: {
				dateFrom: "criteria_startDate" ,
				getValue: "criteria_startDate" ,
				
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
						<s:textfield id="criteria_nameEmp" name="criteria.nameEmp" maxlength="100" cssClass="combox" />
					</td>					
					<td class="BORDER"></td>
				</tr>
				<tr>
					<td class="BORDER"></td>
					<td class="LABEL">
						<s:text name="emp.nickname" />
					</td>
					<td class="VALUE">
						<s:textfield id="criteria_nicknameEmp" name="criteria.nicknameEmp" maxlength="100" cssClass="combox" />
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
							<s:textfield id="criteria_departmentId" name="criteria.departmentId" code-of="criteria_departmentId_autocomplete" cssClass="autocomplete" />
							<s:textfield id="criteria_departmentName" name="criteria.departmentName" text-of="criteria_departmentId_autocomplete" cssClass="autocomplete" />
				
					</td>
					<td class="LABEL">
						<s:text name="emp.department"/>
					</td>
					<td class="VALUE">
							<s:textfield id="criteria_spmCode" name="criteria.spmCode" code-of="criteria_spmCode_autocomplete" cssClass="autocomplete" />
							<s:textfield id="criteria_spmName" name="criteria.spmName" text-of="criteria_spmCode_autocomplete" cssClass="autocomplete" />
				</td>
					<td class="BORDER"></td>
				</tr>
				<tr>
					<td class="BORDER"></td>
					<td class="LABEL">
						<s:text name="emp.startdate" />
					</td>
					<td class="VALUE">
						<s:textfield id="criteria_startDate" name="criteria.startDate" maxlength="100" cssClass="input_datapicker"/>
					</td>
					<td class="LABEL">
						<s:text name="emp.startDateTo" />
					</td>
					<td class="VALUE">
						<s:textfield id="criteria_startDateTo" name="criteria.startDateTo" maxlength="100" cssClass="input_datapicker"/>
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
                        <th></th>
                        <th></th>
                        <th></th>
                        <th><s:text name="emp.nameEmp"/></th>
                        <th><s:text name="emp.sex"/></th>
                        <th><s:text name="emp.groupEmp"/></th>
                        <th><s:text name="emp.department"/></th>
                        
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="8" class="dataTables_empty">Loading data from server</td>
                    </tr>
                </tbody>
            </table>
        </div>
   	</div>
	   	
   	<!--------------------------------------- include --------------------------------------->
   	 <s:include value="/jsp/template/hiddenSearchForDatatable.jsp" />
   	<s:hidden name="projects.id" />
   	<s:token />
   	<!--------------------------------------- include --------------------------------------->
	
	</s:form>
</body>
</html>