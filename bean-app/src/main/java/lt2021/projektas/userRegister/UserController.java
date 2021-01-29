package lt2021.projektas.userRegister;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	public List<ServiceLayerUser> getUsers() {
		return userService.getUsers();
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.GET)
	public ServiceLayerUser getSingleUser(@PathVariable final long userId) {
		return userService.getSingleUser(userId);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create user", notes = "Creates users with data")
	public List<ServiceLayerUser> createUser(
			@ApiParam(value = "User Data", required = true) @RequestBody final CreateUserCommand user) {
		userService.createUser(user);
		return userService.getUsers();
	}

	@RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
	public void updateUser(@PathVariable final long userId, @Valid @RequestBody final CreateUserCommand user) {
		userService.updateUser(new ServiceLayerUser(userId, user.getFirstname(), user.getLastname(), user.getEmail(),
				user.getPassword(), user.getRole()));
	}

//	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete user", notes = "Deletes user by id")
	public void deleteUser(@PathVariable final Long userId) {
		userService.deleteUser(userId);
	}
	
	@RequestMapping(path = "/loggedrole", method = RequestMethod.GET)
	public String getLoggedRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			String currentRole = userService.findByEmail(auth.getName()).getRole().toString();
			return currentRole;
		}
		return "not logged";
	}

}
