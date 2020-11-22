package com.seth.blargh.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.seth.blargh.controllers.UserController;

@Service
public class SecurityServiceImpl implements SecurityService {
@Autowired
private UserDetailsService userDetailsService;

private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

@Autowired
private AuthenticationManager authManager;
	
@Override
	public boolean login(String username, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password,userDetails.getAuthorities());
		authManager.authenticate(token);
		boolean result = token.isAuthenticated();
		
		if (result) {
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		return result;
	}

}
