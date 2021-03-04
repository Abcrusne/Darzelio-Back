package lt2021.projektas.child;

public class ChildStatusObject {
	
	private boolean applicationFilled;
	private boolean applicationAccepted;
	private String notAcceptedReason;
	private int placeInQueue;
	private String acceptedKindergarten;
	
	public ChildStatusObject() {
		// TODO Auto-generated constructor stub
	}

	public ChildStatusObject(boolean applicationFilled, boolean applicationAccepted, String notAcceptedReason,
			int placeInQueue, String acceptedKindergarten) {
		super();
		this.applicationFilled = applicationFilled;
		this.applicationAccepted = applicationAccepted;
		this.notAcceptedReason = notAcceptedReason;
		this.placeInQueue = placeInQueue;
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

	public int getPlaceInQueue() {
		return placeInQueue;
	}

	public void setPlaceInQueue(int placeInQueue) {
		this.placeInQueue = placeInQueue;
	}

	public String getAcceptedKindergarten() {
		return acceptedKindergarten;
	}

	public void setAcceptedKindergarten(String acceptedKindergarten) {
		this.acceptedKindergarten = acceptedKindergarten;
	}
	
	
	
}
