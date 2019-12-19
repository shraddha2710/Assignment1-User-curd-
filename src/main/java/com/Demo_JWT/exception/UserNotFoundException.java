package com.Demo_JWT.exception;

public class UserNotFoundException extends Exception{

	 public UserNotFoundException(long userid)
	 {
		 super(String.format("User is not found with id : '%s'", userid));
     }
}
