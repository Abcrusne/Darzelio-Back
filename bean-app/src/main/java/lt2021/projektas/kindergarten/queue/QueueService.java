package lt2021.projektas.kindergarten.queue;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.kindergarten.Kindergarten;
import lt2021.projektas.kindergarten.KindergartenDao;
import lt2021.projektas.kindergarten.admission.AdmissionDao;
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
	public void createNewQueuesForKindergarten(Kindergarten kg) {
		var admission = admissionDao.findAll().get(0);
		if (kg.getSpotsInFirstAgeGroup() > 0) {
			var preSchool = new KindergartenQueue(admission, kg, AgeGroup.PRESCHOOL);
			var queues = kg.getQueues();
			queues.add(preSchool);
			kg.setQueues(queues);
			var adQueues = admission.getQueues();
			adQueues.add(preSchool);
			admission.setQueues(adQueues);
		}
		if (kg.getSpotsInSecondAgeGroup() > 0) {
			var kindergarten = new KindergartenQueue(admission, kg, AgeGroup.KINDERGARTEN);
			var queues = kg.getQueues();
			queues.add(kindergarten);
			kg.setQueues(queues);
			var adQueues = admission.getQueues();
			adQueues.add(kindergarten);
			admission.setQueues(adQueues);
		}
		kindergartenDao.save(kg);
		admissionDao.save(admission);
	}
	
	@Transactional
	public void addRegistrationToQueues(KindergartenRegistration kgReg) {
		var admission = admissionDao.findAll().get(0);
		Date today = new Date();
		var timeDiff = today.getTime() - kgReg.getChild().getBirthdate().getTime();
		var timeDiffDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
		var yearDiff = timeDiffDays / 365;
		AgeGroup ageGroup = null;
		if (yearDiff >= 2 && yearDiff < 3) {
			ageGroup = AgeGroup.PRESCHOOL;
		} else if (yearDiff >= 3 && yearDiff <= 6) {
			ageGroup = AgeGroup.KINDERGARTEN;
		}
		var kindergarten = kindergartenDao.findByName(kgReg.getFirstPriority()).orElse(null);
		if (kindergarten != null && ageGroup != null) {
			var queue = queueDao.findQueueByKindergartenNameAndAgeGroup(kindergarten, ageGroup).orElse(null);
			if (queue != null) {
				var registrations = queue.getRegistrations();
				registrations.add(kgReg);
				queue.setRegistrations(registrations);
				var queues = kgReg.getQueues();
				queues.add(queue);
				kgReg.setQueues(queues);
				kindergartenDao.save(kindergarten);
			}
			kindergarten = kindergartenDao.findByName(kgReg.getSecondPriority()).orElse(null);
			if (kindergarten != null) {
				queue = queueDao.findQueueByKindergartenNameAndAgeGroup(kindergarten, ageGroup).orElse(null);
				if (queue != null) {
					var registrations = queue.getRegistrations();
					registrations.add(kgReg);
					queue.setRegistrations(registrations);
					var queues = kgReg.getQueues();
					queues.add(queue);
					kgReg.setQueues(queues);
					kindergartenDao.save(kindergarten);
				}
			}
			kindergarten = kindergartenDao.findByName(kgReg.getThirdPriority()).orElse(null);
			if (kindergarten != null) {
				queue = queueDao.findQueueByKindergartenNameAndAgeGroup(kindergarten, ageGroup).orElse(null);
				if (queue != null) {
					var registrations = queue.getRegistrations();
					registrations.add(kgReg);
					queue.setRegistrations(registrations);
					var queues = kgReg.getQueues();
					queues.add(queue);
					kgReg.setQueues(queues);
					kindergartenDao.save(kindergarten);
				}
			}
			kindergarten = kindergartenDao.findByName(kgReg.getFourthPriority()).orElse(null);
			if (kindergarten != null) {
				queue = queueDao.findQueueByKindergartenNameAndAgeGroup(kindergarten, ageGroup).orElse(null);
				if (queue != null) {
					var registrations = queue.getRegistrations();
					registrations.add(kgReg);
					queue.setRegistrations(registrations);
					var queues = kgReg.getQueues();
					queues.add(queue);
					kgReg.setQueues(queues);
					kindergartenDao.save(kindergarten);
				}
			}
			kindergarten = kindergartenDao.findByName(kgReg.getFifthPriority()).orElse(null);
			if (kindergarten != null) {
				queue = queueDao.findQueueByKindergartenNameAndAgeGroup(kindergarten, ageGroup).orElse(null);
				if (queue != null) {
					var registrations = queue.getRegistrations();
					registrations.add(kgReg);
					queue.setRegistrations(registrations);
					var queues = kgReg.getQueues();
					queues.add(queue);
					kgReg.setQueues(queues);
					kindergartenDao.save(kindergarten);
				}
			}
			var adRegistrations = admission.getRegistrations();
			adRegistrations.add(kgReg);
			admission.setRegistrations(adRegistrations);
			kgReg.setAdmission(admission);
			registrationDao.save(kgReg);
			admissionDao.save(admission);
		}
	}
	
	@Transactional
	public List<QueueTableObject> getAllAdmissionQueues() {
		return queueDao.findAll().stream()
				.map(queue -> new QueueTableObject(queue.getId(), queue.getKindergarten().getName(), queue.getAgeGroup().toString(), queue.getRegistrations().size(),
						queue.getAgeGroup().equals(AgeGroup.PRESCHOOL) ? queue.getKindergarten().getSpotsInFirstAgeGroup() : queue.getKindergarten().getSpotsInSecondAgeGroup()))
				.collect(Collectors.toList());
	}
	
}
