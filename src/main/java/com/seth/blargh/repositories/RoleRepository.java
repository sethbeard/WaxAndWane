package com.seth.blargh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seth.blargh.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
