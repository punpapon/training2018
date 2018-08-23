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
	AND PRE.PREFIX_ID = %s
	AND CONCAT(NAME, ' ', SURNAME) LIKE CONCAT(%s, '%')
	AND NICK_NAME = %s
	AND EMP.SEX = %s
	AND DEP.DEPARTMENT_ID = %s
	AND POS.POSITION_ID = %s
	AND EMP.START_WORK_DATE >= STR_TO_DATE(%s, '%d/%m/%Y')			          	/* START_DATE '2017-01-20 00:00:00' */
	AND EMP.START_WORK_DATE <= STR_TO_DATE(%s, '%d/%m/%Y')				        /* END_DATE '2017-02-25 00:00:00' */
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
	 , DATE_FORMAT(EMP.START_WORK_DATE ,'%d/%m/%Y') AS START_WORK_DATE
	 , DATE_FORMAT(EMP.END_WORK_DATE ,'%d/%m/%Y') AS END_WORK_DATE
	 , EMP.WORK_STATUS
	 , DATE_FORMAT(EMP.CREATE_DATE , '%d/%m/%Y') AS CREATE_DATE 
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
	 AND EMP.START_WORK_DATE >= STR_TO_DATE(%s, '%d/%m/%Y')             /* START_DATE '2017-01-20 00:00:00' */
	 AND EMP.START_WORK_DATE <= STR_TO_DATE(%s, '%d/%m/%Y')           /* END_DATE '2017-02-25 00:00:00' */
	 AND EMP.WORK_STATUS = %s
	 LIMIT %s
		, %s
	 
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ค้นหาข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
searchEmployeeX {
	SELECT EMP.EMPLOYEE_ID
	 , PRE.PREFIX_ID
	 , PRE.PREFIX_NAME
	 , CONCAT(NAME, ' ', SURNAME, ' (', NICK_NAME , ')') AS FULLNAME
	 , EMP.SEX
	 , DEP.DEPARTMENT_ID
	 , DEP.DEPARTMENT_NAME
	 , POS.POSITION_ID
	 , POS.POSITION_NAME
	 , DATE_FORMAT(EMP.START_WORK_DATE ,'%d/%m/%Y') AS START_WORK_DATE
	 , DATE_FORMAT(EMP.END_WORK_DATE ,'%d/%m/%Y') AS END_WORK_DATE
	 , EMP.WORK_STATUS
	 , DATE_FORMAT(EMP.CREATE_DATE , '%d/%m/%Y') AS CREATE_DATE 
	 , EMP.CREATE_USER
	 , EMP.UPDATE_DATE
	 , EMP.UPDATE_USER
	 , EMP.REMARK
	 FROM TRN_EMPLOYEE EMP
	 LEFT JOIN TRN_POSITION POS ON POS.POSITION_ID = EMP.POSITION_ID
	 LEFT JOIN TRN_DEPARTMENT DEP ON DEP.DEPARTMENT_ID = POS.DEPARTMENT_ID
	 LEFT JOIN TRN_PREFIX PRE ON EMP.PREFIX_ID = PRE.PREFIX_ID
	 WHERE 1=1 
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ค้นหาข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
searchEmployee2 {
	SELECT EMP.EMPLOYEE_ID
	 , PRE.PREFIX_ID
	 , PRE.PREFIX_NAME
	 , EMP.NAME
	 , EMP.SURNAME
	 , EMP.SEX
	 , DEP.DEPARTMENT_ID
	 , DEP.DEPARTMENT_NAME
	 , POS.POSITION_ID
	 , POS.POSITION_NAME
	 , DATE_FORMAT(EMP.START_WORK_DATE ,'%d/%m/%Y') AS START_WORK_DATE
	 , DATE_FORMAT(EMP.END_WORK_DATE ,'%d/%m/%Y') AS END_WORK_DATE
	 , EMP.WORK_STATUS
	 , DATE_FORMAT(EMP.CREATE_DATE , '%d/%m/%Y') AS CREATE_DATE 
	 , EMP.CREATE_USER
	 , EMP.UPDATE_DATE
	 , EMP.UPDATE_USER
	 , EMP.REMARK
	 FROM TRN_EMPLOYEE EMP
	 LEFT JOIN TRN_POSITION POS ON POS.POSITION_ID = EMP.POSITION_ID
	 LEFT JOIN TRN_DEPARTMENT DEP ON DEP.DEPARTMENT_ID = POS.DEPARTMENT_ID
	 LEFT JOIN TRN_PREFIX PRE ON EMP.PREFIX_ID = PRE.PREFIX_ID
	 WHERE 1=1 
	 AND EMP.NAME = %s
	 AND EMP.SURNAME = %s
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ค้นหาข้อมูล Export Excel พนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
searchExportEmployee {
	SELECT EMP.EMPLOYEE_ID
	 , PRE.PREFIX_ID
	 , PRE.PREFIX_NAME
	 , CONCAT(NAME, ' ', SURNAME, ' (', NICK_NAME , ')') AS FULLNAME
	 , EMP.SEX
	 , DEP.DEPARTMENT_ID
	 , DEP.DEPARTMENT_NAME
	 , POS.POSITION_ID
	 , POS.POSITION_NAME
	 , DATE_FORMAT(EMP.START_WORK_DATE ,'%d/%m/%Y') AS START_WORK_DATE
	 , DATE_FORMAT(EMP.END_WORK_DATE ,'%d/%m/%Y') AS END_WORK_DATE
	 , EMP.WORK_STATUS
	 , DATE_FORMAT(EMP.CREATE_DATE , '%d/%m/%Y') AS CREATE_DATE 
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
	 AND EMP.START_WORK_DATE >= STR_TO_DATE(%s, '%d/%m/%Y')            /* START_DATE '2017-01-20 00:00:00' */
	 AND EMP.START_WORK_DATE <= STR_TO_DATE(%s, '%d/%m/%Y')           /* END_DATE '2017-02-25 00:00:00' */
	 AND EMP.WORK_STATUS = %s
	 ORDER BY DEP.DEPARTMENT_ID ASC, POS.POSITION_ID DESC
	 
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ตรวจสอบข้อมูลพนักงานซ้ำ_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
searchCountDup {
	SELECT COUNT(1) AS TOT
	FROM TRN_EMPLOYEE
	WHERE ACTIVE = 'Y'
	AND NAME = %s
	AND SURNAME = %s
	AND NICK_NAME = %s
	AND SEX = %s
	AND POSITION_ID = %s
	AND EMPLOYEE_ID <> %s
	AND WORK_STATUS = %s
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : เพิ่มข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
insertEmployee {
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
	  %s
	  ,%s
	  ,%s
	  ,%s
	  ,%s
	  ,%s
	  ,STR_TO_DATE(%s, '%d/%m/%Y')
	  ,%s
	  ,%s
	  ,'Y'
	  ,CURRENT_TIMESTAMP -- CREATE_DATE - IN DATETIME
	  ,%s
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
searchById {
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
	, DATE_FORMAT(EMP.START_WORK_DATE ,'%d/%m/%Y') AS START_WORK_DATE
	, DATE_FORMAT(EMP.END_WORK_DATE ,'%d/%m/%Y') AS END_WORK_DATE
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
	AND EMPLOYEE_ID = %s
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : แก้ไขข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
updateEmployee {
	UPDATE TRN_EMPLOYEE
	SET
	  NAME = %s
	  ,SURNAME = %s
	  ,NICK_NAME = %s
	  ,PREFIX_ID = %s
	  ,POSITION_ID = %s
	  	
	  ,END_WORK_DATE = STR_TO_DATE(%s, '%d/%m/%Y')
	  ,WORK_STATUS = %s
	  ,REMARK = %s
	  ,UPDATE_DATE = CURRENT_TIMESTAMP
	  ,UPDATE_USER = %s
	WHERE EMPLOYEE_ID = %s
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ลบข้อมูลพนักงาน_SQL
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
deleteEmployee {
	UPDATE TRN_EMPLOYEE SET
   ACTIVE = 'N'
  ,UPDATE_DATE = CURRENT_TIMESTAMP
  ,UPDATE_USER = %s
 WHERE EMPLOYEE_ID IN (%s)
}
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------
SQL : ค้นหาข้อมูลไปออก  iReport
Description : 
------------------------------------------------------------------------------------------------------------------------------------------------------------*/
ireportEmployee {
	SELECT PERSON_ID
	 , NAME
	 , LAST_NAME
	 , AGE	
	 , DEPARTMENT_ID
	 FROM PERSON
	 WHERE 1=1	 
}