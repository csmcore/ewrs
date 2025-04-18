package app.ewarehouse.serviceImpl;

import app.ewarehouse.repository.UsersRepository;
import app.ewarehouse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
			}
		};
		
	}
	
}
