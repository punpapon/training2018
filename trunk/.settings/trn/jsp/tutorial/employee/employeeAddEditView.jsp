<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<script type="text/javascript">
	
	function sf() {
		/* Auto Complete แสดงสังกัดและแผนก  */
		
		jQuery("#employee_departmentId").autocompletezAjax(
				[{
					url: "<s:url value='/departmentAutoSelectItemServlet'/>",
	                defaultKey: "",     // กำหนดค่า key ตัวแรกของ Dropdownlist
	                defaultValue: "",    // กำหนดค่า value ตัวแรกของ Dropdownlist
	                requireInput: true 
             	},{
                 	inputModelId: 'employee_positionId',
                 	url: "<s:url value='/positionAutoSelectItemServlet'/>",
                 	postParamsId: {departmentId: "employee_departmentId"},
                 	defaultKey: "",     // กำหนดค่า key ตัวแรกของ Dropdownlist
                 	defaultValue: "",    // กำหนดค่า value ตัวแรกของ Dropdownlist
                 	requireInput: true 
             	}]
         );
	
			$(function(){
				jQuery("#employee_startWorkDate").input_dateformat({
					dateformat : "dd_sl_mm_sl_yyyy" ,
					selectDateRange: { 
						
			            dateTo: "employee_endWorkDate" // กำหนดวันที่สิ้นสุด
			        }
			    });
				
				jQuery("#employee_endWorkDate").input_dateformat({
					dateformat : "dd_sl_mm_sl_yyyy" ,
					
					selectDateRange: {
						dateFrom: "employee_startWorkDate" 	// กำหนดวันเริ่มต้น	
					} 
				});
			});
			$("#employee_endWorkDate_dd_sl_mm_sl_yyyy").attr("disabled",true);
	}
	
	/* กดปุ่มบันทึก   */
	function saveAdd(){
		// validate old version
		if(!validateAll()){ 
	        return false;
	    }
		
		// validate all version
// 		if(!validateFormInputAll()){
// 			return false;
// 		}
		
		if(!confirm("<s:text name='50006' />")){
			return false;
		}
		submitPage("<s:url value='/jsp/tutorial/addEmployee.action' />");
	}
	
	/* กดปุ่มแก้ไข   */
	function saveEdit(){
		// validate old version
		if(!validateAll()){ 
	        return false;
	    }
		
		// validate all version
// 		if(!validateFormInputAll()){
// 			return false;
// 		}
		
		if(!confirm("<s:text name='50007' />")){
			return false;
		}
		submitPage("<s:url value='/jsp/tutorial/editEmployee.action' />");
	}
	
	/* กดปุ่มยกเลิกเพิ่ม   */
	function cancelAdd(){
		if(!confirm("<s:text name='50008' />")){
			return false;
		}
		submitPageForm();
	}
	/* กดปุ่มยกเลิกแก้ไข  */
	function cancelEdit(){
		if(!confirm("<s:text name='50009' />")){
			return false;
		}
		submitPageForm();
	}
	
	/* กดปุ่มยกเลิกแสดง */
	function cancelView(){
		if (!confirm('<s:text name="50010" />')) {
            return false;
        }
		submitPageForm();
	}
	
	function submitPageForm() {
	     if (document.getElementsByName('criteria.criteriaKey')[0].value == '') {
	        action = "<s:url value='/jsp/tutorial/initEmployee.action'/>";
	    } else {
	        action = "<s:url value='/jsp/tutorial/initEmployee.action'/>";
	    } 
	    submitPage(action);
	}
	
 	function chkStatusWork(val) {		
		if(val != 'X'){			
				$("#employee_endWorkDate_dd_sl_mm_sl_yyyy").attr("disabled",true);
			}	
		else {			
				$("#employee_endWorkDate_dd_sl_mm_sl_yyyy").attr("disabled",false);
			}		
		console.log(val);	
	} 
	
</script>
<STYLE type="text/css">
	b{color: red;}
</STYLE>
</head>
<body>
	<s:form id="addEditForm" name="addEditForm" method="post" namespace="/jsp/tutorial" cssClass="margin-zero" onsubmit="return false;" >
		<!-- set class ให้กับหน้า -->
		<s:if test="page.getPage() == 'view'"> 
		 	<s:set var="readOnly" value="true" />
		</s:if>
		<s:else> 
			<s:set var="readOnly" value="" />
		</s:else>
		
		<s:if test="page.getPage() == 'edit'">
			<s:set var="projCodeReadOnly" value="%{'readonly'}" />
		</s:if>
		<s:else>
			<s:set var="projCodeReadOnly" value="" />
		</s:else>
		 
		<%-- <s:text name="%{readOnly}" /> --%>
		<div class="">
			<table class="FORM" style="float:left; margin:13px;">
			
				<tbody>
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
							<s:text name="emp.beforename"/><em>*</em>
						</td>
						<td class="VALUE" >
						<s:if test="page.getPage() == 'view' ">
							<s:select list="listPrefix"
								id = "employee_prefixId"
								name = "employee.prefixId"
								headerKey=""
								headerValue="%{getText('all')}" 
								listKey="key" 
								listValue="value"
								cssClass = "combox"
								cssStyle="width: 200px;"
								disabled= "true"
							/>
						</s:if>
						<s:else>
							<s:select list="listPrefix"
								id = "employee_prefixId"
								name = "employee.prefixId"
								headerKey=""
								headerValue="%{getText('all')}" 
								listKey="key" 
								listValue="value"
								cssClass = "combox"
								cssStyle="width: 200px;"
								required="true"
																
							/>
						</s:else>	
						</td>
						<td class="LABEL">
							<s:text name="emp.sex"></s:text><em>*</em>
						</td>
						<td class="VALUE">
						<s:if test="page.getPage() == 'view' || page.getPage() == 'edit'">
							 <s:radio label="Answer" name="employee.sex" id="employee_sex" 
							 list="listSex" 
							 listKey="key"
							 listValue="value" 	
							 disabled="true"				 
							 />
						</s:if>
						
						<s:else>
						
							 <s:radio label="Answer" name="employee.sex" id="employee_sex" 
							 list="listSex" 
							 listKey="key"
							 listValue="value" 	
							 		 
							 />
						</s:else>
						</td>
						<td class="BORDER"></td>
					</tr>
					<tr>
						<td class="BORDER"></td>
						<td class="LABEL">
							<s:text name="emp.name" /><em>*</em>
						</td>
						<td class="VALUE">
						<s:if test="page.getPage() == 'view'" >
							<s:textfield name="employee.name" disabled="true" cssStyle="width: 200px;"/>
						</s:if>
						<s:else>
							<s:textfield id="employee_name" 
								name="employee.name" 
								cssClass ="textfield requireInput %{readOnly} " 
								cssStyle="width: 200px;"
								validName="%{getText('emp.beforename')}"  
								maxlength="255" />						
						</s:else>
						</td>
						<td class="LABEL">
							<s:text name="emp.lastname" /><em>*</em>
						</td>
						<td class="VALUE">
							<s:textfield id="employee_lastName" 
								name="employee.lastName" 
								cssClass ="txt requireInput %{readOnly} " 
								cssStyle="width: 200px;"
								validName="%{getText('emp.lastname')}" 
								maxlength="255"
								disabled="%{readOnly}"/>
						</td>
						<td class="BORDER"></td>
					</tr>
					<tr>
						<td class="BORDER"></td>
						<td class="LABEL">
							<s:text name="emp.nickname"></s:text><em>*</em>
						</td>
						<td class="VALUE">
						<s:if test="page.getPage() == 'view'" >
							<s:textfield name="employee.nickName" disabled="true" cssStyle="width: 200px;"/>
						</s:if>
						<s:else>
							<s:textfield id="employee_nickName" 
								name="employee.nickName" 
								cssClass ="textfield requireInput %{readOnly} " 
								cssStyle="width: 200px;"
								validName="%{getText('emp.lastname')}" 
								maxlength="255"/>
						</s:else>		
								
						</td>						
						<td class="BORDER"></td>
					</tr>
					<tr>
						<td class="BORDER"></td>
						<td class="LABEL">
							<s:text name="emp.groupEmp" /><em>*</em>
						</td>
						<td class="VALUE">
						<s:if test="page.getPage() == 'view'">
								<s:textfield id="employee_departmentDesc" 
									name="employee.departmentDesc" 
									disabled="true"
									cssStyle="width: 200px;"/>								
							</s:if>
							<s:else>
								<s:textfield id="employee_departmentId" name="employee.departmentId" code-of="employee_department_autocomplete"  cssClass="autocomplete" />						
								<s:textfield id="employee_departmentDesc" name="employee.departmentDesc" text-of="employee_department_autocomplete" cssClass="autocomplete" />
							</s:else>
						</td>
						<td class="LABEL">
							<s:text name="emp.department" /><em>*</em>
						</td>
						<td class="VALUE">	
						<s:if test="page.getPage() == 'view'">
								<s:textfield id="employee_positionDesc" 
									name="employee.positionDesc" 
									disabled="true"
									cssStyle="width: 200px;"
									/>
							</s:if>		
							<s:else>				
								<s:textfield id="employee_positionId" name="employee.positionId" code-of="employee_position_autocomplete"  cssClass="autocomplete"  />
								<s:textfield id="employee_positionDesc" name="employee.positionDesc" text-of="employee_position_autocomplete"  cssClass="autocomplete" />
							</s:else>
						</td>
						<td class="BORDER"></td>
					</tr>
					<tr>
						<td class="BORDER"></td>
						<td class="LABEL">
							<s:text name="emp.startWorkDate"></s:text><em>*</em>
						</td>
						<td class="VALUE">
						<s:if test="page.getPage() == 'view' || page.getPage() == 'edit'">
								<s:textfield name="employee.startWorkDate" cssStyle="width: 200px;" disabled="true"/>
						</s:if>
						<s:else>
								<s:textfield id="employee_startWorkDate" name="employee.startWorkDate" cssStyle="width: 200px;" cssClass="input_datapicker" />
						</s:else>
						</td>
					<s:if test="page.getPage() == 'view'">
						<td class="LABEL"><s:text name="emp.endWorkDate"></s:text></td>				
						<td class="VALUE"><s:textfield name="employee.endWorkDate" cssStyle="width: 200px;" disabled="true"/></td>
					</s:if>
					<s:else>
						<td class="LABEL"><s:text name="emp.endWorkDate"></s:text><em>*</em></td>				
						<td class="VALUE"><s:textfield id="employee_endWorkDate" name="employee.endWorkDate" cssStyle="width: 200px;" cssClass="input_datapicker" class="cancelxx"/></td>
					</s:else>
						<td class="BORDER"></td>
						
					</tr>
					<tr>
						<td class="BORDER"></td>
						<td class="LABEL">
							<s:text name="emp.statusEmp"/><em>*</em>
						</td>
						<td class="VALUE">
						<s:if test="page.getPage() == 'view'" >
							<s:radio name="employee.workStatus" 
								id="employee_workStatus"
								list="listWorkStatus" 
								listKey="key" 
								listValue="value" 
								disabled = "true">
							</s:radio>
						</s:if>
						<s:else>						
						<s:radio name="employee.workStatus" 
								id="employee_workStatus"
								list="listWorkStatus" 
								listKey="key" 
								listValue="value" 
							    onclick="chkStatusWork(value)" >							
								</s:radio>   																								
						</s:else>
						</td>			
						<td class="BORDER"></td>
					</tr>
					<tr>
						<td class="BORDER"></td>
						<td class="LABEL">
							<s:text name="emp.remark"/>
						</td>
						<td class="VALUE">
						<s:if test="page.getPage() == 'view' ">
							<s:textarea name="employee.remark" maxlenght="300" cssStyle="width: 200px;" disabled="true"></s:textarea>
						</s:if>
						<s:else>
							<s:textarea id="employee_remark" name="employee.remark" maxlenght="300" cssStyle="width: 200px;"></s:textarea>
						</s:else>
						</td>			
						<td class="BORDER"></td>
					</tr>
					
				</tbody>
				
			</table>
					<table style="margin: 0px auto;">
					  <tr>
					    <th></th>
					    <th></th>
					    <th></th>
					    <th></th>
					  </tr>
					  
			 		<s:if test="page.getPage() != 'add' ">		 		
					  <tr>			 
					    <td ><b><s:text name="Crate user : " /><s:property value="transaction.createUser" /></b></td>	
					   		 <td></td>			  
					    <td ><b><s:text name="Update user" /><s:property value="transaction.updateUser" /></b></td>
					    	 <td></td>					  
					  </tr>
					  <tr>
					    <td ><b><s:text name="Crate date" /><s:property value="transaction.createDate" /></b></td>	
					    	<td></td>			  
					    <td ><b><s:text name="Update date" /><s:property value="transaction.updateDate" /></b></td>
					    	<td></td>				    
					  </tr>				 
					 </s:if>					  
					 <s:else>
					   		<s:property value="" />
					 </s:else>					 					 					 
					</table>
			<!--------------------------------------- ADD PAGE --------------------------------------->		
			<s:if test='page.getPage() == "add"'>
				<s:include value="/jsp/template/button.jsp">
					<s:param name="buttonType" value="%{'add,enable'}" />
				</s:include>
			</s:if>
			<!--------------------------------------- EDIT PAGE --------------------------------------->
			<s:elseif test='page.getPage() == "edit"'>
				<s:include value="/jsp/template/button.jsp">
					<s:param name="buttonType" value="%{'edit,enable'}" />
				</s:include>
			</s:elseif>
			<!--------------------------------------- VIEW PAGE --------------------------------------->
			<s:elseif test='page.getPage() == "view"'>
				<s:include value="/jsp/template/button.jsp">
					<s:param name="buttonType" value="%{'view,enable'}" />
				</s:include>
			</s:elseif>			
			<s:textfield name="criteria.criteriaKey" />
			<s:textfield name="P_CODE"/>
		    <s:textfield name="F_CODE"/>
		    <s:textfield name="page"/>
			<s:textfield name="employee.id" />
			<s:token />
		</div>
	</s:form>
</body>
</html>