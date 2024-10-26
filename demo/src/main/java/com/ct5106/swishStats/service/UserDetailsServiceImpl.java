package com.ct5106.swishStats.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ct5106.swishStats.domain.AppUser;
import com.ct5106.swishStats.repository.AppUserRepository;

@Service   // used to mark the class as a (business) service provider
public class UserDetailsServiceImpl implements UserDetailsService
{
	// we need this to fetch the user from the database
	private final AppUserRepository repository;

	public UserDetailsServiceImpl(AppUserRepository repository)
	{
		this.repository = repository;
	}

	// this is the key method to override which retrieves the user based on the username
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		Optional<AppUser> user = repository.findByUsername(username);
		UserBuilder builder = null;
		
		// this is just to make sure we don't crash if no user is returned
		if (user.isPresent())
		{
			AppUser currentUser = user.get();
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(currentUser.getPassword());
			builder.roles(currentUser.getRole());
		} else
		{
			throw new UsernameNotFoundException("User not found.");
		}
		return builder.build();
	}
}
