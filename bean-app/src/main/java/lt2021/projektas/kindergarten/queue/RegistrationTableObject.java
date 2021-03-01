package lt2021.projektas.kindergarten.queue;

import java.util.List;

public class RegistrationTableObject {
	
	private int pageNumber;
	private double pageCount;
	private List<RegistrationTableItem> registrations;
	
	public RegistrationTableObject() {
		// TODO Auto-generated constructor stub
	}

	public RegistrationTableObject(int pageNumber, double pageCount, List<RegistrationTableItem> registrations) {
		super();
		this.pageNumber = pageNumber;
		this.pageCount = pageCount;
		this.registrations = registrations;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public double getPageCount() {
		return pageCount;
	}

	public void setPageCount(double pageCount) {
		this.pageCount = pageCount;
	}

	public List<RegistrationTableItem> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<RegistrationTableItem> registrations) {
		this.registrations = registrations;
	}
	
	

}
