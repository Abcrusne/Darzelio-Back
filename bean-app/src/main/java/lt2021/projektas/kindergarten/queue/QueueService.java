package lt2021.projektas.kindergarten.queue;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.kindergarten.Kindergarten;
import lt2021.projektas.kindergarten.KindergartenDao;
import lt2021.projektas.kindergarten.admission.AdmissionDao;
import lt2021.projektas.kindergarten.admission.AdmissionProcess;
import lt2021.projektas.kindergarten.admission.AdmissionService;
import lt2021.projektas.kindergarten.registration.KindergartenRegistration;
import lt2021.projektas.kindergarten.registration.KindergartenRegistrationDao;

@Service
public class QueueService {

	@Autowired
	private QueueDao queueDao;

	@Autowired
	private AdmissionService admissionService;

	@Autowired
	private AdmissionDao admissionDao;

	@Autowired
	private KindergartenDao kindergartenDao;

	@Autowired
	private KindergartenRegistrationDao registrationDao;

	@Transactional
	public void updateKindergartenQueues(long admissionId) {
		var admission = admissionDao.findById(admissionId).orElse(null);
		var kindergartens = kindergartenDao.findAll();

		if (admission != null) {
			if (admission.isActive()) {
				this.createKindergartenQueues(admission);
				for (Kindergarten kg : kindergartens) {
					var kgQueues = kg.getQueues().stream()
							.filter(queue -> queue.getAdmissionProcess().equals(admission))
							.collect(Collectors.toList());
					for (KindergartenQueue kgQueue : kgQueues) {
						updateKindergartenAdmissionQueue(kgQueue, kg);
					}
					kindergartenDao.save(kg);
				}
			}
		}
	}

	public void updateKindergartenAdmissionQueue(KindergartenQueue kgQueue, Kindergarten kg) {
		Date today = new Date();
		if (!kgQueue.isApproved()) {
			if (kgQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)) {
				var registrations = registrationDao.findRegistrationsWithSpecifiedKindergarten(kg.getName());
				for (KindergartenRegistration kgReg : registrations) {
					var timeDiff = today.getTime() - kgReg.getChild().getBirthdate().getTime();
					var timeDiffDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
					var yearDiff = timeDiffDays / 365;
					if (yearDiff >= 2 && yearDiff < 3) {
						var currentRegistrations = kgQueue.getRegistrations();
						currentRegistrations.add(kgReg);
						kgQueue.setRegistrations(currentRegistrations);
						var regQueues = kgReg.getQueues();
						regQueues.add(kgQueue);
						kgReg.setQueues(regQueues);
					}
				}
			} else if (kgQueue.getAgeGroup().equals(AgeGroup.KINDERGARTEN)) {
				var registrations = registrationDao.findRegistrationsWithSpecifiedKindergarten(kg.getName());
				for (KindergartenRegistration kgReg : registrations) {
					var timeDiff = today.getTime() - kgReg.getChild().getBirthdate().getTime();
					var timeDiffDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
					var yearDiff = timeDiffDays / 365;
					if (yearDiff >= 3 && yearDiff <= 6) {
						var currentRegistrations = kgQueue.getRegistrations();
						currentRegistrations.add(kgReg);
						kgQueue.setRegistrations(currentRegistrations);
						var regQueues = kgReg.getQueues();
						regQueues.add(kgQueue);
						kgReg.setQueues(regQueues);
					}
				}
			}
		}
	}

	@Transactional
	public List<KindergartenQueue> getCurrentAdmissionProcessQueues(long admissionId) {
		var admission = admissionDao.findById(admissionId).orElse(null);
		updateKindergartenQueues(admissionId);
		if (admission != null) {
			return admission.getQueues().stream().collect(Collectors.toList());
		}
		return null;
	}

	@Transactional
	public AdmissionProcess createKindergartenQueues(AdmissionProcess admissionProcess) {

		var kindergartens = kindergartenDao.findAll();
		var admissionQueues = admissionProcess.getQueues();
		for (Kindergarten kg : kindergartens) {
			if (!(kg.getQueues().stream().anyMatch(queue -> queue.getAdmissionProcess().equals(admissionProcess)))) {
				var preSchoolQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.PRESCHOOL);
				var kgQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.KINDERGARTEN);
				if (kg.getSpotsInFirstAgeGroup() > 0 && kg.getSpotsInSecondAgeGroup() > 0) {
					var kgQueues = kg.getQueues();
					kgQueues.add(preSchoolQueue);
					kgQueues.add(kgQueue);
					kg.setQueues(kgQueues);
					admissionQueues.add(preSchoolQueue);
					admissionQueues.add(kgQueue);
				} else if (kg.getSpotsInFirstAgeGroup() == 0) {
					var kgQueues = kg.getQueues();
					kgQueues.add(kgQueue);
					kg.setQueues(kgQueues);
					admissionQueues.add(kgQueue);
				} else if (kg.getSpotsInSecondAgeGroup() == 0) {
					var kgQueues = kg.getQueues();
					kgQueues.add(preSchoolQueue);
					kg.setQueues(kgQueues);
					admissionQueues.add(preSchoolQueue);
				}
				kindergartenDao.save(kg);
			} else {
				var adQueues = kg.getQueues().stream()
						.filter(queue -> queue.getAdmissionProcess().equals(admissionProcess))
						.collect(Collectors.toList());
				if (adQueues.stream().filter(queue -> queue.getAgeGroup().equals(AgeGroup.PRESCHOOL)).findAny()
						.orElse(null) == null && kg.getSpotsInFirstAgeGroup() > 0) {
					var preSchoolQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.PRESCHOOL);
					var kgQueues = kg.getQueues();
					kgQueues.add(preSchoolQueue);
					kg.setQueues(kgQueues);
					admissionQueues.add(preSchoolQueue);
				} else if (adQueues.stream().filter(queue -> queue.getAgeGroup().equals(AgeGroup.KINDERGARTEN))
						.findAny().orElse(null) == null && kg.getSpotsInSecondAgeGroup() > 0) {
					var kgQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.KINDERGARTEN);
					var kgQueues = kg.getQueues();
					kgQueues.add(kgQueue);
					kg.setQueues(kgQueues);
					admissionQueues.add(kgQueue);
				}
				kindergartenDao.save(kg);
			}
		}
		admissionProcess.setQueues(admissionQueues);
		return admissionDao.save(admissionProcess);
	}

	public int getNumberOfRegistrationsByAdmission(AdmissionProcess admission) {
		if (admission != null) {
			return registrationDao.findRegistrationsbyAdmission(admission).size();
		}
		return registrationDao.findRegistrationsWithNullAdmission().size();
	}

	@Transactional
	public ResponseEntity<String> approveAdmissionQueue(long admissionId, long queueId) {
		var queue = queueDao.findById(queueId).orElse(null);
		var admission = admissionDao.findById(admissionId).orElse(null);
		if (queue != null) {
			if (!admission.isActive()) {
				queue.setApproved(true);
				queueDao.save(queue);
				return new ResponseEntity<String>("Eilė patvirtinta", HttpStatus.OK);
			}
			return new ResponseEntity<String>("Priėmimas dar neuždarytas", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Eilė nerasta", HttpStatus.BAD_REQUEST);
	}

}
