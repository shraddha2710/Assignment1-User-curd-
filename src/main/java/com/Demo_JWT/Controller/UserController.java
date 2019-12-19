package com.Demo_JWT.Controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Demo_JWT.exception.UserNotFoundException;
import com.Demo_JWT.model.JwtRequest;
import com.Demo_JWT.model.JwtResponse;
import com.Demo_JWT.model.User;
import com.Demo_JWT.model.UserDTO;
import com.Demo_JWT.repository.UserDao;
import com.Demo_JWT.security.JwtTokenUtil;
import com.Demo_JWT.service.JwtUserDetailsService;
import com.Demo_JWT.validation.Validation;


@RestController
public class UserController {
	
	private static Logger logger = Logger.getLogger(UserController.class);
	 
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDao userdao;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		System.out.println("inside ");
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	
	/* Api for register user and save the details in db
	 *  @Rquest UserDTO user
	 *  check for duplicate email
	 *  email and phone validation
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		logger.info(" Register User details ");
		List<User> userData = (List<User>) userdao.findAll();
		// Check if user email is already exists in database 
		for(User u : userData) {
			if(user.getEmail().equals(u.getEmail())) {
				throw new Exception("Duplicate Email");
			}
		}
		logger.info("Register User details "+ "User Name : " + user.getUsername() +" " + "User Country :" + user.getCountry()
		+" "+ "User Company : " + user.getCompany() +" "+ "User Email :" + user.getEmail());
		return ResponseEntity.ok(userDetailsService.save(user));
	}
	
	/* Api for get all user details
	 * return all user details
	 */
	@GetMapping("/get_user")
	public List <User>getAlluser()
	{	
		logger.info("User Data");
		List<User> userdata = (List<User>) userdao.findAll();
		logger.info("User data" + userdata);
		return userdata;
	}
	
	/* Api for update user
	 * @Pathvariale userid
	 * email and phone validation
	 * return updated user data
	 */
	@PutMapping("/update_user/{id}")
	public User update (@PathVariable (value = "id") Long userid ,@RequestBody User userdetails) throws Exception
	{	
		logger.info(" Updated User data");
		User user = userdao.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
		user.setUsername(userdetails.getUsername());
		user.setPassword(bcryptEncoder.encode(userdetails.getPassword()));
		user.setCompany(userdetails.getCompany());
		user.setCountry(userdetails.getCountry());
		String email = userdetails.getEmail();
		String phone = userdetails.getPhone();
		Validation.validateEmail(email);
		Validation.validatePhone(phone);
		user.setEmail(userdetails.getEmail());
		user.setPhone(userdetails.getPhone());
		
		User updateduser = userdao.save(user);
		logger.info("Updated user data:"+"User Id : " +updateduser.getId()+ " User Name : " +" " + updateduser.getUsername());
		return updateduser;
	}
	
	
	/*Api for delete particular user
	 * @Pathvariable userid
	 * @Throws UserNotFoundException
	 */
	@DeleteMapping("/user_delete/{id}")
	public ResponseEntity<?> deleteuser(@PathVariable(value = "id") Long userid) throws UserNotFoundException
	{	
		logger.info(" Delete User data");
		User user = userdao.findById(userid).orElseThrow(()->new UserNotFoundException(userid));
		userdao.delete(user);
		logger.info("Delete user data:"+"User Id : " +user.getId());
		return ResponseEntity.ok().build();
		
	}
	
	/*
	 * Api for get user data by particular id
	 * @Pathvariable userid
	 * @throws UserNotFoundException
	 */
	@GetMapping("/getById/{id}")
	public User getUserById(@PathVariable (value = "id") Long userid) throws UserNotFoundException
	{
		return userdao.findById(userid).orElseThrow(()-> new UserNotFoundException(userid));
	}
	

	private void authenticate(String username, String password) throws Exception {
		System.out.println("in authenticate method");
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
