package com.okan.bankapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.okan.bankapi.dto.LoginRequest;
import com.okan.bankapi.dto.LoginResponse;
import com.okan.bankapi.service.UserService;

@RestController
public class LoginController {
	
	private final UserService userService;

	
	@Autowired
	public LoginController(UserService userService) {
		this.userService = userService;
	}

	

	@PostMapping("/v1/auth")
	public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
		
		LoginResponse response = userService.login(request);
		
		
		if(response == null) {
			return ResponseEntity
					.notFound().build();
		}
		return ResponseEntity
		.ok()
        .body(response);
		
	}

}
