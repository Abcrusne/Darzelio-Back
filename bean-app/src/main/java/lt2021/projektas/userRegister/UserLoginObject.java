package lt2021.projektas.userRegister;

public class UserLoginObject {

	public String email;
	public String password;
	
	public UserLoginObject() {
	}

	public UserLoginObject(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
