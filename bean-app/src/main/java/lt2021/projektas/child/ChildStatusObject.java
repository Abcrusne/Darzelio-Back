package lt2021.projektas.child;

public class ChildStatusObject {

	private String firstname;
	private String lastname;
	private boolean applicationFilled;
	private boolean applicationAccepted;
	private String notAcceptedReason;
	private int placeInFirstQueue;
	private int placeInSecondQueue;
	private int placeInThirdQueue;
	private int placeInFourthQueue;
	private int placeInFifthQueue;
	private String acceptedKindergarten;

	public ChildStatusObject() {
		// TODO Auto-generated constructor stub
	}

	public ChildStatusObject(String firstname, String lastname, boolean applicationFilled, boolean applicationAccepted,
			String notAcceptedReason, int placeInFirstQueue, int placeInSecondQueue, int placeInThirdQueue,
			int placeInFourthQueue, int placeInFifthQueue, String acceptedKindergarten) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.applicationFilled = applicationFilled;
		this.applicationAccepted = applicationAccepted;
		this.notAcceptedReason = notAcceptedReason;
		this.placeInFirstQueue = placeInFirstQueue;
		this.placeInSecondQueue = placeInSecondQueue;
		this.placeInThirdQueue = placeInThirdQueue;
		this.placeInFourthQueue = placeInFourthQueue;
		this.placeInFifthQueue = placeInFifthQueue;
		this.acceptedKindergarten = acceptedKindergarten;
	}

	public boolean isApplicationFilled() {
		return applicationFilled;
	}

	public void setApplicationFilled(boolean applicationFilled) {
		this.applicationFilled = applicationFilled;
	}

	public boolean isApplicationAccepted() {
		return applicationAccepted;
	}

	public void setApplicationAccepted(boolean applicationAccepted) {
		this.applicationAccepted = applicationAccepted;
	}

	public String getNotAcceptedReason() {
		return notAcceptedReason;
	}

	public void setNotAcceptedReason(String notAcceptedReason) {
		this.notAcceptedReason = notAcceptedReason;
	}

	public int getPlaceInFirstQueue() {
		return placeInFirstQueue;
	}

	public void setPlaceInFirstQueue(int placeInFirstQueue) {
		this.placeInFirstQueue = placeInFirstQueue;
	}

	public int getPlaceInSecondQueue() {
		return placeInSecondQueue;
	}

	public void setPlaceInSecondQueue(int placeInSecondQueue) {
		this.placeInSecondQueue = placeInSecondQueue;
	}

	public int getPlaceInThirdQueue() {
		return placeInThirdQueue;
	}

	public void setPlaceInThirdQueue(int placeInThirdQueue) {
		this.placeInThirdQueue = placeInThirdQueue;
	}

	public int getPlaceInFourthQueue() {
		return placeInFourthQueue;
	}

	public void setPlaceInFourthQueue(int placeInFourthQueue) {
		this.placeInFourthQueue = placeInFourthQueue;
	}

	public int getPlaceInFifthQueue() {
		return placeInFifthQueue;
	}

	public void setPlaceInFifthQueue(int placeInFifthQueue) {
		this.placeInFifthQueue = placeInFifthQueue;
	}

	public String getAcceptedKindergarten() {
		return acceptedKindergarten;
	}

	public void setAcceptedKindergarten(String acceptedKindergarten) {
		this.acceptedKindergarten = acceptedKindergarten;
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

}
