package com.Demo_JWT.validation;
import java.util.regex.*; 

import org.apache.commons.validator.routines.EmailValidator;
public class Validation {
	
	public static void validateEmail(String email) throws Exception {
		boolean valid = false;
		if(email!=null) {
			valid = EmailValidator.getInstance().isValid(email);
		}
		if(!valid) {
			throw new Exception("Invalid Email");
		}
	}
	
	public static void validatePhone(String phone) throws Exception {
		Pattern pattern  = Pattern.compile("(0/91)?[7-9][0-9]{9}"); 
		Matcher matcher = pattern.matcher(phone); 
		 if(!matcher.matches()) {
			throw new Exception("Invalid Phone");
		 }
		 
	}
}
