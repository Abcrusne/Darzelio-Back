package lt2021.projektas.kindergarten.queue;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lt2021.projektas.kindergarten.Kindergarten;
import lt2021.projektas.kindergarten.admission.AdmissionProcess;
import lt2021.projektas.kindergarten.registration.KindergartenRegistration;

@Entity
@Table(name = "Queue")
public class KindergartenQueue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Kindergarten kindergarten;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Queue_registrations", joinColumns = @JoinColumn(name = "queue_id"), inverseJoinColumns = @JoinColumn(name = "registration_id"))
	private Set<KindergartenRegistration> registrations;
	
	@ManyToOne
	private AdmissionProcess admissionProcess;
	
	@Enumerated(EnumType.STRING)
	private AgeGroup ageGroup;
	
	private boolean approved;
	
	public KindergartenQueue() {
		// TODO Auto-generated constructor stub
	}

	public KindergartenQueue(AdmissionProcess admissionProcess, Kindergarten kindergarten, AgeGroup ageGroup) {
		super();
		this.admissionProcess = admissionProcess;
		this.kindergarten = kindergarten;
		this.ageGroup = ageGroup;
		this.approved = false;
		this.registrations = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Kindergarten getKindergarten() {
		return kindergarten;
	}

	public void setKindergarten(Kindergarten kindergarten) {
		this.kindergarten = kindergarten;
	}

	public Set<KindergartenRegistration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(Set<KindergartenRegistration> registrations) {
		this.registrations = registrations;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public AdmissionProcess getAdmissionProcess() {
		return admissionProcess;
	}

	public void setAdmissionProcess(AdmissionProcess admissionProcess) {
		this.admissionProcess = admissionProcess;
	}

	public AgeGroup getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(AgeGroup ageGroup) {
		this.ageGroup = ageGroup;
	}
	
	
	
	
}
