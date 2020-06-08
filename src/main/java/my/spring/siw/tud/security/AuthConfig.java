package my.spring.siw.tud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import my.spring.siw.tud.model.Credentials;

import javax.sql.*;

@Configuration
@EnableWebSecurity
public class AuthConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Bean
	BCryptPasswordEncoder getPasswordEncoder() {return new BCryptPasswordEncoder();} 
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers(HttpMethod.GET,"/","/index","/login","/signUp").permitAll()
		.antMatchers(HttpMethod.POST,"/login","/signUp","/save").permitAll() //why is save needed?
		.antMatchers(HttpMethod.GET, "/resources/**","/css/**").permitAll()
		.antMatchers(HttpMethod.GET, "/admin").hasAnyAuthority(Credentials.ADMIN_ROLE)
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.usernameParameter("usernameParameter")
		.passwordParameter("passwordParameter")
		.loginProcessingUrl("/login")
		.defaultSuccessUrl("/profile")
		.and()
		.logout()
		.logoutUrl("/logout")
		.clearAuthentication(true)
		.logoutSuccessUrl("/index");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
		.dataSource(this.dataSource)
		.authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username = ?")
		.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username = ?");
	}

}
