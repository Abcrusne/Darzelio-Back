package lt2021.projektas.userRegister;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Transactional(readOnly = true)
	public List<ServiceLayerUser> getUsers() {
		return userDao.findAll().stream()
				.map(userFromService -> new ServiceLayerUser(userFromService.getId(), userFromService.getFirstname(),
						userFromService.getLastname(), userFromService.getEmail(), userFromService.getPassword(), userFromService.getRole()))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ServiceLayerUser getSingleUser(long id) {
		var userFromService = userDao.findById(id).orElse(null);
		return new ServiceLayerUser(userFromService.getId(), userFromService.getFirstname(),
				userFromService.getLastname(), userFromService.getEmail(), userFromService.getPassword(), userFromService.getRole());
	}

	@Transactional
	public void createUser(CreateUserCommand newUser) {
		User userToSave = new User(newUser.getFirstname(), newUser.getLastname(), newUser.getEmail(),
				newUser.getRole());
		@SuppressWarnings("deprecation")
		PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");
		userToSave.setPassword(encoder.encode(newUser.getPassword()));
		userDao.save(userToSave);
	}

	@Transactional
	public void updateUser(ServiceLayerUser user) {
		var updatedUser = userDao.findById(user.getId()).orElse(null);
		updatedUser.setFirstname(user.getFirstname());
		updatedUser.setLastname(user.getLastname());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setRole(user.getRole());
		updatedUser.setPassword(user.getPassword());
	}

	@Transactional
	public void deleteUser(Long id) {
		userDao.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username + " not found.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				AuthorityUtils.createAuthorityList(new String[] { "ROLE_" + user.getRole() }));
	}

	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

}
