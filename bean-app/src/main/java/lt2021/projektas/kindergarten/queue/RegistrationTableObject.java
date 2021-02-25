package lt2021.projektas.kindergarten.queue;

public class RegistrationTableObject {

	private long childId;
	private int position;
	private String firstname;
	private String lastname;
	private String kindergartenName;
	private String ageGroup;
	private int rating;
	private boolean accepted;

	public RegistrationTableObject() {
	}

	public RegistrationTableObject(long childId, int position, String firstname, String lastname,
			String kindergartenName, String ageGroup, int rating, boolean accepted) {
		super();
		this.childId = childId;
		this.position = position;
		this.firstname = firstname;
		this.lastname = lastname;
		this.kindergartenName = kindergartenName;
		this.ageGroup = ageGroup;
		this.rating = rating;
		this.accepted = accepted;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public long getChildId() {
		return childId;
	}

	public void setChildId(long childId) {
		this.childId = childId;
	}

	public String getKindergartenName() {
		return kindergartenName;
	}

	public void setKindergartenName(String kindergartenName) {
		this.kindergartenName = kindergartenName;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

}
