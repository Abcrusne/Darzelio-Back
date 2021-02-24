package lt2021.projektas.kindergarten.admission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.kindergarten.queue.AgeGroup;
import lt2021.projektas.kindergarten.queue.QueueService;

@Service
public class AdmissionService {
	
	@Autowired
	private AdmissionDao admissionDao;
	
	@Autowired
	private QueueService queueService;
	
	@Transactional
	public List<AdmissionTableObject> getAllAdmissionProcesses() {
		var adm = admissionDao.findAll().stream().filter(ad -> ad.isActive()).findFirst().orElse(null);
		if (adm != null) {
			queueService.updateKindergartenQueues(adm.getId());
		}
		return admissionDao.findAll().stream()
				.map(admission -> new AdmissionTableObject(admission.getId(),
						new SimpleDateFormat("yyyy-MM-dd").format(admission.getStartDate()),
						(admission.getEndDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(admission.getEndDate()) : ""),
						admission.isActive(),
						admission.isActive() ? queueService.getNumberOfRegistrationsByAdmission(null) : queueService.getNumberOfRegistrationsByAdmission(admission),
						admission.getQueues().stream().map(queue -> queue.getAgeGroup().equals(AgeGroup.PRESCHOOL) ?
								queue.getKindergarten().getSpotsInFirstAgeGroup() : queue.getKindergarten().getSpotsInSecondAgeGroup()).reduce(0, (acc, elem) -> acc + elem),
						admission.getQueues().stream().allMatch(queue -> queue.isApproved())))
				.collect(Collectors.toList());
	}
	
	@Transactional
	public ResponseEntity<String> createNewAdmissionProcess() {
		if (admissionDao.findAll().stream().filter(ad -> ad.isActive()).findFirst().orElse(null) != null) {
			return new ResponseEntity<String>("Yra dar neužbaigtas priėmimo procesas", HttpStatus.BAD_REQUEST);
		}
		if (admissionDao.findAll().stream().anyMatch(ad -> !(ad.getQueues().stream().allMatch(queue -> queue.isApproved())))) {
			return new ResponseEntity<String>("Yra priėmimų su nepatvirtintomis eilėmis", HttpStatus.BAD_REQUEST);
		}
		var admission = admissionDao.save(new AdmissionProcess());
		queueService.createKindergartenQueues(admission);
		return new ResponseEntity<String>(new SimpleDateFormat("yyyy-MM-dd").format(admission.getStartDate()).toString(), HttpStatus.OK);
	}
	
	
	@Transactional
	public ResponseEntity<String> closeAdmissionProcess() {
		var admission = admissionDao.findAll().stream().filter(ad -> ad.isActive()).findFirst().orElse(null);
		if (admission != null) {
			queueService.updateKindergartenQueues(admission.getId());
			admission.setActive(false);
			admission.setEndDate(new Date());
			var admissionRegistrations = admission.getRegistrations();
			admission.getQueues().stream()
				.flatMap(queue -> queue.getRegistrations().stream())
				.forEach(reg ->  {
					admissionRegistrations.add(reg);
					reg.setAdmission(admission);
				});
			admission.setRegistrations(admissionRegistrations);
			admissionDao.save(admission);
			return new ResponseEntity<String>(new SimpleDateFormat("yyyy-MM-dd").format(admission.getEndDate()).toString(), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Nėra pradėto priėmimo proceso", HttpStatus.BAD_REQUEST);
	}

}
