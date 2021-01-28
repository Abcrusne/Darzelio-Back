package lt2021.projektas;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import lt2021.projektas.userRegister.ServiceLayerUser;
import lt2021.projektas.userRegister.User;
import lt2021.projektas.userRegister.UserRole;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	private static final String URI = "/api/users";

	@Autowired
	private TestRestTemplate rest;

	@Test
	public void createsUserThenRetrievesUserListAndDeletesUser() {

//		final long id = 2;
		final String firstname = "Testa";
		final String lastname = "Testauskaite";
		final String email = "test@mail.com";
		final String password = "Slaptazodis1";
		UserRole role = UserRole.PARENT;

		final ServiceLayerUser createUser = new ServiceLayerUser();

		createUser.setFirstname(firstname);
		createUser.setLastname(lastname);
		createUser.setEmail(email);
		createUser.setPassword(password);

		createUser.setRole(role);
//		createUser.setId(id);

		rest.postForLocation(URI, createUser);
		List<User> users = rest.getForEntity(URI, List.class).getBody();
//
//		Assert.assertThat(users.size(), CoreMatchers.is(1));

//		final Long id = createUser.getId();
//		rest.delete(URI + "/" + id);
//		users = rest.getForEntity(URI, List.class).getBody();
//		Assert.assertThat(users.size(), CoreMatchers.is(0));
//
	}
}
