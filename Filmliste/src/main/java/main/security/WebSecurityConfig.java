package main.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
//annotation nur fuer userdetailservice noetig
@ComponentScan(basePackageClasses=UserRepositoryUserDetailsService.class)
// Methoden mit @PostAuthorize (am sinnvollsten in repos) werden vor ausfuehren geprueft. sieh bsp in userrepo
//@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	public void configure(HttpSecurity http) throws Exception{
		http.csrf().disable()
		.authorizeRequests().anyRequest().authenticated()
		.and().httpBasic().realmName("MAIN").authenticationEntryPoint(getBasicEntryPoint())
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicEntryPoint(){
		return new CustomBasicAuthenticationEntryPoint();
	}
	
	@Autowired
	public void configureGlobal(UserDetailsService userDetailService , AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailService);
	}
}
