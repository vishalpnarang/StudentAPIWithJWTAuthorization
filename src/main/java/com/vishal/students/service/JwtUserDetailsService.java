package com.vishal.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vishal.students.model.User;
import com.vishal.students.repository.UserRepository;
import com.vishal.students.security.JwtUserFactory;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
    private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		
		if(user == null)
			throw new UsernameNotFoundException("User not found with user name : " + username);
		else
			return JwtUserFactory.create(user);
	}

}
