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
		Date today = new Date();
		if (admission != null) {
			if (admission.isActive()) {
				this.createKindergartenQueues(admission);
				for (Kindergarten kg : kindergartens) {
					var kgQueues = kg.getQueues().stream()
							.filter(queue -> queue.getAdmissionProcess().equals(admission))
							.collect(Collectors.toList());
					for (KindergartenQueue kgQueue : kgQueues) {
						if (!(kgQueue.isApproved())) {
							if (kgQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)) {
								var registrations = registrationDao
										.findRegistrationsWithSpecifiedKindergarten(kg.getName());
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
								var registrations = registrationDao
										.findRegistrationsWithSpecifiedKindergarten(kg.getName());
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
					kindergartenDao.save(kg);
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
	public List<KindergartenQueue> createKindergartenQueues(AdmissionProcess admissionProcess) {

		var kindergartens = kindergartenDao.findAll();

		for (Kindergarten kg : kindergartens) {
			if (kg.getQueues().stream().filter(queue -> queue.getAdmissionProcess().equals(admissionProcess))
					.findFirst().orElse(null) == null) {
				var preSchoolQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.PRESCHOOL);
				var kgQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.KINDERGARTEN);
				if (kg.getSpotsInFirstAgeGroup() > 0 && kg.getSpotsInSecondAgeGroup() > 0) {
					var kgQueues = kg.getQueues();
					kgQueues.add(preSchoolQueue);
					kgQueues.add(kgQueue);
					kg.setQueues(kgQueues);
				} else if (kg.getSpotsInFirstAgeGroup() == 0) {
					var kgQueues = kg.getQueues();
					kgQueues.add(kgQueue);
					kg.setQueues(kgQueues);
				} else if (kg.getSpotsInSecondAgeGroup() == 0) {
					var kgQueues = kg.getQueues();
					kgQueues.add(preSchoolQueue);
					kg.setQueues(kgQueues);
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
				} else if (adQueues.stream().filter(queue -> queue.getAgeGroup().equals(AgeGroup.KINDERGARTEN))
						.findAny().orElse(null) == null && kg.getSpotsInSecondAgeGroup() > 0) {
					var kgQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.KINDERGARTEN);
					var kgQueues = kg.getQueues();
					kgQueues.add(kgQueue);
					kg.setQueues(kgQueues);
				}
				kindergartenDao.save(kg);
			}
		}
		return queueDao.findByAdmissionProcess(admissionProcess);
	}

}
