package lt2021.projektas.kindergarten.queue;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lt2021.projektas.kindergarten.Kindergarten;

@Entity
public class KindergartenQueue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(cascade= CascadeType.ALL)
	@Column(name = "kindergarten_ID")
	private Kindergarten kindergarten;
	
	
}
