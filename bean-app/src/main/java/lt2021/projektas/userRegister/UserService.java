package lt2021.projektas.userRegister;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
						userFromService.getLastname(), userFromService.getEmail(), userFromService.getPassword(),
						userFromService.getRole(), userFromService.isMarkedForDeletion()))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ServiceLayerUser getSingleUser(long id) {
		var userFromService = userDao.findById(id).orElse(null);
		return new ServiceLayerUser(userFromService.getId(), userFromService.getFirstname(),
				userFromService.getLastname(), userFromService.getEmail(), userFromService.getPassword(),
				userFromService.getRole(), userFromService.isMarkedForDeletion());
	}

	@Transactional
	public void createUser(CreateUserCommand newUser) {
		User userToSave = new User(newUser.getFirstname(), newUser.getLastname(), newUser.getEmail().toLowerCase(),
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
		updatedUser.setEmail(user.getEmail().toLowerCase());
		updatedUser.setRole(user.getRole());
		@SuppressWarnings("deprecation")
		PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");
		if (!(user.getPassword().equals(updatedUser.getPassword()))) {
			updatedUser.setPassword(encoder.encode(user.getPassword()));
		}
		userDao.save(updatedUser);
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
				AuthorityUtils.createAuthorityList(new String[] { user.getRole().toString() }));
	}

	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
	@Transactional
	public ResponseEntity<String> changePassword(User user, String oldPassword, String newPassword) {
		PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");
		if (oldPassword.equals(newPassword)) {
			return new ResponseEntity<String>("Senas ir naujas slaptažodis negali sutapti", HttpStatus.BAD_REQUEST);
		}
		if (encoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(encoder.encode(newPassword));
			userDao.save(user);
			return new ResponseEntity<String>("Slaptažodis pakeistas", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Neteisingai įvestas senas slaptažodis", HttpStatus.BAD_REQUEST);
	}

}
