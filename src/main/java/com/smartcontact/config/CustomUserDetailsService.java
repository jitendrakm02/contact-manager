package com.smartcontact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontact.model.User;
import com.smartcontact.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {
     @Autowired
	 private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//fetching user details
		User user=userRepository.getUserByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("could not found email ");
		}
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		
		return customUserDetails;
	}

}
