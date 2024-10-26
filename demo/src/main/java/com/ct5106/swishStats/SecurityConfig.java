package com.ct5106.swishStats;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ct5106.swishStats.service.UserDetailsServiceImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
		private final UserDetailsServiceImpl userDetailsService;
		private final AuthenticationFilter authenticationFilter;
		
		// inject the UserDetailsServiceImpl
		public SecurityConfig(UserDetailsServiceImpl userDetailsService, AuthenticationFilter authenticationFilter)
		{
			this.userDetailsService = userDetailsService;
			this.authenticationFilter = authenticationFilter;
		}

		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
		{
			// set the default UserDetailsService for this AuthenticationManagerBuilder
			auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		}

		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception
		{
			return authConfig.getAuthenticationManager();
		}

		// Add filterChain method
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
		{
			http.csrf((csrf) -> csrf.disable())
					.sessionManagement(
							(sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
							.requestMatchers(HttpMethod.POST, "/login").permitAll().anyRequest().authenticated())
					.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);;
			return http.build();
		}

		/*
		 * @Bean public InMemoryUserDetailsManager userDetailsService() { UserDetails
		 * user =
		 * User.builder().username("user").password(passwordEncoder().encode("password")
		 * ).roles("USER") .build(); return new InMemoryUserDetailsManager(user); }
		 */

		@Bean
		public PasswordEncoder passwordEncoder()
		{
			return new BCryptPasswordEncoder();
		}
	}
