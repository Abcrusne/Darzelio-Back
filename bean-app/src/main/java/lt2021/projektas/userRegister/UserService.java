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
	public List<ServiceLayerUser> getUsers() {
		return userDao.findAll().stream()
				.map(userFromService -> new ServiceLayerUser(userFromService.getId(), userFromService.getFirstname(),
						userFromService.getLastname(), userFromService.getEmail(), userFromService.getRole(),
						userFromService.getPassword()))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ServiceLayerUser getSingleUser(long id) {
		var userFromService = userDao.findById(id).orElse(null);
		return new ServiceLayerUser(userFromService.getId(), userFromService.getFirstname(),
				userFromService.getLastname(), userFromService.getEmail(), userFromService.getRole(),
				userFromService.getPassword());
	}

	@Transactional
	public void createUser(ServiceLayerUser newUser) {
		userDao.save(new User(newUser.getFirstname(), newUser.getLastname(), newUser.getEmail(), newUser.getRole(),
				newUser.getPassword()));

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

}
