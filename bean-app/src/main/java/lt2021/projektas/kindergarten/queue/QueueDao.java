package lt2021.projektas.kindergarten.queue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lt2021.projektas.kindergarten.admission.AdmissionProcess;

public interface QueueDao extends JpaRepository<KindergartenQueue, Long> {
	
	List<KindergartenQueue> findByAdmissionProcess(AdmissionProcess admission);

}
