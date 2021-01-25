package lt2021.projektas.userRegister;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public List<UserServiceLayer> getUsers() {
		return userService.getUsers();
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.GET)
	public UserServiceLayer getSingleUser(@PathVariable final Long userId) {
		return userService.getSingleUser(userId);
	}
}
