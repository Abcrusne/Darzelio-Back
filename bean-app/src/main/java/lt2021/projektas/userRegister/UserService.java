package lt2021.projektas.userRegister;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Transactional(readOnly = true)
	public List<UserServiceLayer> getUsers() {
		return userDao.findAll().stream()
				.map(userFromService -> new UserServiceLayer(userFromService.getId(), userFromService.getFirstname(),
						userFromService.getLastname(), userFromService.getEmail(), userFromService.getRole()))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public UserServiceLayer getSingleUser(Long id) {
		var userFromService = userDao.findById(id).orElse(null);
		return new UserServiceLayer(userFromService.getId(), userFromService.getFirstname(),
				userFromService.getLastname(), userFromService.getEmail(), userFromService.getRole());
	}

//nepadarytas password creation
	@Transactional
	public void createUser(UserServiceLayer newUser) {
		userDao.save(new User(newUser.getFirstname(), newUser.getLastname(), newUser.getEmail(), newUser.getRole()));

	}

	// nepadarytas password update
	@Transactional
	public void updateUser(UserServiceLayer user) {
		var updatedUser = userDao.findById(user.getId()).orElse(null);
		updatedUser.setFirstname(user.getFirstname());
		updatedUser.setLastname(user.getLastname());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setRole(user.getRole());
	}

	@Transactional
	public void deleteUser(Long id) {
		userDao.deleteById(id);
	}

}
