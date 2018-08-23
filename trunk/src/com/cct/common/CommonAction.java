package com.cct.common;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import util.database.CCTConnection;
import util.file.FileManagerUtil;
import util.image.BrowseUploadServiceUtil;
import util.log.LogUtil;
import util.web.SessionUtil;

import com.cct.common.CommonModel.PageType;
import com.cct.domain.GlobalVariable;
import com.cct.domain.SearchCriteria;
import com.cct.exception.AuthenticateException;
import com.cct.exception.AuthorizationException;
import com.cct.exception.DuplicateException;
import com.cct.exception.MaxExceedAlertException;
import com.cct.exception.MaxExceedException;
import com.cct.exception.MaxExceedReportException;
import com.cct.exception.MessageException;
import com.cct.exception.UCPValidateException;
import com.cct.trn.core.config.parameter.domain.ParameterConfig;
import com.cct.trn.core.log.service.LogManager;
import com.cct.trn.core.security.authorization.domain.Authorize;
import com.cct.trn.core.security.authorization.domain.PFCode;
import com.cct.trn.core.security.authorization.service.AuthorizationService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

public class CommonAction extends ActionSupport implements Serializable {

	private static final long serialVersionUID = 2843485154046138037L;

	private Logger log = loggerInititial();

	protected Logger loggerInititial() {
		return LogUtil.COMMON;
	}

	public String[] LPP = ParameterConfig.getParameter().getApplication().getLpp();

	public Authorize ATH = new Authorize();
	public PFCode PF_CODE;
	public String P_CODE;
	public String F_CODE;

	private boolean useAddMenu = true;
	private boolean useProfileMenu = true;
	private boolean useHomeMenu = true;
	private boolean useRefreshMenu = true;
	private String alertMaxExceed = MaxExceedType.NORMAL.getType();

	private int LIMIT_LINE_ERROR = 10;
	public static String MESSAGE = "message";
	public static String MESSAGE_ALERT = "message_alert";
	public static String MESSAGE_ALERT_SEARCHDO;

	public AuthorizationService AUTHORIZATION_SERVICE = new AuthorizationService();

	public enum MaxExceedType {
		ALERT("A"), CONFIRM("C"), NORMAL("N");

		private String type;

		private MaxExceedType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	/**
	 * @Description: Class enum for message Code
	 */
	public enum MessageCode {
		MSG_30011("30011"), MSG_30014("30014"), MSG_30018("30018");

		private String type;

		private MessageCode(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	public enum FileType {
		WORD("application/msword"), EXCEL("application/ms-excel"), PDF("application/pdf");
		private String type;

		private FileType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	/**
	 * @Description: Constuctor for load user and locale
	 */
	public CommonAction() {

		if (SessionUtil.get(MESSAGE) == null && SessionUtil.get(MESSAGE_ALERT) == null) {
			clearMessages();

		} else if (SessionUtil.get(MESSAGE) != null) {
			clearMessages();
			SessionUtil.remove(MESSAGE_ALERT);

			Object object = SessionUtil.get(MESSAGE);
			if ((object instanceof String[]) == true) {
				String[] messages = (String[]) object;
				for (String message : messages) {
					addActionMessage(message);
				}
			} else {
				addActionMessage(object.toString());
			}
			SessionUtil.remove(MESSAGE);
		} else if (SessionUtil.get(MESSAGE_ALERT) != null) {
			clearMessages();
			Object object = SessionUtil.get(MESSAGE_ALERT);
			addActionMessage(object.toString());
			SessionUtil.remove(MESSAGE_ALERT);
		}
	}

	/*
	 * Arunya.k เพื่อให้งาน getLocal ใช้ จะได้ไม่ไปรวมกับงาน
	 * Connectionและงานอื่นๆ
	 */
	public CommonAction(boolean b) {

	}

	/**
	 * ดึง user จาก session
	 *
	 * @return
	 */
	public CommonUser getUser() {
		return (CommonUser) SessionUtil.get(CommonUser.DEFAULT_SESSION_ATTRIBUTE);
	}

	/**
	 * @Description: Class enum for return result
	 */
	public enum ReturnType {
		MAINPAGE("mainpage")
		, INIT("init")
		, SEARCH("search")
		, SEARCH_DO("searchDo")
		, DOWNLOAD(null)
		, UPLOAD("upload")
		, ADD_EDIT("addEdit")
		, CONFIRM_ADD_EDIT("confirmAddEdit")
		, ADD_DO("addDo")
		, EDIT_DO("editDo")
		, HOME("home")
		, THIS(null)
		, VIEW("view")
		, DELETE("delete")
		, SEARCH_AJAX("searchResultAjax")
		, LOGIN("login")
		, PROFILE("profile");

		private String result;

		private ReturnType(String result) {
			this.result = result;
		}

		public String getResult() {
			return result;
		}
	}

	/**
	 * @Description: Class enum for message type
	 */
	public enum MessageType {
		ERROR("E")
		, SUCCESS("S")
		, WARING("W")
		, CONFIRM("C")
		, DUP("D");// ใช้สำหรับ Data table กรณี update Active, Inactive, ResetPassword, delete (เพื่อให้แสดง Table result)

		private String type;

		private MessageType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	/**
	 * @Description: Class enum for return result
	 */
	public enum ResultType {
		CHAIN("chain")
		, BASIC("")
		, NULL("");

		private String type;

		private ResultType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	/**
	 * @Description: Load authorization
	 * @Return: void
	 */
	public void getAuthorization(PFCode pfcode) throws Exception {

		if (getUser() == null) {
			log.debug("Session expired. , sessionId: " + SessionUtil.getId() + " , user: " + getUser());
			throw new AuthorizationException(GlobalVariable.Message.SESSION_EXPIRED.getValue());
		}
		this.ATH = AUTHORIZATION_SERVICE.checkAuthorize(pfcode, getUser().getMapOperator());
		this.P_CODE = pfcode.getProgramCode();
		this.PF_CODE = pfcode;
	}

	public void checkAuthorization(String function) throws AuthorizationException {

		try {
			this.F_CODE = null;

			if (function.equals(PF_CODE.getSearchFunction())) {
				if (ATH.isSearch()) {
					F_CODE = PF_CODE.getSearchFunction();
				}
			} else if (function.equals(PF_CODE.getAddFunction())) {
				if (ATH.isAdd()) {
					F_CODE = PF_CODE.getAddFunction();
				}
			} else if (function.equals(PF_CODE.getEditFunction())) {
				if (ATH.isEdit()) {
					F_CODE = PF_CODE.getEditFunction();
				}
			} else if (function.equals(PF_CODE.getViewFunction())) {
				if (ATH.isView()) {
					F_CODE = PF_CODE.getViewFunction();
				}
			} else if (function.equals(PF_CODE.getDeleteFunction())) {
				if (ATH.isDelete()) {
					F_CODE = PF_CODE.getDeleteFunction();
				}
			} else if (function.equals(PF_CODE.getChangeFunction())) {
				if (ATH.isChange()) {
					F_CODE = PF_CODE.getChangeFunction();
				}
			} else if (function.equals(PF_CODE.getPrintFunction())) {
				if (ATH.isPrint()) {
					F_CODE = PF_CODE.getPrintFunction();
				}
			} else if (function.equals(PF_CODE.getImportFunction())) {
				if (ATH.isImport()) {
					F_CODE = PF_CODE.getImportFunction();
				}
			} else if (function.equals(PF_CODE.getExportFunction())) {
				if (ATH.isExport()) {
					F_CODE = PF_CODE.getExportFunction();
				}
			} else if (function.equals(PF_CODE.getResetPasswordFunction())) {
				if (ATH.isResetPassword()) {
					F_CODE = PF_CODE.getResetPasswordFunction();
				}
			} else if (function.equals(PF_CODE.getConfirmAddFunction())) {
				if (ATH.isConfirmAdd()) {
					F_CODE = PF_CODE.getConfirmAddFunction();
				}
			} else if (function.equals(PF_CODE.getConfirmEditFunction())) {
				if (ATH.isConfirmEdit()) {
					F_CODE = PF_CODE.getConfirmEditFunction();
				}
			} else if (function.equals(PF_CODE.getActiveFunction())) {
				if (ATH.isActive()) {
					F_CODE = PF_CODE.getActiveFunction();
				}
			} else if (function.equals(PF_CODE.getInActiveFunction())) {
				if (ATH.isInActive()) {
					F_CODE = PF_CODE.getInActiveFunction();
				}
			} else {
				throw new AuthorizationException();
			}

			if (F_CODE == null) {
				throw new AuthorizationException();
			}

		} catch (AuthorizationException e) {
			setMessageAlert(GlobalVariable.Message.NO_PERMISSIONS.getValue(), ResultType.CHAIN);
			throw e;
		} catch (Exception e) {
			log.error("", e);
			setMessage(CommonAction.MessageType.ERROR, GlobalVariable.Message.SERVER_ERROR.getValue(), getErrorMessage(e), ResultType.CHAIN);
			AuthorizationException afe = new AuthorizationException();
			afe.setStackTrace(e.getStackTrace());
			throw afe;
		}
	}

	public void clearSearchCriteria(String className) {
		try {
			Map<String, Object> sessions = SessionUtil.get();
			for (String key : sessions.keySet()) {
				log.debug("Key :- " + key);
				log.debug("Value :- " + sessions.get(key));
				log.debug("Class :- [" + sessions.get(key).getClass().getName() + " == " + className + "]");
				if (sessions.get(key).getClass().getName().equals(className)) {
					log.debug("Remove :- [" + key + "]");
					SessionUtil.remove(key);
				}
			}
			log.debug("------");
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public String getErrorMessage(Exception e) {
		String error = e.getMessage() + "<br>";
		for (int i = 0; i < LIMIT_LINE_ERROR; i++) {
			if (e.getStackTrace().length <= i) {
				break;
			} else {
				error += e.getStackTrace()[i] + "<br>";
			}
		}
		return error;
	}

	public void setMessage(MessageType type, String subject, ResultType resultType) {
		setMessage(type, subject, null, resultType);
	}

	public void setMessageAlert(String message, ResultType resultType) {

		if (resultType.equals(ResultType.CHAIN)) {
			SessionUtil.put(MESSAGE_ALERT, message);
		} else {
			SessionUtil.remove(MESSAGE_ALERT);
			clearMessages();
			addActionMessage(message);
		}
	}

	public void setMessage(MessageType type, String subject, String detail, ResultType resultType) {
		if (resultType.equals(ResultType.CHAIN)) {
			String[] messages = { type.getType(), subject, detail };
			SessionUtil.put(MESSAGE, messages);
		} else {
			clearMessagesException();
			addActionMessage(type.getType());
			addActionMessage(subject);
			addActionMessage(detail);
		}
	}

	/**
	 * Set message for datatable
	 */
	public void setMessage(CommonModel model, MessageType type, String subject, String detail) {
		model.getMessageAjax().setMessageType(type.getType());
		model.getMessageAjax().setMessage(subject);
		model.getMessageAjax().setMessageDetail(detail);
	}

	/**
	 * Set message for datatable
	 */
	public void setMessageAlert(CommonModel model, MaxExceedType type, String msg) {
		model.getMessageAjax().setMessageType(type.getType());
		model.getMessageAjax().setMessage(msg);
	}

	private void clearMessagesException() {
		SessionUtil.remove(MESSAGE);
		clearMessages();
	}

	protected void setMessageConfrimMaxExceed(String message) {
		alertMaxExceed = MaxExceedType.CONFIRM.getType();
		clearMessagesException();
		addActionMessage(message);
	}

	protected void setMessageAlertMaxExceed(String message) {
		alertMaxExceed = MaxExceedType.ALERT.getType();
		clearMessagesException();
		addActionMessage(message);
	}

	public String getP_CODE() {
		return P_CODE;
	}

	public void setP_CODE(String p_CODE) {
		P_CODE = p_CODE;
	}

	public String getF_CODE() {
		return F_CODE;
	}

	public void setF_CODE(String f_CODE) {
		F_CODE = f_CODE;
	}

	public boolean isUseAddMenu() {
		return useAddMenu;
	}

	public void setUseAddMenu(boolean useAddMenu) {
		this.useAddMenu = useAddMenu;
	}

	public boolean isUseProfileMenu() {
		return useProfileMenu;
	}

	public void setUseProfileMenu(boolean useProfileMenu) {
		this.useProfileMenu = useProfileMenu;
	}

	public boolean isUseHomeMenu() {
		return useHomeMenu;
	}

	public void setUseHomeMenu(boolean useHomeMenu) {
		this.useHomeMenu = useHomeMenu;
	}

	public boolean isUseRefreshMenu() {
		return useRefreshMenu;
	}

	public void setUseRefreshMenu(boolean useRefreshMenu) {
		this.useRefreshMenu = useRefreshMenu;
	}

	/**
	 * initial ระบบกรณีที่ Initial ที่มีเงื่อนไข return ไปที่ init
	 *
	 * @param conn
	 * @param model
	 * @param criteria
	 * @param function
	 * @param pageType
	 * @return
	 * @throws Exception
	 */
	public String manageInitial(CCTConnection conn, CommonModel model, SearchCriteria criteria, String function, PageType pageType) throws Exception {

		checkAuthorization(function);

		String result = ReturnType.INIT.getResult();

		model.setPage(pageType);

		if (model.getCriteria().getCriteriaKey() != null && (model.getCriteria().getCriteriaKey().isEmpty() == false)) {
			SessionUtil.remove(model.getCriteria().getCriteriaKey());
			model.getCriteria().setCriteriaKey("");
		}

		model.setCriteria(criteria);

		model.getCriteria().setStart(1);
		model.getCriteria().setLinePerPage(Integer.parseInt(LPP[0]));
		model.getCriteria().setCheckMaxExceed(true);

		/**
		 * Anusorn.l 2015-06-19 เพิ่มการเก็บตัวแปร orderSortsSelect ใช้สำหรับ
		 * datatable
		 **/
		model.getCriteria().setDefaultHeaderSorts();

		if (model.getCriteria().getHeaderSortsSelect() != null) {
			String[] hSelect = model.getCriteria().getHeaderSortsSelect().split(",");
			String order = "";
			for (String head : hSelect) {
				order += "," + model.getCriteria().getHeaderSorts()[Integer.parseInt(head)].getOrder();
			}
			model.getCriteria().setOrderSortsSelect(order.substring(1));
		}

		if (function != null) {
//			LogManager logManager = new LogManager(conn, log);
//			logManager.addEvent(conn, function, getUserIdFromSession(), model);
		}
		return result;
	}

	/**
	 * initial ระบบกรณีที่ต้องการหน้าแรกไม่มี criteria เงื่อนไข return ไปที่
	 * init
	 *
	 * @param conn
	 * @param model
	 * @param function
	 * @param pageType
	 * @return
	 * @throws Exception
	 */
	public String manageInitial(CCTConnection conn, CommonModel model, String function, PageType pageType) throws Exception {

		checkAuthorization(function);

		String result = ReturnType.INIT.getResult();

		model.setPage(pageType);

		if (function != null) {
//			LogManager logManager = new LogManager(conn, log);
//			logManager.addEvent(conn, function, getUserIdFromSession(), model);
		}
		return result;
	}

	/**
	 * Arunya.k ยุบ Code มาไว้ที่เดียว Action ที่ Extend ไปได้มี Code ไม่ยาว
	 * สำหรับตรวจสอบสิทธิ์หน้าแก้ไข, จัดการเงือนไขการค้นหา และ return ไปที่
	 * search
	 *
	 * @param conn
	 * @param model
	 * @param criteriaModel
	 * @param function
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageSearch(CCTConnection conn, CommonModel model, SearchCriteria criteriaModel, String function) throws AuthorizationException {
		checkAuthorization(function);
		String result = ReturnType.SEARCH.getResult();

		if ((model.getCriteria().getCriteriaKey() == null) || (model.getCriteria().getCriteriaKey().equals(""))) {
			model.getCriteria().setDefaultHeaderSorts();
			// เพิ่มเก็บ url ของ search action เพื่อ ให้ตัวกลางวาด table ใช้ตอน page navigate
			ActionMapping actionMapping = (ActionMapping) ActionContext.getContext().get("struts.actionMapping");
			model.getCriteria().setUrlSearchAction(actionMapping.getNamespace() + "/" + actionMapping.getName() + "." + actionMapping.getExtension());
		}

		model.setPage(CommonModel.PageType.SEARCH);
		if ((criteriaModel.getCriteriaKey() == null) || (criteriaModel.getCriteriaKey().equals(""))) {
			log.debug("3. Check criteria...[GENERATE]");
			// clearSearchCriteria(criteriaModel.getClass().getName()); แก้ไข error ตอนเปิด tab ใหม่
			String criteriaKey = String.valueOf(Calendar.getInstance().getTimeInMillis());
			log.debug("3.1 Check criteria...[GENERATE] , criteriaKey : " + criteriaKey + " , userId: " + getUser().getUserId());
			criteriaModel.setCriteriaKey(criteriaKey);

			criteriaModel.setStart(1);
			criteriaModel.setCheckMaxExceed(true);

			SessionUtil.put(criteriaModel.getCriteriaKey(), criteriaModel);
		} else {
			log.debug("3. Check criteria...[LOAD]: " + criteriaModel.getCriteriaKey() + ", sessionId: " + SessionUtil.getId() + " , userId: " + getUser().getUserId());
			SearchCriteria criteriaSession = (SearchCriteria) SessionUtil.get(criteriaModel.getCriteriaKey());
			log.debug("4. criteriaSession: " + criteriaSession);
			criteriaSession.setCriteriaKey(criteriaModel.getCriteriaKey());

			if (criteriaModel.getStart() > 0) {
				criteriaSession.setStart(criteriaModel.getStart());
				criteriaSession.setCheckMaxExceed(criteriaModel.isCheckMaxExceed());
			}

			log.debug("   Check header sorts select...[" + criteriaModel.getHeaderSortsSelect() + "]");
			if (criteriaModel.getHeaderSortsSelect() != null) {
				criteriaSession.setHeaderSorts(criteriaModel.getHeaderSorts());
				criteriaSession.setHeaderSortsSelect(criteriaModel.getHeaderSortsSelect());
			}

			criteriaModel = criteriaSession;
			model.setCriteria(criteriaModel);

		}

		if (function != null) {
//			LogManager logManager = new LogManager(conn, log);
//			logManager.addEvent(conn, function, getUserIdFromSession(), model);
		}

		return result;
	}

	/**
	 * manageSearchAjax
	 *
	 * @param conn
	 * @param model
	 * @param criteriaModel
	 * @param function
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageSearchAjax(CCTConnection conn, CommonModel model, SearchCriteria criteriaModel, String function) throws AuthorizationException {

		checkAuthorization(function);
		String result = ReturnType.SEARCH_AJAX.getResult();

		if ((model.getCriteria().getCriteriaKey() == null) || (model.getCriteria().getCriteriaKey().equals(""))) {
			model.getCriteria().setDefaultHeaderSorts();

			/**
			 * Anusorn.l 2015-06-19 เพิ่มการเก็บตัวแปร orderSortsSelect
			 * ใช้สำหรับ datatable
			 **/
			model.getCriteria().setDefaultHeaderSorts();

			if (model.getCriteria().getHeaderSortsSelect() != null && !model.getCriteria().getHeaderSortsSelect().equals("")) {
				String[] hSelect = model.getCriteria().getHeaderSortsSelect().split(",");
				String order = "";
				for (String head : hSelect) {
					order += "," + model.getCriteria().getHeaderSorts()[Integer.parseInt(head)].getOrder();
				}
				model.getCriteria().setOrderSortsSelect(order.substring(1));
			}
			/** Anusorn.l 2015-06-19 **/

			// เพิ่มเก็บ url ของ search action เพื่อ ให้ตัวกลางวาด table ใช้ตอน page navigate
			ActionMapping actionMapping = (ActionMapping) ActionContext.getContext().get("struts.actionMapping");
			model.getCriteria().setUrlSearchAction(actionMapping.getNamespace() + "/" + actionMapping.getName() + "." + actionMapping.getExtension());
		}

		model.setPage(CommonModel.PageType.SEARCH);
		if ((criteriaModel.getCriteriaKey() == null) || (criteriaModel.getCriteriaKey().equals(""))) {
			log.info("3. Check criteria...[GENERATE]");
			// clearSearchCriteria(criteriaModel.getClass().getName()); comment เพื่อทดสอบ BlacklistAlert Criteria ใน Session หาย
			String criteriaKey = String.valueOf(Calendar.getInstance().getTimeInMillis());
			log.info("3.1 Check criteria...[GENERATE] , criteriaKey : " + criteriaKey + " , userId: " + getUser().getUserId());

			criteriaModel.setCriteriaKey(criteriaKey);

			criteriaModel.setStart(1);
			//criteriaModel.setCheckMaxExceed(true);
			
			if(criteriaModel.isAlertMaxExceed()) {
				criteriaModel.setCheckMaxExceed(false);
				criteriaModel.setAlertMaxExceed(false);
			} else {
				criteriaModel.setCheckMaxExceed(true);
			}

			SessionUtil.put(criteriaModel.getCriteriaKey(), criteriaModel);
		} else {
			log.info("3. Check criteria...[LOAD]: " + criteriaModel.getCriteriaKey() + ", sessionId: " + SessionUtil.getId() + ", userId: " + getUser().getUserId());
			SearchCriteria criteriaSession = (SearchCriteria) SessionUtil.get(criteriaModel.getCriteriaKey());
			log.info("4. criteriaSession: " + criteriaSession);

			// แก้ไขกรณี criteriaKey มี แต่ criteriaSession ไม่มี
			if (criteriaSession == null) {
				criteriaSession = criteriaModel;
				SessionUtil.put(criteriaModel.getCriteriaKey(), criteriaModel);
				log.info("5. criteriaSession: is null set criteriaSession = criteriaModel. " + criteriaSession);
			}

			criteriaSession.setCriteriaKey(criteriaModel.getCriteriaKey());

			String startStr = (String) SessionUtil.requestParameter("start");
			int start = startStr == null ? 0 : Integer.parseInt(startStr);
			if (criteriaModel.getStart() > 0) { // มาจากหน้าแก้ไขจะไม่เข้า if
				criteriaSession.setStart(start + 1);
				criteriaSession.setCheckMaxExceed(criteriaModel.isCheckMaxExceed());
			}

			int orderableCount = 0;
			String orderCol[] = null;
			String orderDir[] = null;
			if (model.getCriteria().getHeaderSortsSelect() != null) {
				orderableCount = model.getCriteria().getHeaderSortsSelect().split(",").length;

				// จำนวน column sorting
				orderCol = new String[orderableCount];
				orderDir = new String[orderableCount];
				for (int i = 0; i < orderableCount; i++) {
					orderCol[i] = (String) SessionUtil.requestParameter("order[" + i + "][column]");
					orderDir[i] = (String) SessionUtil.requestParameter("order[" + i + "][dir]");
				}

				StringBuilder headerSortsSelect = new StringBuilder();
				StringBuilder orderSortsSelect = new StringBuilder();
				for (int i = 0; i < orderCol.length; i++) {
					if (orderCol[i] != null) {
						headerSortsSelect.append(",").append(orderCol[i]);
						orderSortsSelect.append(",").append(orderDir[i]);
						criteriaModel.getHeaderSorts()[Integer.parseInt(orderCol[i])].setOrder(orderDir[i].toUpperCase());
					}
				}

				// log.debug("   Check header sorts select...[" + criteriaModel.getHeaderSortsSelect() + "]");
				if (criteriaModel.getHeaderSortsSelect() != null && !criteriaModel.getHeaderSortsSelect().equals("") && !criteriaModel.getHeaderSortsSelect().equals("0")) {
					criteriaSession.setHeaderSorts(criteriaModel.getHeaderSorts());
					criteriaSession.setHeaderSortsSelect(headerSortsSelect.toString().isEmpty() ? "" : headerSortsSelect.toString().substring(1));
					criteriaSession.setOrderSortsSelect(orderSortsSelect.toString().isEmpty() ? "" : orderSortsSelect.toString().substring(1));
				}
			} else if (criteriaSession.getHeaderSortsSelect() != null && !criteriaSession.getHeaderSortsSelect().equals("")) {
				String[] hSelect = criteriaSession.getHeaderSortsSelect().split(",");
				String order = "";
				for (String head : hSelect) {
					order += "," + model.getCriteria().getHeaderSorts()[Integer.parseInt(head)].getOrder();
				}
				model.getCriteria().setOrderSortsSelect(order.substring(1));
			}

			criteriaModel = criteriaSession;
			model.setCriteria(criteriaModel);

		}

		if (function != null) {
//			LogManager logManager = new LogManager(conn, log);
//			logManager.addEvent(conn, function, getUserIdFromSession(), model);
		}

		return result;
	}

	/**
	 * สำหรับจัดการผลลัพธ์การค้นหา กรณีไม่พบข้อมูลให้แสดง message
	 * ไม่พบข้อมูลที่ต้องการ
	 *
	 * @param model
	 * @param lstResult
	 * @throws Exception
	 */
	public void manageSearchResult(CommonModel model, List<?> lstResult) throws Exception {
		manageSearchResult(model, lstResult, ResultType.BASIC);
	}

	/**
	 * สำหรับจัดการผลลัพธ์การค้นหา กรณีไม่พบข้อมูลให้แสดง message
	 * ไม่พบข้อมูลที่ต้องการ
	 *
	 * @param model
	 * @param lstResult
	 * @param resultType
	 * @throws Exception
	 */
	public void manageSearchResult(CommonModel model, List<?> lstResult, ResultType resultType) throws Exception {
		try {
			if (lstResult == null || lstResult.size() == 0) {
				setMessage(model, CommonAction.MessageType.WARING, getText("30004"), "");
				setMessage(CommonAction.MessageType.WARING, getText("30004"), resultType);
			} else {
				model.setLstResult(lstResult);
				model.setRecordsTotal(model.getCriteria().getTotalResult());
				model.setRecordsFiltered(model.getCriteria().getTotalResult());
				model.setResult(lstResult);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * สำหรับจัดการก่อนเข้าหน้าเพิ่ม return ไปที่ addEdit
	 *
	 * @param conn
	 * @param model
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageGotoAdd(CCTConnection conn, CommonModel model) throws AuthorizationException {

		checkAuthorization(PF_CODE.getAddFunction());

		String result = ReturnType.ADD_EDIT.getResult();

		model.setPage(CommonModel.PageType.ADD);

//		LogManager logManager = new LogManager(conn, log);
//		logManager.addEvent(conn, PF_CODE.getAddFunction(), getUserIdFromSession(), model);

		return result;
	}

	/**
	 * สำหรับจัดการหลังบันทึกเพิ่มเสร็จ return ไปที่ addEdit
	 *
	 * @param conn
	 * @param model
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageAdd(CCTConnection conn, CommonModel model) throws AuthorizationException {
		return manageAdd(conn, model, ResultType.BASIC);
	}

	/**
	 * สำหรับจัดการหลังบันทึกเพิ่มเสร็จ return ไปที่ addEdit
	 *
	 * @param conn
	 * @param model
	 * @param resultType
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageAdd(CCTConnection conn, CommonModel model, ResultType resultType) throws AuthorizationException {

		checkAuthorization(PF_CODE.getAddFunction());

		String result = ReturnType.ADD_EDIT.getResult();

		setMessage(CommonAction.MessageType.SUCCESS, getText("30003"), resultType);

		model.setPage(CommonModel.PageType.ADD);

//		LogManager logManager = new LogManager(conn, log);
//		logManager.addEvent(conn, PF_CODE.getAddFunction(), getUserIdFromSession(), model);

		return result;
	}

	/**
	 * สำหรับจัดการก่อนเข้าหน้าแก้ไข return ไปที่ addEdit
	 *
	 * @param conn
	 * @param model
	 * @return
	 * @throws AuthorizationException
	 * @throws IOException
	 * @throws DuplicateException
	 */
	public String manageGotoEdit(CCTConnection conn, CommonModel model) throws AuthorizationException, IOException {

		checkAuthorization(PF_CODE.getEditFunction());

		if (model.getCriteria() != null && model.getCriteria().getCriteriaKey() == null) {
			String urlInit = ServletActionContext.getRequest().getContextPath() + ServletActionContext.getActionMapping().getNamespace() + "/"
					+ ServletActionContext.getActionMapping().getName().replace("gotoEdit", "init") + ".action";
			ServletActionContext.getResponse().sendRedirect(urlInit);
		}

		String result = ReturnType.ADD_EDIT.getResult();

		model.setPage(CommonModel.PageType.EDIT);

		model.setUrlEdit(ServletActionContext.getRequest().getContextPath() + ServletActionContext.getActionMapping().getNamespace() + "/"
				+ ServletActionContext.getActionMapping().getName() + ".action");

//		LogManager logManager = new LogManager(conn, log);
//		logManager.addEvent(conn, PF_CODE.getEditFunction(), getUserIdFromSession(), model);

		return result;
	}

	/**
	 * สำหรับจัดการหลังบันทึกแ้ก้ไขเสร็จ return ไปที่ searchDo
	 *
	 * @param conn
	 * @param model
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageEdit(CCTConnection conn, CommonModel model) throws AuthorizationException {
		return manageEdit(conn, model, ResultType.CHAIN);
	}

	/**
	 * สำหรับจัดการหลังบันทึกแ้ก้ไขเสร็จ return ไปที่ searchDo
	 *
	 * @param conn
	 * @param model
	 * @param resultType
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageEdit(CCTConnection conn, CommonModel model, ResultType resultType) throws AuthorizationException {

		checkAuthorization(PF_CODE.getEditFunction());

		String result = ReturnType.SEARCH_DO.getResult();

		setMessage(CommonAction.MessageType.SUCCESS, getText("30004"), resultType);

		model.setPage(CommonModel.PageType.EDIT);

//		LogManager logManager = new LogManager(conn, log);
//		logManager.addEvent(conn, PF_CODE.getEditFunction(), getUserIdFromSession(), model);

		return result;
	}

	/**
	 * สำหรับจัดการก่อนเข้าหน้าแสดง return ไปที่ addEdit
	 *
	 * @param conn
	 * @param model
	 * @return
	 * @throws AuthorizationException
	 * @throws IOException
	 */
	public String manageGotoView(CCTConnection conn, CommonModel model) throws AuthorizationException, IOException {

		checkAuthorization(PF_CODE.getViewFunction());

		if (model.getCriteria() != null && model.getCriteria().getCriteriaKey() == null) {
			String urlInit = ServletActionContext.getRequest().getContextPath() + ServletActionContext.getActionMapping().getNamespace() + "/"
					+ ServletActionContext.getActionMapping().getName().replace("gotoView", "init") + ".action";
			ServletActionContext.getResponse().sendRedirect(urlInit);
		}

		String result = ReturnType.ADD_EDIT.getResult();

		model.setPage(CommonModel.PageType.VIEW);

//		LogManager logManager = new LogManager(conn, log);
//		logManager.addEvent(conn, PF_CODE.getViewFunction(), getUserIdFromSession(), model);

		return result;
	}

	/**
	 * สำหรับจัดการหลังบันทึกสถานะเสร็จ return ไปที่ searchDo
	 *
	 * @param conn
	 * @param model
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageUpdateActive(CCTConnection conn, CommonModel model) throws AuthorizationException {
		return manageUpdateActive(conn, model, ResultType.CHAIN);
	}

	/**
	 * สำหรับจัดการหลังบันทึกสถานะเสร็จ return ไปที่ searchDo
	 *
	 * @param conn
	 * @param model
	 * @param resultType
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageUpdateActive(CCTConnection conn, CommonModel model, ResultType resultType) throws AuthorizationException {

		checkAuthorization(PF_CODE.getChangeFunction());

		String result = ReturnType.SEARCH_DO.getResult();

		if (model.getCriteria().getStatusForUpdate().equals("Y")) {
			setMessage(model, CommonAction.MessageType.SUCCESS, getText("30001"), "");
			setMessage(CommonAction.MessageType.SUCCESS, getText("30001"), resultType);

		} else if (model.getCriteria().getStatusForUpdate().equals("N")) {
			setMessage(model, CommonAction.MessageType.SUCCESS, getText("30002"), "");
			setMessage(CommonAction.MessageType.SUCCESS, getText("30002"), resultType);

		} else if (model.getCriteria().getStatusForUpdate().equals("P")) {
			// กรณีที่ เป็นการระงับการใช้งาน ACTIVE = P Msg = 30019 :
			// เปลี่ยนเป็นสถานะระงับใช้งานเรียบร้อยแล้ว
			setMessage(model, CommonAction.MessageType.SUCCESS, getText("30019"), "");
			setMessage(CommonAction.MessageType.SUCCESS, getText("30019"), resultType);
		}

//		LogManager logManager = new LogManager(conn, log);
//		logManager.addEvent(conn, PF_CODE.getChangeFunction(), getUserIdFromSession(), model);

		return result;
	}

	/**
	 * สำหรับจัดการหลังบันทึกสถานะเสร็จ return ไปที่ searchDo
	 *
	 * @param conn
	 * @param model
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageDelete(CCTConnection conn, CommonModel model) throws AuthorizationException {
		return manageDelete(conn, model, ResultType.CHAIN);
	}

	/**
	 * สำหรับจัดการหลังบันทึกสถานะเสร็จ return ไปที่ searchDo
	 *
	 * @param conn
	 * @param model
	 * @param resultType
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageDelete(CCTConnection conn, CommonModel model, ResultType resultType) throws AuthorizationException {

		checkAuthorization(PF_CODE.getDeleteFunction());

		String result = ReturnType.SEARCH_DO.getResult();

		setMessage(CommonAction.MessageType.SUCCESS, getText("30005"), resultType);

//		LogManager logManager = new LogManager(conn, log);
//		logManager.addEvent(conn, PF_CODE.getDeleteFunction(), getUserIdFromSession(), model);

		return result;
	}

	/**
	 * สำหรับจัดการหลังส่งออกข้อมูลเสร็จ return ไปที่ download
	 *
	 * @param conn
	 * @param model
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageExport(CCTConnection conn, CommonModel model) throws AuthorizationException {
		String func = F_CODE;
		checkAuthorization(PF_CODE.getExportFunction());

		String result = ReturnType.DOWNLOAD.getResult();
		model.setPage(model.getPage());
		
		F_CODE = func;


		return result;
	}

	/**
	 * สำหรับจัดการหลังนำเข้าข้อมูลเสร็จ return ไปที่ upload
	 *
	 * @param conn
	 * @param model
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageImport(CCTConnection conn, CommonModel model) throws AuthorizationException {
		return manageImport(conn, model, ResultType.BASIC);
	}

	/**
	 * สำหรับจัดการหลังนำเข้าข้อมูลเสร็จ return ไปที่ upload
	 *
	 * @param conn
	 * @param model
	 * @param resultType
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageImport(CCTConnection conn, CommonModel model, ResultType resultType) throws AuthorizationException {

		checkAuthorization(PF_CODE.getImportFunction());

		String result = ReturnType.UPLOAD.getResult();

		setMessage(CommonAction.MessageType.SUCCESS, getText("30003"), resultType);

		model.setPage(CommonModel.PageType.ADD);

//		LogManager logManager = new LogManager(conn, log);
//		logManager.addEvent(conn, PF_CODE.getImportFunction(), getUserIdFromSession(), model);

		return result;
	}

	/**
	 * สำหรับจัดการหลังนำเข้าข้อมูลเสร็จ return ไปที่ upload
	 *
	 * @param conn
	 * @param model
	 * @param resultType
	 * @return
	 * @throws AuthorizationException
	 */
	public String manageOther(CCTConnection conn, String functionCode, ReturnType returnType, MessageType messageType, String messageCode, ResultType resultType,
			PageType pageType, CommonModel model) throws AuthorizationException {

		checkAuthorization(functionCode);

		String result = returnType.getResult();

		setMessage(model, messageType, getText(messageCode), "");
		setMessage(messageType, getText(messageCode), resultType);

		model.setPage(pageType);

//		LogManager logManager = new LogManager(conn, log);
//		logManager.addEvent(conn, functionCode, getUserIdFromSession(), model);

		return result;
	}

	/**
	 * จัดการ Error ต่างๆ
	 *
	 * @param conn
	 * @param operatorId
	 *            คือ function code
	 * @param className
	 *            คือ class ของ error นั้นๆ
	 * @param e
	 *            คือ exception error
	 * @param model
	 * @throws AuthorizationException
	 */
	public void manageException(CCTConnection conn, String operatorId, Object className, Exception e, CommonModel model) throws AuthorizationException, UCPValidateException {
		if (e instanceof MaxExceedException) {
			setMessageAlert(model, CommonAction.MaxExceedType.CONFIRM, getText(e.getMessage()).replace("xxx", String.valueOf(model.getCriteria().getTotalResult())));
			setMessageConfrimMaxExceed(getText(e.getMessage()).replace("xxx", String.valueOf(model.getCriteria().getTotalResult())));
		} else if (e instanceof MaxExceedAlertException) {
			setMessageAlert(model, CommonAction.MaxExceedType.ALERT, getText(e.getMessage()));
			setMessageAlertMaxExceed(getText(e.getMessage()));
		} else if (e instanceof MaxExceedReportException) {
			setMessageAlert(model, CommonAction.MaxExceedType.ALERT, getText(e.getMessage()));
			setMessageAlertMaxExceed(getText(e.getMessage()));
		} else if (e instanceof DuplicateException) {
			setMessage(model, CommonAction.MessageType.DUP, getText(e.getMessage()), "");
			setMessage(CommonAction.MessageType.WARING, getText(e.getMessage()), ResultType.BASIC);
		} else if (e instanceof AuthenticateException) {
			setMessage(CommonAction.MessageType.ERROR, e.getMessage(), ResultType.BASIC);
		} else if (e instanceof AuthorizationException) {
			throw (AuthorizationException) e;
		} else if (e instanceof UCPValidateException) {
			throw (UCPValidateException) e;
		} else {
			// suraphong.a
			if (operatorId != null && getUser() != null) {
				if (e instanceof MessageException && e.getCause() == null) {
					
				} else {
//					LogManager.addErrorLog(conn, operatorId, className, e, getUser());
				}
			}
			
			log.error("userId: " + getUserIdFromSession() + ", sessionId: " + SessionUtil.getId() + " , className: " + className + ", model: " + model);
			log.error("", e);
			setMessage(model, CommonAction.MessageType.ERROR, getText(e.getMessage()), getErrorMessage(e));
			setMessage(CommonAction.MessageType.ERROR, getText(e.getMessage()), getErrorMessage(e), ResultType.BASIC);
		}
	}

	/**
	 * จัดการ Error ต่างๆ โดยสามารถกำหนด ResultType และ MessageCode เพิ่มเติมได้
	 *
	 * @param conn
	 * @param operatorId
	 * @param className
	 * @param e
	 * @param model
	 * @param resultType
	 *            (ถ้าเป็นการกลับไปที่หน้า jsp ให้ใช้ Basic, ถ้าเป็นการข้าม
	 *            action ให้ใช้ Chain)
	 * @param messageDesc
	 *            (getText("30014").replace("xxx",
	 *            String.valueOf(model.getCriteria().getTotalResult())))
	 * @throws AuthorizationException
	 */
	public void manageException(CCTConnection conn, String operatorId, Object className, Exception e, CommonModel model, ResultType resultType)
			throws AuthorizationException, UCPValidateException {
		if (e instanceof MaxExceedException) {
			setMessageAlert(model, CommonAction.MaxExceedType.CONFIRM, getText(e.getMessage()).replace("xxx", String.valueOf(model.getCriteria().getTotalResult())));
			setMessageConfrimMaxExceed(getText(e.getMessage()).replace("xxx", String.valueOf(model.getCriteria().getTotalResult())));
		} else if (e instanceof MaxExceedAlertException) {
			setMessageAlert(model, CommonAction.MaxExceedType.ALERT, getText(e.getMessage()));
			setMessageAlertMaxExceed(getText(e.getMessage()));
		} else if (e instanceof MaxExceedReportException) {
			setMessageAlert(model, CommonAction.MaxExceedType.ALERT, getText(e.getMessage()));
			setMessageAlertMaxExceed(getText(e.getMessage()));
		} else if (e instanceof DuplicateException) {
			setMessage(model, CommonAction.MessageType.DUP, getText(e.getMessage()), "");
			setMessage(CommonAction.MessageType.WARING, getText(e.getMessage()), resultType);
		} else if (e instanceof AuthenticateException) {
			setMessage(CommonAction.MessageType.ERROR, getText(e.getMessage()), resultType);
		} else if (e instanceof AuthorizationException) {
			throw (AuthorizationException) e;
		} else if (e instanceof UCPValidateException) {
			throw (UCPValidateException) e;
		} else {
			// suraphong.a
			if (operatorId != null && getUser() != null) {
				if (e instanceof MessageException && e.getCause() == null) {
					
				} else {
					LogManager.addErrorLog(conn, operatorId, className, e, getUser());
				}
			}
			
			log.error("userId: " + getUserIdFromSession() + ", sessionId: " + SessionUtil.getId() + " , className: " + className + ", model: " + model);
			log.error("", e);
			setMessage(model, CommonAction.MessageType.ERROR, getText(e.getMessage()), getErrorMessage(e));
			setMessage(CommonAction.MessageType.ERROR, getText(e.getMessage()), getErrorMessage(e), resultType);
		}
	}

	public void exportFile(HttpServletResponse response, byte[] bytes, String fileName, FileType contentType) {
		exportFile(response, bytes, fileName, contentType.getType());
	}

	public void exportFile(HttpServletResponse response, byte[] bytes, String fileName, String contentType) {
		log.info("exportFile");
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType(contentType);
			response.setContentLength(bytes.length);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(bytes, 0, bytes.length);
			servletOutputStream.flush();
			servletOutputStream.close();

		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void exportFilePreview(HttpServletResponse response, byte[] bytes, String fileName, String contentType) {
		log.info("exportFile");
		try {
			response.setHeader("Content-Disposition", "inline;filename=" + fileName);
			response.setContentType(contentType);
			response.setContentLength(bytes.length);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(bytes, 0, bytes.length);
			servletOutputStream.flush();
			servletOutputStream.close();

		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void exportExcelFile(XSSFWorkbook workbook, String fileName) {
		log.info("exportExcelFile");
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/octet-stream");			
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public String getAlertMaxExceed() {
		return alertMaxExceed;
	}

	public void setAlertMaxExceed(String alertMaxExceed) {
		this.alertMaxExceed = alertMaxExceed;
	}

	public String getUserIdFromSession() {
		String userId = null;
		if (getUser() != null) {
			userId = getUser().getUserId();
		}
		return userId;
	}

	/**
	 * สำหรับ upload เอกสาร โดยไม่แสดง thumbnail
	 *
	 * @return String
	 */
	public String browseWithoutThumbnail() {
		log.debug("browseWithoutThumbnail");
		try {

			Object[] tmpFile = createTemp();

			ValueStack valueStack = (ValueStack) ServletActionContext.getRequest().getAttribute("struts.valueStack");
			CommonModel model = (CommonModel) valueStack.getRoot().get(0);

			FileManagerUtil.crateDirectoryWithoutOverwrite(ParameterConfig.getParameter().getAttachFile().getTmpPath());
			BrowseUploadServiceUtil.performBrowse(tmpFile[1].toString(), model.getFileMeta(), false);

			model.getFileMeta().setFileUploadFileNameTmp((String[]) tmpFile[0]);
		} catch (Exception e) {
			log.error("", e);
		}
		return "filemeta";
	}

	/**
	 * สำหรับ upload เอกสาร
	 *
	 * @return String
	 */
	public String browse() {
		log.debug("");
		try {

			Object[] tmpFile = createTemp();

			ValueStack valueStack = (ValueStack) ServletActionContext.getRequest().getAttribute("struts.valueStack");
			CommonModel model = (CommonModel) valueStack.getRoot().get(0);

			String[] fileThumbnail64 = BrowseUploadServiceUtil.performBrowse(tmpFile[1].toString(), model.getFileMeta());

			model.getFileMeta().setFileThumbnail(fileThumbnail64);
			model.getFileMeta().setFileUploadFileNameTmp((String[]) tmpFile[0]);
		} catch (Exception e) {
			log.error("", e);
		}
		return "filemeta";
	}

	/**
	 *
	 * @return object-0: filename, object-1: filename with path
	 * @throws Exception
	 */
	private Object[] createTemp() throws Exception {

		Object[] returnTmp = new Object[2];

		String[] tmpFile = { Calendar.getInstance(ParameterConfig.getParameter().getApplication().getDatabaseLocale()).getTimeInMillis() + ".tmp" };
		String tmpFilePath = ParameterConfig.getParameter().getAttachFile().getTmpPath() + tmpFile[0];

		try {
			log.debug("create tmp: " + tmpFilePath);
			FileManagerUtil.crateDirectoryWithoutOverwrite(ParameterConfig.getParameter().getAttachFile().getTmpPath());

			returnTmp[0] = tmpFile;
			returnTmp[1] = tmpFilePath;

		} catch (Exception e) {
			throw e;
		}
		return returnTmp;
	}

	/**
	 * Clear temporary file for single upload only
	 *
	 * @return
	 */
	public String clearTemp() {
		String fileDelete = SessionUtil.requestParameter("name");
		try {
			if (fileDelete != null && !fileDelete.equals("")) {
				FileManagerUtil.deleteQuietly(ParameterConfig.getParameter().getAttachFile().getTmpPath() + fileDelete);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return "filemeta";
	}

	public void setMessagePopup(CommonModel model, MessageCode msgCode, String msgDecs) {

		if (msgCode != null) {
			// กรณีมีค่า MSG_CODE
			switch (msgCode) {
			case MSG_30011:
				// ไม่พบข้อมูลที่ต้องกาาร
				model.setMessagePopup(MessageType.WARING + "::" + getText("30011"));
				break;
			case MSG_30014:
				// จำนวนข้อมูลที่ค้นพบ = xxx รายการ ต้องการแสดงข้อมูลหรือไม่ ?
				model.setMessagePopup(getText("30014").replace("xxx", msgDecs));
				break;
			case MSG_30018:
				// จำนวนข้อมูลที่ค้นพบมีจำนวนมาก กรุณาระบุเงื่อนไขในการค้นหา
				model.setMessagePopup(MessageType.WARING + "::" + getText("30018"));
				break;
			default:
				break;
			}
		} else {
			// กรณี Exception
			model.setMessagePopup(MessageType.ERROR + "::" + getText("30010") + "::" + msgDecs);
		}

	}

	public void manageSearchResultPopup(CommonModel model, SearchCriteria criteria, List<?> listResultPopup) throws Exception {
		try {
			if (listResultPopup == null || listResultPopup.size() == 0) {
				setMessagePopup(model, MessageCode.MSG_30011, "");
				criteria.setStart(1);
				criteria.setTotalResult(0);
			} else {

			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void manageExceedExceptionPopup(CommonModel model, SearchCriteria criteria) {
		if (criteria.getNavigatePopup().equals("true")) {
			setMessagePopup(model, MessageCode.MSG_30014, String.valueOf(criteria.getTotalResult()));
		} else {
			setMessagePopup(model, MessageCode.MSG_30018, "");
		}
	}

	public void manageInitialPopup(SearchCriteria criteria) throws Exception {

		try {
			criteria.setStart(1);
			criteria.setLinePerPage(ParameterConfig.getParameter().getApplication().getLppPopup());
			criteria.setTotalResult(0);
		} catch (Exception e) {
			throw e;
		}

	}

	public PFCode getPF_CODE() {
		return PF_CODE;
	}

}
