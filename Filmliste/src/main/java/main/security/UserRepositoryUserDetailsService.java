package main.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import main.jpa.UserRepository;
import main.model.User;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Autowired
	public UserRepositoryUserDetailsService(UserRepository userRepository){
		this.userRepository = userRepository;
		User user = new User("ine", "pw");
		userRepository.save(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByName(username);
		if(user == null){
			throw new UsernameNotFoundException("Could not find user "+username);
		}
		return new UserRepositoryUserDetails(user);
	}
	
	private final static class UserRepositoryUserDetails extends User implements UserDetails{
		
		/**
		 * generated
		 */
		private static final long serialVersionUID = -2966164869659321111L;

		private UserRepositoryUserDetails(User user){
			super(user.getName(), user.getPassword());
		}
		
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities(){
			return AuthorityUtils.createAuthorityList("ROLE_USER");
		}
		
		@Override
		public String getUsername(){
			return getName();
		}
		
		@Override
		public boolean isAccountNonExpired(){
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}

}
