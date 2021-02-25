package lt2021.projektas.kindergarten.queue;

import java.util.ArrayList;
import java.util.Comparator;
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
	public List<KindergartenQueue> getCurrentAdmissionProcessQueues(long admissionId) {
		var admission = admissionDao.findById(admissionId).orElse(null);
		updateKindergartenQueues(admissionId);
		if (admission != null) {
			return admission.getQueues().stream().collect(Collectors.toList());
		}
		return null;
	}

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
	public AdmissionProcess createKindergartenQueues(AdmissionProcess admissionProcess) {

		var kindergartens = kindergartenDao.findAll();
		for (Kindergarten kg : kindergartens) {
			if (!(kg.getQueues().stream().anyMatch(queue -> queue.getAdmissionProcess().equals(admissionProcess)))) {
				if (kg.getSpotsInFirstAgeGroup() > 0 && kg.getSpotsInSecondAgeGroup() > 0) {
					var preSchoolQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.PRESCHOOL);
					var kgQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.KINDERGARTEN);
					var kgQueues = kg.getQueues();
					kgQueues.add(preSchoolQueue);
					kgQueues.add(kgQueue);
					kg.setQueues(kgQueues);
				} else if (kg.getSpotsInFirstAgeGroup() == 0) {
					var kgQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.KINDERGARTEN);
					var kgQueues = kg.getQueues();
					kgQueues.add(kgQueue);
					kg.setQueues(kgQueues);
				} else if (kg.getSpotsInSecondAgeGroup() == 0) {
					var preSchoolQueue = new KindergartenQueue(admissionProcess, kg, AgeGroup.PRESCHOOL);
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
		admissionProcess
				.setQueues(queueDao.findByAdmissionProcess(admissionProcess).stream().collect(Collectors.toSet()));
		admissionDao.save(admissionProcess);
		return null;
	}

	@Transactional
	public void calculateWhereAllRegistrationsLanded(long admissionId) {
		var admission = admissionDao.findById(admissionId).orElse(null);
		if (admission != null) {
			var admissionRegistrations = admission.getRegistrations();
			var sortedRegistrations = admissionRegistrations.stream().collect(Collectors.toList());
			sortedRegistrations.sort((r1, r2) -> {
				if (r1.getRating() == r2.getRating()) {
					if (r1.getChild().getBirthdate().compareTo(r2.getChild().getBirthdate()) == 0) {
						return r1.getChild().getLastname().compareTo(r2.getChild().getLastname());
					} else {
						return r1.getChild().getBirthdate().compareTo(r2.getChild().getBirthdate());
					}
				} else {
					return r2.getRating() - r1.getRating();
				}
			});
			for (KindergartenRegistration reg : sortedRegistrations) {
				var firstPriorityQueue = reg.getQueues().stream()
						.filter(q -> q.getKindergarten().getName().equals(reg.getFirstPriority())).findFirst()
						.orElse(null);
				var howManyAccepted = firstPriorityQueue.getRegistrations().stream()
						.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg != null).count();
				var freeSpots = firstPriorityQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)
						? firstPriorityQueue.getKindergarten().getSpotsInFirstAgeGroup()
						: firstPriorityQueue.getKindergarten().getSpotsInSecondAgeGroup();
				if (howManyAccepted < freeSpots) {
					reg.setAcceptedKindergarten(firstPriorityQueue.getKindergarten().getName());
					registrationDao.save(reg);
				} else {
					var secondPriorityQueue = reg.getQueues().stream()
							.filter(q -> q.getKindergarten().getName().equals(reg.getSecondPriority())).findFirst()
							.orElse(null);
					if (secondPriorityQueue != null) {
						howManyAccepted = secondPriorityQueue.getRegistrations().stream()
								.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg != null).count();
						freeSpots = secondPriorityQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)
								? secondPriorityQueue.getKindergarten().getSpotsInFirstAgeGroup()
								: secondPriorityQueue.getKindergarten().getSpotsInSecondAgeGroup();
						if (howManyAccepted < freeSpots) {
							reg.setAcceptedKindergarten(secondPriorityQueue.getKindergarten().getName());
							registrationDao.save(reg);
						} else {
							var thirdPriorityQueue = reg.getQueues().stream()
									.filter(q -> q.getKindergarten().getName().equals(reg.getThirdPriority()))
									.findFirst().orElse(null);
							if (thirdPriorityQueue != null) {
								howManyAccepted = thirdPriorityQueue.getRegistrations().stream()
										.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg != null).count();
								freeSpots = thirdPriorityQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)
										? thirdPriorityQueue.getKindergarten().getSpotsInFirstAgeGroup()
										: thirdPriorityQueue.getKindergarten().getSpotsInSecondAgeGroup();
								if (howManyAccepted < freeSpots) {
									reg.setAcceptedKindergarten(thirdPriorityQueue.getKindergarten().getName());
									registrationDao.save(reg);
								} else {
									var fourthPriorityQueue = reg.getQueues().stream()
											.filter(q -> q.getKindergarten().getName().equals(reg.getFourthPriority()))
											.findFirst().orElse(null);
									if (fourthPriorityQueue != null) {
										howManyAccepted = fourthPriorityQueue.getRegistrations().stream()
												.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg != null).count();
										freeSpots = fourthPriorityQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)
												? fourthPriorityQueue.getKindergarten().getSpotsInFirstAgeGroup()
												: fourthPriorityQueue.getKindergarten().getSpotsInSecondAgeGroup();
										if (howManyAccepted < freeSpots) {
											reg.setAcceptedKindergarten(
													fourthPriorityQueue.getKindergarten().getName());
											registrationDao.save(reg);
										} else {
											var fifthPriorityQueue = reg.getQueues().stream().filter(
													q -> q.getKindergarten().getName().equals(reg.getFifthPriority()))
													.findFirst().orElse(null);
											if (fifthPriorityQueue != null) {
												howManyAccepted = fifthPriorityQueue.getRegistrations().stream()
														.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg != null)
														.count();
												freeSpots = fifthPriorityQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)
														? fifthPriorityQueue.getKindergarten().getSpotsInFirstAgeGroup()
														: fifthPriorityQueue.getKindergarten()
																.getSpotsInSecondAgeGroup();
												if (howManyAccepted < freeSpots) {
													reg.setAcceptedKindergarten(
															fifthPriorityQueue.getKindergarten().getName());
													registrationDao.save(reg);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
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

	@Transactional
	public List<RegistrationTableObject> getQueueRegistrations(long admissionId, long queueId) {
		List<KindergartenRegistration> registrations = new ArrayList<>();
		var queue = queueDao.findById(queueId).orElse(null);
		if (queue != null) {
			queue.getRegistrations().forEach(q -> registrations.add(q));
			registrations.sort((r1, r2) -> {
				if (r1.getRating() == r2.getRating()) {
					if (r1.getChild().getBirthdate().compareTo(r2.getChild().getBirthdate()) == 0) {
						return r1.getChild().getLastname().compareTo(r2.getChild().getLastname());
					} else {
						return r1.getChild().getBirthdate().compareTo(r2.getChild().getBirthdate());
					}
				} else {
					return r2.getRating() - r1.getRating();
				}
			});
			List<RegistrationTableObject> orderedRegistrations = new ArrayList<>();
			for (int i = 1; i <= registrations.size(); i++) {
				orderedRegistrations.add(new RegistrationTableObject(registrations.get(i - 1).getId(), i,
						registrations.get(i - 1).getChild().getFirstname(),
						registrations.get(i - 1).getChild().getLastname(), queue.getKindergarten().getName(),
						queue.getAgeGroup().toString(), registrations.get(i - 1).getRating(),
						registrations.get(i - 1).getAcceptedKindergarten() == null ? false
								: registrations.get(i - 1).getAcceptedKindergarten()
										.equals(queue.getKindergarten().getName()) ? true : false));
			}
			return orderedRegistrations;
		}
		return null;
	}

}
