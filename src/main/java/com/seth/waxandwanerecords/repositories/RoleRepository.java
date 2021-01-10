package com.seth.waxandwanerecords.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seth.waxandwanerecords.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
