package lt2021.projektas.userRegister;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class CreateUserCommand {

	private String firstname;
	private String lastname;
	private String email;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	public CreateUserCommand() {
		super();
	}

	public CreateUserCommand(String firstname, String lastname, String email, UserRole role) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.role = role;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getEmail() {
		return email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

}