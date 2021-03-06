searchUser{
	select FIRSTNAME, LASTNAME, EMAIL, LOGIN
	from [SCHEMAS].sec_user
	where USER_ID = %s
}

countCustomer{
	SELECT COUNT(*) AS CNT
	FROM [SCHEMAS].cus_customer
	WHERE 1=1
		AND CUS_FIRSTNAME LIKE CONCAT('%',%s,'%')
		AND CUS_LASTNAME LIKE CONCAT('%',%s,'%')
		AND PROVINCE_ID = %s
		AND AMPHUR_ID = %s
		AND ACTIVE = %s
		AND CUS_ID NOT IN (%s)
}

searchListCustomer{
	SELECT CUS_ID, CUS_CODE, CUS_FIRSTNAME, CUS_LASTNAME, CUS_EMAIL, CUS_ADDRESS, CUS_MOBILE, LOGIN
	FROM [SCHEMAS].cus_customer
	WHERE 1=1
		AND CUS_FIRSTNAME LIKE CONCAT('%',%s,'%')
		AND CUS_LASTNAME LIKE CONCAT('%',%s,'%')
		AND PROVINCE_ID = %s
		AND AMPHUR_ID = %s
		AND ACTIVE = %s
		AND CUS_ID NOT IN (%s)
	ORDER BY 
		%s
	LIMIT %s
    	,%s
}

searchListCustomerById{
	SELECT CUS_ID, CUS_CODE, CUS_FIRSTNAME, CUS_LASTNAME, CUS_EMAIL, CUS_ADDRESS, CUS_MOBILE, LOGIN
	FROM [SCHEMAS].cus_customer
	WHERE CUS_ID IN (%s)
}

searchTree{
	SELECT OPERATOR_ID
	  , PARENT_ID
	  , LABEL_TH
	  , LABEL_EN
	  , URL
	  , LEVEL
	  , SEQUENCE_NO
	  , SYSTEM_CODE
	  , TYPE
	  , ACTIVE
	FROM [SCHEMAS].sec_operator;

}

XsearchTreeX{
	SELECT O.GROUP_SYSTEM, O.OPERATOR_TYPE, O.PARENT_ID, O.OPERATOR_ID, O.OPERATOR_LEVEL
		, O.LIST_NO, O.PATH, O.URL, O.DETAIL,S.LABEL_TH AS SYSTEM_NAME, O.LABEL_TH
		, CASE  WHEN O.OPERATOR_TYPE= 'M' AND O.OPERATOR_LEVEL <> 1 THEN O.LABEL_TH  END AS MENU_NAME
		, CASE  O.OPERATOR_TYPE  WHEN 'F' THEN O.LABEL_TH  END AS FUNCTION_NAME, O.PATH_LABEL, O.DETAIL
	FROM [SCHEMAS].TUT_TREE O , [SCHEMAS].TUT_TREE S
	WHERE O.GROUP_SYSTEM = S.OPERATOR_ID
		AND O.ACTIVE = 'Y' AND S.ACTIVE = 'Y'
		AND (O.USER_TYPE = 'O' OR O.USER_TYPE = 'A' )
		AND (S.USER_TYPE = 'O' OR S.USER_TYPE = 'A' )
		AND O.OPERATOR_ID IN (
			SELECT A.OPERATOR_ID
			FROM [SCHEMAS].TUT_TREE A
			WHERE A.OPERATOR_TYPE = 'F'
				AND A.REPORT_TYPE <> 'O'
			UNION ALL
			SELECT V_A.OPERATOR_ID
			FROM [SCHEMAS].TUT_TREE V_A
				INNER JOIN (
					SELECT A.PARENT_ID, SUM(CASE WHEN A.OPERATOR_TYPE = 'F' THEN 1 ELSE 0 END) AS SUM_REF
					FROM [SCHEMAS].TUT_TREE A
					WHERE A.OPERATOR_TYPE = 'F'
						AND  A.REPORT_TYPE <> 'O'
					GROUP BY A.PARENT_ID
					HAVING SUM(CASE WHEN A.OPERATOR_TYPE = 'F' THEN 1 ELSE 0 END) > 0
				)V_B ON V_A.OPERATOR_ID = V_B.PARENT_ID
			UNION ALL
			SELECT OPERATOR_ID
			FROM [SCHEMAS].TUT_TREE
			WHERE OPERATOR_ID IN (
				SELECT V_A.PARENT_ID
				FROM [SCHEMAS].TUT_TREE V_A
					INNER JOIN (
						SELECT A.PARENT_ID, SUM(CASE WHEN A.OPERATOR_TYPE = 'F' THEN 1 ELSE 0 END) AS SUM_REF
						FROM [SCHEMAS].TUT_TREE A
						WHERE A.OPERATOR_TYPE = 'F'
							AND  A.REPORT_TYPE <> 'O'
						GROUP BY A.PARENT_ID
						HAVING SUM(CASE WHEN A.OPERATOR_TYPE = 'F' THEN 1 ELSE 0 END) > 0
					)V_B ON V_A.OPERATOR_ID = V_B.PARENT_ID
				)
			UNION ALL
			SELECT OPERATOR_ID
			FROM [SCHEMAS].TUT_TREE
			WHERE OPERATOR_ID IN (
				SELECT PARENT_ID
				FROM [SCHEMAS].TUT_TREE
				WHERE OPERATOR_ID IN (
					SELECT V_A.PARENT_ID
					FROM [SCHEMAS].TUT_TREE V_A
						INNER JOIN (
							SELECT A.PARENT_ID, SUM(CASE WHEN A.OPERATOR_TYPE = 'F' THEN 1 ELSE 0 END) AS SUM_REF
							FROM [SCHEMAS].TUT_TREE A
							WHERE A.OPERATOR_TYPE = 'F'
								AND  A.REPORT_TYPE <> 'O'
							GROUP BY A.PARENT_ID
							HAVING SUM(CASE WHEN A.OPERATOR_TYPE = 'F' THEN 1 ELSE 0 END) > 0
						)V_B ON V_A.OPERATOR_ID = V_B.PARENT_ID
					)
				)
			)
		ORDER BY O.ROW_NUM
}

searchListDetailById {
	SELECT O.OPERATOR_ID, O.LABEL, O.ACTIVE, G.VALUE AS ACTIVE_DESC
	FROM [SCHEMAS].SEC_USER U
	  LEFT OUTER JOIN [SCHEMAS].SEC_MAP_USER_OPERATOR M ON U.USER_ID = M.USER_ID
	  LEFT OUTER JOIN [SCHEMAS].SEC_OPERATOR O ON M.OPERATOR_ID = O.OPERATOR_ID
	  LEFT OUTER JOIN [SCHEMAS].CON_GLOBAL_DATA G ON (G.GLOBAL_TYPE_ID = 1 AND G.CODE = O.ACTIVE AND G.LANGUAGE_ID = 1)
	WHERE O.LANGUAGE_ID = 1
	  AND O.LABEL <> ''
	  AND U.USER_ID = %s
ORDER BY 
		%s
}

countUser{
	SELECT COUNT(*) AS CNT
	FROM [SCHEMAS].sec_user u
		LEFT OUTER JOIN [SCHEMAS].con_global_data cgdlc ON (cgdlc.GLOBAL_TYPE_ID = 8 AND cgdlc.CODE = u.LOCKED AND cgdlc.LANGUAGE_ID = 1)
	WHERE u.ACTIVE = 'Y'
		AND u.LANGUAGE_CODE = 'th'
		AND u.FIRSTNAME LIKE CONCAT('%',%s,'%')
		AND u.LASTNAME LIKE CONCAT('%',%s,'%')
		AND u.LOCKED = %s
		AND u.USER_ID NOT IN (%s)
}

searchListUser{
	SELECT u.USER_ID, u.FIRSTNAME, u.LASTNAME, u.EMAIL, u.LOGIN, u.LOCKED, cgdlc.VALUE AS LOCKED_DESC
	FROM [SCHEMAS].sec_user u
		LEFT OUTER JOIN [SCHEMAS].con_global_data cgdlc ON (cgdlc.GLOBAL_TYPE_ID = 8 AND cgdlc.CODE = u.LOCKED AND cgdlc.LANGUAGE_ID = 1)
	WHERE u.ACTIVE = 'Y'
		AND u.LANGUAGE_CODE = 'th'
		AND u.FIRSTNAME LIKE CONCAT('%',%s,'%')
		AND u.LASTNAME LIKE CONCAT('%',%s,'%')
		AND u.LOCKED = %s
		AND u.USER_ID NOT IN (%s)
	ORDER BY 
		%s
	LIMIT %s
    	,%s
}

searchListUserById{
	SELECT u.USER_ID, u.FIRSTNAME, u.LASTNAME, u.EMAIL, u.LOGIN, u.LOCKED, cgdlc.VALUE AS LOCKED_DESC
	FROM [SCHEMAS].sec_user u
		LEFT OUTER JOIN [SCHEMAS].con_global_data cgdlc ON (cgdlc.GLOBAL_TYPE_ID = 8 AND cgdlc.CODE = u.LOCKED AND cgdlc.LANGUAGE_ID = 1)
	WHERE u.LANGUAGE_CODE = 'th'
		AND u.USER_ID IN (%s)
}