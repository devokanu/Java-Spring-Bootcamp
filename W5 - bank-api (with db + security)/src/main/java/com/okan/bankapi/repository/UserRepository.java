package com.okan.bankapi.repository;

import org.springframework.stereotype.Repository;

import com.okan.bankapi.model.UserModel;

@Repository
public interface UserRepository {
	
	UserModel loadUserByUsername(String username);

}
