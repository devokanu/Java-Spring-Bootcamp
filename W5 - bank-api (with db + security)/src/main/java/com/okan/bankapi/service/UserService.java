package com.okan.bankapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.okan.bankapi.dto.LoginRequest;
import com.okan.bankapi.dto.LoginResponse;
import com.okan.bankapi.model.UserModel;
import com.okan.bankapi.repository.UserRepository;
import com.okan.bankapi.security.JWTTokenUtil;

@Service
public class UserService implements UserDetailsService {
	
	private final UserRepository repo;
	private final AuthenticationManager authenticationManager;
	private final JWTTokenUtil jwtTokenUtil;
	private final AccountService accountService;
	
	
	@Autowired
	public UserService(UserRepository repository, AuthenticationManager authenticationManager, UserService userService,
			JWTTokenUtil jwtTokenUtil, AccountService accountService) {
		this.accountService = accountService;
		this.repo = repository;
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		
	}
	
	public UserDetails loadUserByUsername(String username) {
		
		UserModel user = repo.loadUserByUsername(username);
		if(user != null) {
			
			String[] auths = user.getAuthorities().split(",");
			List<GrantedAuthority> grantedAuhorities = new ArrayList<GrantedAuthority>();
			for(String authority : auths) {
				grantedAuhorities.add(new SimpleGrantedAuthority(authority));
			}
			
			return User
					.builder()
					.username(user.getUsername())
					.password(user.getPassword())
					.disabled(true)
					.accountExpired(true)
					.accountLocked(true)
					.credentialsExpired(true)
					.authorities(grantedAuhorities)
					.build();
		}else {
				throw new UsernameNotFoundException(username + " Not Found");
		}
		
	}
	
	public LoginResponse login(LoginRequest request) {
		LoginResponse resp = new LoginResponse();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			final UserDetails userDetails = loadUserByUsername(request.getUsername());
			if(userDetails != null) {
				final String token = jwtTokenUtil.generateToken(userDetails);
				resp.setStatus("success");
				resp.setToken(token);
			}else {
				resp = null;
			}
		} catch (Exception e) {
			resp.setStatus("false");
		}
		return resp;
	}
	
	public boolean isAuth(long accountNumber) {

		int accountId = accountService
				.getDetail(accountNumber)
				.getId();

		UserModel authUser = (UserModel)SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();
		

		if (authUser.getId() == accountId) {
			return true;
		}else {
			return false;
		}
		
	}
	

}
