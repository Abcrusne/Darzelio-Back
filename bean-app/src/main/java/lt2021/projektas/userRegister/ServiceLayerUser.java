package lt2021.projektas.userRegister;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class ServiceLayerUser {

	private Long id;
	private String firstname;
	private String lastname;
	private String email;

	@Enumerated(EnumType.STRING)
	private UserRole role;
  
	private String password;
//		private String confirmPassword;
	
	private boolean markedForDeletion;

	public ServiceLayerUser() {
		super();
	}


	public ServiceLayerUser(String firstname, String lastname, String email, String password, UserRole role) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.role = role;
		this.password = password;
	}


	public ServiceLayerUser(Long id, String firstname, String lastname, String email, String password, UserRole role, boolean markedForDeletion) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.role = role;
		this.password = password;
		this.markedForDeletion = markedForDeletion;
	}

	public Long getId() {
		return id;
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

	public void setId(Long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}


	public void setMarkedForDeletion(boolean markedForDeletion) {
		this.markedForDeletion = markedForDeletion;
	}
	
	

}
