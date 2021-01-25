package lt2021.projektas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lt2021.projektas.userRegister.UserLoginObject;
import lt2021.projektas.userRegister.UserService;

@RestController
public class HelloController {
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
	
	
	private final UserService userService;
	
	@Autowired
	public HelloController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/hello")
	String hello() {
		return "Hello World!";
	}

	@Data
	static class Result {

		private final int left;
		private final int right;
		private final long answer;

		public Result(int left, int right, long answer) {
			this.left = left;
			this.right = right;
			this.answer = answer;
		}

		public int getLeft() {
			return left;
		}

		public int getRight() {
			return right;
		}

		public long getAnswer() {
			return answer;
		}

	}
	
	@RequestMapping(path = "/loggedUsername", method = RequestMethod.GET)
	public String getLoggedInUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			return currentUserName;
		}
		return "not logged";
	}

	// SQL sample
	@RequestMapping("calc")
	@PreAuthorize("hasRole('PARENT')")
	Result calc(@RequestParam int left, @RequestParam int right) {
		MapSqlParameterSource source = new MapSqlParameterSource().addValue("left", left).addValue("right", right);
		return jdbcTemplate.queryForObject("SELECT :left + :right AS answer", source,
				(rs, rowNum) -> new Result(left, right, rs.getLong("answer")));
	}
	
	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public void loginWithUser(@RequestBody final UserLoginObject user) {
		
	}
}
