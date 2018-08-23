package util.report;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ManageDataPOI {

	
	public static int checknumber(int num1 , int num2 , int num3){
		if(num1==0) {
	 		num1=1;
	 		return num1;
	 	}
	 	else if(num2==0){
	 		num2=1;
	 		return num2;
	 	}
	 	else if(num3==0){
	 		num3=1;
	 		return num3;
	 	}
		return 0;
	}
	
	public static int checknumber1(int num1){
		if(num1==0) {
	 		num1=1;
		}	
		return num1;
	}
	public static int checknumber2(int num2){
		if(num2==0) {
			num2=1;
		}	
		return num2;
	}
	public static int checknumber3(int num3){
		if(num3==0) {
			num3=1;
		}	
		return num3;
	}
	
	public static String checkNullCriteria(String cri) {
		String valueCri = cri;
		if(cri.equals("")){
			valueCri = "-";
		}
		return valueCri;
	}
	
	public static String convertDate(String dateTH) {
		String dateTh2 = dateTH;
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
	    // แปลงวันที่ ที่รับเข้ามา
	    
	     DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	     try {
	      Date parseDate = df.parse(dateTh2);
	      dateTh2 = formatter.format(parseDate);
	     } catch (ParseException p) {
	      p.printStackTrace();
	     }
	     return dateTh2;
	}
}

	