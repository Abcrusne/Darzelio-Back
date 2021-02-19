package lt2021.projektas.kindergarten.admission;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.kindergarten.queue.QueueService;

@Service
public class AdmissionService {
	
	@Autowired
	private AdmissionDao admissionDao;
	
	@Autowired
	private QueueService queueService;
	
	@Transactional
	public void createNewAdmissionProcess() {
		var admission = new AdmissionProcess();
		var queues = queueService.createKindergartenQueues(admissionDao.save(admission)).stream()
				.collect(Collectors.toSet());
		admission.setQueues(queues);
		admissionDao.save(admission);
	}
	
	@Transactional
	public void updateAdmissionProcess(long admissionId) {
		var admission = admissionDao.findById(admissionId).orElse(null);
		var queues = queueService.createKindergartenQueues(admissionDao.save(admission)).stream()
				.collect(Collectors.toSet());
		admission.setQueues(queues);
		admissionDao.save(admission);
	}

}
