package lt2021.projektas.kindergarten.admission;

public class AdmissionStatusObject {
	
	private int registrationsInFirstAgeGroup;
	private int registrationsInSecondAgeGroup;
	private int spotsInFirstAgeGroups;
	private int spotsInSecondAgeGroups;
	private boolean active;
	
	public AdmissionStatusObject() {
		// TODO Auto-generated constructor stub
	}

	public AdmissionStatusObject(int registrationsInFirstAgeGroup, int registrationsInSecondAgeGroup,
			int spotsInFirstAgeGroups, int spotsInSecondAgeGroups, boolean active) {
		super();
		this.registrationsInFirstAgeGroup = registrationsInFirstAgeGroup;
		this.registrationsInSecondAgeGroup = registrationsInSecondAgeGroup;
		this.spotsInFirstAgeGroups = spotsInFirstAgeGroups;
		this.spotsInSecondAgeGroups = spotsInSecondAgeGroups;
		this.active = active;
	}

	public int getRegistrationsInFirstAgeGroup() {
		return registrationsInFirstAgeGroup;
	}

	public void setRegistrationsInFirstAgeGroup(int registrationsInFirstAgeGroup) {
		this.registrationsInFirstAgeGroup = registrationsInFirstAgeGroup;
	}

	public int getRegistrationsInSecondAgeGroup() {
		return registrationsInSecondAgeGroup;
	}

	public void setRegistrationsInSecondAgeGroup(int registrationsInSecondAgeGroup) {
		this.registrationsInSecondAgeGroup = registrationsInSecondAgeGroup;
	}

	public int getSpotsInFirstAgeGroups() {
		return spotsInFirstAgeGroups;
	}

	public void setSpotsInFirstAgeGroups(int spotsInFirstAgeGroups) {
		this.spotsInFirstAgeGroups = spotsInFirstAgeGroups;
	}

	public int getSpotsInSecondAgeGroups() {
		return spotsInSecondAgeGroups;
	}

	public void setSpotsInSecondAgeGroups(int spotsInSecondAgeGroups) {
		this.spotsInSecondAgeGroups = spotsInSecondAgeGroups;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	} 
	
	

}
