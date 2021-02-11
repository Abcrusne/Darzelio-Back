package lt2021.projektas.child;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lt2021.projektas.kindergarten.registration.KindergartenRegistration;
import lt2021.projektas.parentdetails.Address;
import lt2021.projektas.parentdetails.ParentDetails;

@Entity
public class Child {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String firstname;

	@NotBlank
	private String lastname;

	@NotNull
	@Min(value = 30000000000L, message = "Personal code must be valid")
	@Max(value = 49999999999L, message = "Personal code must be valid")
	@Column(length = 11, unique = true)
	private long personalCode;
	
	@NotNull
	private boolean isAdopted;

	@Temporal(TemporalType.DATE)
	private Date birthdate;

	@Embedded
	private Address livingAddress;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Parents_Children", joinColumns = @JoinColumn(name = "Child_id"), inverseJoinColumns = @JoinColumn(name = "Parent_details_id"))
	private Set<ParentDetails> parents;
	
	@OneToOne(mappedBy = "child")
	private KindergartenRegistration registrationForm;

	public Child() {
	}

	public Child(@NotBlank String firstname, @NotBlank String lastname, @NotNull long personalCode,
			@NotNull boolean isAdopted, Date birthdate, Address livingAddress) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.personalCode = personalCode;
		this.isAdopted = isAdopted;
		this.birthdate = birthdate;
		this.livingAddress = livingAddress;
		this.parents = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public long getPersonalCode() {
		return personalCode;
	}

	public void setPersonalCode(long personalCode) {
		this.personalCode = personalCode;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Address getLivingAddress() {
		return livingAddress;
	}

	public void setLivingAddress(Address livingAddress) {
		this.livingAddress = livingAddress;
	}

	public Set<ParentDetails> getParents() {
		return parents;
	}

	public void setParents(Set<ParentDetails> parents) {
		this.parents = parents;
	}

	public boolean isAdopted() {
		return isAdopted;
	}

	public void setAdopted(boolean isAdopted) {
		this.isAdopted = isAdopted;
	}
	
	

}
