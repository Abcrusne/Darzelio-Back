package lt2021.projektas.kindergarten.admission;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lt2021.projektas.kindergarten.queue.KindergartenQueue;

@Entity
public class AdmissionProcess {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(
			inverseJoinColumns = @JoinColumn(name = "queue_id", referencedColumnName = "id")
			)
	private Set<KindergartenQueue> queues;
	
	private boolean active;
	
	public AdmissionProcess() {
		super();
		this.startDate = new Date();
		this.active = true;
		this.queues = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Set<KindergartenQueue> getQueues() {
		return queues;
	}

	public void setQueues(Set<KindergartenQueue> queues) {
		this.queues = queues;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
	

}
