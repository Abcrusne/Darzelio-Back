package lt2021.projektas.kindergarten.admission;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> createNewAdmissionProcess() {
		if (admissionDao.findAll().stream().filter(ad -> ad.isActive()).findFirst().orElse(null) != null) {
			return new ResponseEntity<String>("Yra dar neužbaigtas priėmimo procesas", HttpStatus.BAD_REQUEST);
		}
		var admission = new AdmissionProcess();
		var queues = queueService.createKindergartenQueues(admissionDao.save(admission)).stream()
				.collect(Collectors.toSet());
		admission.setQueues(queues);
		admissionDao.save(admission);
		return new ResponseEntity<String>("Sėkmingai pradėtas naujas priėmimo procesas", HttpStatus.OK);
	}
	
	@Transactional
	public void updateAdmissionProcess() {
		var admission = admissionDao.findAll().stream().filter(ad -> ad.isActive()).findFirst().orElse(null);
		if (admission != null) {
			var queues = queueService.createKindergartenQueues(admissionDao.save(admission)).stream()
					.collect(Collectors.toSet());
			admission.setQueues(queues);
			admissionDao.save(admission);
		}
	}

}
