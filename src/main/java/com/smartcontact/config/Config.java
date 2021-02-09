package com.smartcontact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService getuserDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider daoauth=new DaoAuthenticationProvider();
		daoauth.setUserDetailsService(this.getuserDetailsService());
		daoauth.setPasswordEncoder(passwordEncoder());
		return daoauth;
		
	}
 // configure method
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
		         .antMatchers("/user/**").hasRole("USER").antMatchers("/**")
		         .permitAll().and().formLogin().loginPage("/signin")
		         .loginProcessingUrl("/dologin")
		         .defaultSuccessUrl("/user/index")
		         //.failureUrl("/login-fail")
		         .and().csrf().disable();
	}
	
	
	
	
}
