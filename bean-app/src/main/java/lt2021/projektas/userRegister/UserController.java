package lt2021.projektas.userRegister;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "users")
@RequestMapping(value = "/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "Get users list", notes = "Returns all users")
	public List<UserServiceLayer> getUsers() {
		return userService.getUsers();
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.GET)
	public UserServiceLayer getSingleUser(@PathVariable final long userId) {
		return userService.getSingleUser(userId);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create user", notes = "Creates users with data")
	public List<UserServiceLayer> createUser(
			@ApiParam(value = "User Data", required = true) @RequestBody final UserDatabaseLayer user) {
		userService.createUser(
				new UserServiceLayer(user.getFirstname(), user.getLastname(), user.getEmail(), user.getRole()));
		return userService.getUsers();
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
	public void updateUser(@PathVariable final long userId, @Valid @RequestBody final UserDatabaseLayer user) {
		userService.updateUser(
				new UserServiceLayer(userId, user.getFirstname(), user.getLastname(), user.getEmail(), user.getRole()));
	}

//	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete user", notes = "Deletes user by id")
	public void deleteUser(@PathVariable final long userId) {
		userService.deleteUser(userId);
	}

}
