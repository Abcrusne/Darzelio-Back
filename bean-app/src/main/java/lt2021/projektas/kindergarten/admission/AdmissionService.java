package lt2021.projektas.kindergarten.admission;

import java.text.SimpleDateFormat;
import java.util.Date;
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
		var admission = admissionDao.save(new AdmissionProcess());
		var queues = queueService.createKindergartenQueues(admission).stream()
				.collect(Collectors.toSet());
		admission.setQueues(queues);
		admissionDao.save(admission);
		return new ResponseEntity<String>(new SimpleDateFormat("yyyy-MM-dd").format(admission.getStartDate()).toString(), HttpStatus.OK);
	}
	
	
	@Transactional
	public ResponseEntity<String> closeAdmissionProcess() {
		var admission = admissionDao.findAll().stream().filter(ad -> ad.isActive()).findFirst().orElse(null);
		if (admission != null) {
			admission.setActive(false);
			admission.setEndDate(new Date());
			queueService.updateKindergartenQueues(admission.getId());
			admissionDao.save(admission);
			return new ResponseEntity<String>(new SimpleDateFormat("yyyy-MM-dd").format(admission.getEndDate()).toString(), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Nėra pradėto priėmimo proceso", HttpStatus.BAD_REQUEST);
	}

}
