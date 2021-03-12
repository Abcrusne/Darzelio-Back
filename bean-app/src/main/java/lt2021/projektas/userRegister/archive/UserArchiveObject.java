package lt2021.projektas.userRegister.archive;

public class UserArchiveObject {
	
	private Long id;
	private String email;
	private String filename;
	
	public UserArchiveObject() {
		// TODO Auto-generated constructor stub
	}

	public UserArchiveObject(Long id, String email, String filename) {
		super();
		this.id = id;
		this.email = email;
		this.filename = filename;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
	
}
