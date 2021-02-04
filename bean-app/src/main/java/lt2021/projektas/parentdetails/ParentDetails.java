package lt2021.projektas.parentdetails;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lt2021.projektas.userRegister.User;

@Entity
public class ParentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(mappedBy = "parentDetails")
	private User parent;

	@NotBlank
	private String phone;

	@NotNull
	@Column(length = 11)
	private long personalCode;

	@Embedded
	private Address livingAddress;

	@NotNull
	private int numberOfKids;
	
	@NotNull
	private boolean isStudying;

	private String studyingInstitution;
	
	@NotNull
	private boolean hasDisability;
	
	@NotNull
	private boolean declaredResidenceSameAsLiving;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "city", column = @Column(name = "declared_city")),
		@AttributeOverride(name = "street", column = @Column(name = "declared_street")),
		@AttributeOverride(name = "houseNumber", column = @Column(name = "declared_houseNumber")),
		@AttributeOverride(name = "flatNumber", column = @Column(name = "declared_flatNumber"))
	})
	private Address declaredAddress;

	public ParentDetails() {
	}

	public ParentDetails(@NotBlank String phone, @NotBlank Long personalCode, Address livingAddress,
			@NotBlank int numberOfKids, @NotBlank boolean isStudying, String studyingInstitution,
			@NotBlank boolean hasDisability, @NotBlank boolean declaredResidenceSameAsLiving, Address declaredAddress) {
		super();
		this.phone = phone;
		this.personalCode = personalCode;
		this.livingAddress = livingAddress;
		this.numberOfKids = numberOfKids;
		this.isStudying = isStudying;
		this.studyingInstitution = studyingInstitution;
		this.hasDisability = hasDisability;
		this.declaredResidenceSameAsLiving = declaredResidenceSameAsLiving;
		this.declaredAddress = declaredAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getParent() {
		return parent;
	}

	public void setParent(User parent) {
		this.parent = parent;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getPersonalCode() {
		return personalCode;
	}

	public void setPersonalCode(Long personalCode) {
		this.personalCode = personalCode;
	}

	public int getNumberOfKids() {
		return numberOfKids;
	}

	public void setNumberOfKids(int numberOfKids) {
		this.numberOfKids = numberOfKids;
	}

	public String getStudyingInstitution() {
		return studyingInstitution;
	}

	public void setStudyingInstitution(String studyingInstitution) {
		this.studyingInstitution = studyingInstitution;
	}

	public boolean isHasDisability() {
		return hasDisability;
	}

	public void setHasDisability(boolean hasDisability) {
		this.hasDisability = hasDisability;
	}

	public boolean isDeclaredResidenceSameAsLiving() {
		return declaredResidenceSameAsLiving;
	}

	public void setDeclaredResidenceSameAsLiving(boolean declaredResidenceSameAsLiving) {
		this.declaredResidenceSameAsLiving = declaredResidenceSameAsLiving;
	}

	public Address getLivingAddress() {
		return livingAddress;
	}

	public void setLivingAddress(Address livingAddress) {
		this.livingAddress = livingAddress;
	}

	public Address getDeclaredAddress() {
		return declaredAddress;
	}

	public void setDeclaredAddress(Address declaredAddress) {
		this.declaredAddress = declaredAddress;
	}

	public boolean isStudying() {
		return isStudying;
	}

	public void setStudying(boolean isStudying) {
		this.isStudying = isStudying;
	}
	
	

}
