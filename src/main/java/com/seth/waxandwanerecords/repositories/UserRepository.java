package com.seth.waxandwanerecords.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seth.waxandwanerecords.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

	
}
