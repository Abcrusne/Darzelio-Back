package lt2021.projektas;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import lt2021.projektas.userRegister.User;
import lt2021.projektas.userRegister.UserServiceLayer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { App.class })
public class UserControllerTestas {

	private static final String URI = "/api/users";

	@Autowired
	private TestRestTemplate rest;

	@Test
	public void createsUserThenRetrievesUserListAndDeletesUser() {

		final String firstname = "Testas";
		final String lastname = "testLastName";
		final String email = "testas@gmail.com";

		final UserServiceLayer createUser = new UserServiceLayer();

		createUser.setFirstname(firstname);
		createUser.setLastname(lastname);
		createUser.setEmail(email);

		final long id = createUser.getId();

		rest.postForLocation(URI, createUser);
		List<User> users = rest.getForEntity(URI, List.class).getBody();

		Assert.assertThat(users.size(), CoreMatchers.is(1));

		rest.delete(URI + "/" + id);
		users = rest.getForEntity(URI, List.class).getBody();
		Assert.assertThat(users.size(), CoreMatchers.is(0));

	}
}
