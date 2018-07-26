/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : COMBOBOX_PREFIX_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
SELECT 
  PREFIX_ID 
  , PREFIX_NAME
FROM TRN_PREFIX
ORDER BY PREFIX_NAME

/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : AUTOCOMPLETE_แผนก_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
SELECT DEPARTMENT_ID
	, DEPARTMENT_NAME
FROM TRN_DEPARTMENT
WHERE ACTIVE = 'Y'
AND DEPARTMENT_NAME LIKE CONCAT(&DEPARTMENT_NAME, '%')		/* DEPARTMENT_NAME */
LIMIT &LIMIT_NUM

/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : AUTOCOMPLETE_ตำแหน่ง_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
SELECT POSITION_ID
, POSITION_NAME
FROM TRN_POSITION
WHERE ACTIVE = 'Y'
AND POSITION_NAME LIKE CONCAT(&POSITION_NAME, '%') 
AND DEPARTMENT_ID = &DEPARTMENT_ID
LIMIT &LIMIT_NUM

/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : นับจำนวนข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
searchCount {
	SELECT COUNT(1) AS TOT
		FROM TRN_EMPLOYEE EMP
	LEFT JOIN TRN_POSITION POS ON POS.POSITION_ID = EMP.POSITION_ID
	LEFT JOIN TRN_DEPARTMENT DEP ON DEP.DEPARTMENT_ID = POS.DEPARTMENT_ID
	LEFT JOIN TRN_PREFIX PRE ON EMP.PREFIX_ID = PRE.PREFIX_ID
	WHERE 1=1
	AND EMP.ACTIVE = 'Y'
	AND PRE.PREFIX_ID = &PREFIX_ID
	AND CONCAT(NAME, ' ', SURNAME) LIKE CONCAT(&FULLNAME, '%')
	AND NICK_NAME = &NICK_NAME
	AND EMP.SEX = &SEX
	AND DEP.DEPARTMENT_ID = 1
	AND POS.POSITION_ID = 2
	AND EMP.START_WORK_DATE >= &START_WORK_DATE			          	/* START_DATE '2017-01-20 00:00:00' */
	AND EMP.START_WORK_DATE <= &START_WORK_DATE				        /* END_DATE '2017-02-25 00:00:00' */
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ค้นหาข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
searchEmployee {
	SELECT EMP.EMPLOYEE_ID
	 , PRE.PREFIX_ID
	 , PRE.PREFIX_NAME
	 , CONCAT(NAME, ' ', SURNAME, ' (', NICK_NAME , ')') AS FULLNAME
	 , EMP.SEX
	 , DEP.DEPARTMENT_ID
	 , DEP.DEPARTMENT_NAME
	 , POS.POSITION_ID
	 , POS.POSITION_NAME
	 , DATE_FORMAT(EMP.START_WORK_DATE ,'%d/%m/%y') AS START_WORK_DATE
	 , DATE_FORMAT(EMP.END_WORK_DATE ,'%d/%m/%y') AS END_WORK_DATE
	 , EMP.WORK_STATUS
	 , EMP.CREATE_DATE
	 , EMP.CREATE_USER
	 , EMP.UPDATE_DATE
	 , EMP.UPDATE_USER
	 , EMP.REMARK
	 FROM TRN_EMPLOYEE EMP
	 LEFT JOIN TRN_POSITION POS ON POS.POSITION_ID = EMP.POSITION_ID
	 LEFT JOIN TRN_DEPARTMENT DEP ON DEP.DEPARTMENT_ID = POS.DEPARTMENT_ID
	 LEFT JOIN TRN_PREFIX PRE ON EMP.PREFIX_ID = PRE.PREFIX_ID
	 WHERE 1=1
	 AND EMP.ACTIVE = 'Y'
	 AND PRE.PREFIX_ID = %s
	 AND CONCAT(NAME, ' ', SURNAME) LIKE CONCAT(%s, '%')
	 AND NICK_NAME = %s
	 AND EMP.SEX = %s
	 AND DEP.DEPARTMENT_ID = %s
	 AND POS.POSITION_ID = %s
	 AND EMP.START_WORK_DATE >= %s             /* START_DATE '2017-01-20 00:00:00' */
	 AND EMP.START_WORK_DATE <= %s           /* END_DATE '2017-02-25 00:00:00' */
	 AND EMP.WORK_STATUS = %s
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ตรวจสอบข้อมูลพนักงานซ้ำ_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
SELECT COUNT(1) AS TOT
FROM TRN_EMPLOYEE
WHERE ACTIVE = 'Y'
AND NAME = &NAME
AND SURNAME = &SURNAME
AND NICK_NAME = &NICK_NAME
AND SEX = &SEX
AND POSITION_ID = &POSITION_ID
AND EMPLOYEE_ID <> &EMPLOYEE_ID

/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : เพิ่มข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
addEmployee {
	INSERT INTO TRN_EMPLOYEE(
	  NAME
	  ,SURNAME
	  ,NICK_NAME
	  ,PREFIX_ID
	  ,SEX
	  ,POSITION_ID
	  ,START_WORK_DATE
	  ,WORK_STATUS
	  ,REMARK
	  ,ACTIVE
	  ,CREATE_DATE
	  ,CREATE_USER
	) VALUES (
	  &NAME
	  ,&SURNAME
	  ,&NICK_NAME
	  ,&PREFIX_ID
	  ,&SEX
	  ,&POSITION_ID
	  ,&START_WORK_DATE
	  ,&REMARK
	  ,'Y'
	  ,CURRENT_TIMESTAMP -- CREATE_DATE - IN DATETIME
	  ,&USER_ID
	)

}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : นับจำนวนข้อมูลพนักงานตามรหัสพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
SELECT COUNT(1) AS TOT
FROM TRN_EMPLOYEE EMP
LEFT JOIN TRN_POSITION POS ON POS.POSITION_ID = EMP.POSITION_ID
LEFT JOIN TRN_DEPARTMENT DEP ON DEP.DEPARTMENT_ID = POS.DEPARTMENT_ID
LEFT JOIN TRN_PREFIX PRE ON EMP.PREFIX_ID = PRE.PREFIX_ID
WHERE 1=1
AND EMP.ACTIVE = 'Y'
AND EMP.EMPLOYEE_ID = &EMPLOYEE_ID

/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ค้นหาข้อมูลพนักงานตามรหัสพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
SELECT EMP.EMPLOYEE_ID
, PRE.PREFIX_ID
, PRE.PREFIX_NAME
, EMP.NAME
, EMP.SURNAME
, EMP.NICK_NAME
, EMP.SEX
, DEP.DEPARTMENT_ID
, DEP.DEPARTMENT_NAME
, POS.POSITION_ID
, POS.POSITION_NAME
, EMP.START_WORK_DATE
, EMP.END_WORK_DATE
, EMP.WORK_STATUS
, EMP.REMARK
, EMP.CREATE_DATE
, EMP.CREATE_USER
, EMP.UPDATE_DATE
, EMP.UPDATE_USER
FROM TRN_EMPLOYEE EMP
LEFT JOIN TRN_POSITION POS ON POS.POSITION_ID = EMP.POSITION_ID
LEFT JOIN TRN_DEPARTMENT DEP ON DEP.DEPARTMENT_ID = POS.DEPARTMENT_ID
LEFT JOIN TRN_PREFIX PRE ON EMP.PREFIX_ID = PRE.PREFIX_ID
WHERE 1=1
AND EMP.ACTIVE = 'Y'
AND EMPLOYEE_ID = &EMPLOYEE_ID

/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : แก้ไขข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
UPDATE TRN_EMPLOYEE
SET
  NAME = &NAME
  ,SURNAME = &SURNAME
  ,NICK_NAME = &NICK_NAME
  ,PREFIX_ID = &PREFIX_ID
  ,POSITION_ID = &POSITION_ID
  ,START_WORK_DATE = &START_WORK_DATE
  ,END_WORK_DATE = &END_WORK_DATE
  ,WORK_STATUS = &WORK_STATUS
  ,REMARK = &REMARK
  ,UPDATE_DATE = CURRENT_TIMESTAMP
  ,UPDATE_USER = &UPDATE_USER
WHERE EMPLOYEE_ID = &UPDATE_DATE

/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ลบข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
UPDATE TRN_EMPLOYEE SET
	 ACTIVE = &ACTIVE
	,UPDATE_DATE = CURRENT_TIMESTAMP
	,UPDATE_USER = &USER_ID
WHERE EMPLOYEE_ID IN (&EMPLOYEE_ID)