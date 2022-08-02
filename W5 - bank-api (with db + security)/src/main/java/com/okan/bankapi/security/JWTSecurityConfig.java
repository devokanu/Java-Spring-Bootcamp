package com.okan.bankapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.okan.bankapi.service.UserService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private SecurityFilter securityFilter;
	private UserService userService;

	public JWTSecurityConfig(SecurityFilter securityFilter, UserService userService) {
		this.securityFilter = securityFilter;
		this.userService = userService;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf().disable();
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.authorizeRequests().antMatchers("/v1/auth").permitAll();
		httpSecurity.authorizeRequests().antMatchers("/v1/accounts/").hasAuthority("ADMIN");
		httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/v1/accounts/**")
										.hasAuthority("CREATE_ACCOUNT");
		httpSecurity.authorizeRequests().antMatchers(HttpMethod.DELETE, "/v1/accounts/**")
										.hasAuthority("REMOVE_ACCOUNT");
		httpSecurity.authorizeRequests().anyRequest().authenticated();
		httpSecurity.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.formLogin().disable();
	}
}
