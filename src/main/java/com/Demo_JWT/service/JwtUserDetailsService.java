package com.Demo_JWT.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Demo_JWT.model.User;
import com.Demo_JWT.model.UserDTO;
import com.Demo_JWT.repository.UserDao;
import com.Demo_JWT.validation.Validation;

@Service
public class JwtUserDetailsService implements UserDetailsService{
	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		User user = (User) userDao.findByUsername(name);
		System.out.println("inside loadbyuser");
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + name);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	public User save(UserDTO user) throws Exception {
		User newUser = new User();
		String email = user.getEmail();
		String phone = user.getPhone();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setCompany(user.getCompany());
		newUser.setCountry(user.getCountry());
		Validation.validateEmail(email);
		Validation.validatePhone(phone);
		newUser.setEmail(user.getEmail());
		newUser.setPhone(user.getPhone());
		return userDao.save(newUser);
	}

}
