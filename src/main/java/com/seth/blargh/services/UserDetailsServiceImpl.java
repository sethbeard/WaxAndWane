package com.seth.blargh.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	@Autowired
	com.seth.blargh.repositories.UserRepository userRepos;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.warn("inside user details service impl");
		
		com.seth.blargh.entities.User user = userRepos.findByEmail(username);
		LOGGER.warn("user "+ user.getEmail());
		if(user==null)
		{
			throw new UsernameNotFoundException("User not found for email " +username);
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles());
	}

}
