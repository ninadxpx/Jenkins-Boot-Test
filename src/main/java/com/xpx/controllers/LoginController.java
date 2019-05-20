package com.xpx.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.xpx.dao.EmployeeRepository;
import com.xpx.entity.ApiToken;
import com.xpx.entity.Employee;

@CrossOrigin()
@RestController
@RequestMapping("/login")
@SessionAttributes("employee")
public class LoginController {
	@Autowired
	EmployeeRepository empRepo;	
	
	// Returns an Employee object if authenticated Or returns with response code not found(404) .	 
	@PostMapping
	public ResponseEntity<Employee> authenticate(HttpServletRequest request, @RequestBody Employee employee) {

		Employee emp = empRepo.findByEmail(employee.getEmail());

		if (null == emp) {
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		} else if (!emp.getPassword().equals(employee.getPassword())) {
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		} else {
			emp.setApiToekn(new ApiToken("access", "refresh", "1"));
			request.getSession().setAttribute("employee", emp);
			return new ResponseEntity<Employee>(emp, HttpStatus.OK);
		}
	}
	
	
	@PostMapping("/logout")
	public ResponseEntity<Employee> logout(HttpServletRequest request) {
		request.getSession().setAttribute("employee", null);
		System.out.println("Request for Logout");
	    return new ResponseEntity<Employee>(HttpStatus.OK);
	}
	

}
