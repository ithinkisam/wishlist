package com.ithinkisam.wishlist.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/home**", "/wishlist**", "/events**", "/secret-santa**").authenticated()
				.antMatchers("/**").permitAll()
				.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/home")
				.usernameParameter("login-email")
				.passwordParameter("login-password")
				.failureUrl("/login?m=login.failure&mtype=danger")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login?logout")
				.permitAll()
				.and()
			.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.usersByUsernameQuery(usersQuery)
			.authoritiesByUsernameQuery(rolesQuery)
			.dataSource(dataSource)
			.passwordEncoder(passwordEncoder);
	}
	
}
