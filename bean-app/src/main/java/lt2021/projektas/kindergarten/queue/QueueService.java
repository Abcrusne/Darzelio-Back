package lt2021.projektas.kindergarten.queue;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	public List<KindergartenQueue> createKindergartenQueues(AdmissionProcess admissionProcess) {
		//var kindergartensInAdmission = admissionProcess.get
		var kindergartens = kindergartenDao.findAll();
		Date today = new Date();
		for (Kindergarten kg: kindergartens) {
			if (kg.getQueues().size() == 0) {
				var preSchoolQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.PRESCHOOL);
				var kgQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.KINDERGARTEN);
				if (kg.getSpotsInFirstAgeGroup() > 0 && kg.getSpotsInSecondAgeGroup() > 0) {
					var registrations = registrationDao.findRegistrationsWithSpecifiedKindergarten(kg.getName());
					for (KindergartenRegistration kgReg: registrations) {
						var timeDiff = today.getTime() - kgReg.getChild().getBirthdate().getTime();
						var timeDiffDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
						var yearDiff = timeDiffDays / 365;
						if (yearDiff >= 2 && yearDiff < 3) {
							var currentRegistrations = preSchoolQueue.getRegistrations();
							currentRegistrations.add(kgReg);
							preSchoolQueue.setRegistrations(currentRegistrations);
							var regQueues = kgReg.getQueues();
							regQueues.add(preSchoolQueue);
							kgReg.setQueues(regQueues);
						} else if (yearDiff >= 3 && yearDiff <= 6) {
							var currentRegistrations = kgQueue.getRegistrations();
							currentRegistrations.add(kgReg);
							kgQueue.setRegistrations(currentRegistrations);
							var regQueues = kgReg.getQueues();
							regQueues.add(kgQueue);
							kgReg.setQueues(regQueues);
						}
					}
				} else if (kg.getSpotsInFirstAgeGroup() == 0) {
					var registrations = registrationDao.findRegistrationsWithSpecifiedKindergarten(kg.getName());
					for (KindergartenRegistration kgReg: registrations) {
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
				} else if (kg.getSpotsInSecondAgeGroup() == 0) {
					var registrations = registrationDao.findRegistrationsWithSpecifiedKindergarten(kg.getName());
					for (KindergartenRegistration kgReg: registrations) {
						var timeDiff = today.getTime() - kgReg.getChild().getBirthdate().getTime();
						var timeDiffDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
						var yearDiff = timeDiffDays / 365;
						if (yearDiff >= 2 && yearDiff < 3) {
							var currentRegistrations = preSchoolQueue.getRegistrations();
							currentRegistrations.add(kgReg);
							preSchoolQueue.setRegistrations(currentRegistrations);
							var regQueues = kgReg.getQueues();
							regQueues.add(preSchoolQueue);
							kgReg.setQueues(regQueues);
						}
					}
				}
				var kgQueues = kg.getQueues();
				kgQueues.add(preSchoolQueue);
				kgQueues.add(kgQueue);
				kg.setQueues(kgQueues);
				kindergartenDao.save(kg);
			} else {
				for (KindergartenQueue kgQueue: kg.getQueues()) {
					if (kgQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)) {
						var registrations = registrationDao.findRegistrationsWithSpecifiedKindergarten(kg.getName());
						for (KindergartenRegistration kgReg: registrations) {
							if (!(kgQueue.getRegistrations().contains(kgReg))) {
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
									queueDao.save(kgQueue);
								}
							}
						}
					} else if (kgQueue.getAgeGroup().equals(AgeGroup.KINDERGARTEN)) {
						var registrations = registrationDao.findRegistrationsWithSpecifiedKindergarten(kg.getName());
						for (KindergartenRegistration kgReg: registrations) {
							if (!(kgQueue.getRegistrations().contains(kgReg))) {
								var timeDiff = today.getTime() - kgReg.getChild().getBirthdate().getTime();
								var timeDiffDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
								var yearDiff = timeDiffDays / 365;
								if (yearDiff >= 3 && yearDiff <= 6) {
									System.out.println("?????????????? YEAR CHECK PASSED ????????????");
									var currentRegistrations = kgQueue.getRegistrations();
									currentRegistrations.add(kgReg);
									kgQueue.setRegistrations(currentRegistrations);
									var regQueues = kgReg.getQueues();
									regQueues.add(kgQueue);
									kgReg.setQueues(regQueues);
									queueDao.save(kgQueue);
								}
							}
						}
					}
					/*
					 * var kgQueues = kg.getQueues(); kgQueues.add(kgQueue);
					 * kg.setQueues(kgQueues);
					 * kindergartenDao.save(kg);
					 */
				}
			}
		}
		return queueDao.findAll();
	}
	
	
}
