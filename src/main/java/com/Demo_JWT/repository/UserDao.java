package com.Demo_JWT.repository;

import org.springframework.stereotype.Repository;

import com.Demo_JWT.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserDao extends CrudRepository<User,Long>{

	User findByUsername(String name);

	
}
