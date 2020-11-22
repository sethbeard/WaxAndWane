package com.seth.blargh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seth.blargh.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

	
}
