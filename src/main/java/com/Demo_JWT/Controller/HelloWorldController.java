package com.Demo_JWT.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Demo_JWT.model.User;
import com.Demo_JWT.repository.UserDao;

@RestController
public class HelloWorldController {

	@RequestMapping("/hello")
	public String test()
	{
		return "welcome.......";
	}
	
	
	
}
