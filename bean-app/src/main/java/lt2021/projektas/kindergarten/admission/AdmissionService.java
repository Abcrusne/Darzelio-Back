package lt2021.projektas.kindergarten.admission;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.child.ChildDao;
import lt2021.projektas.kindergarten.queue.AgeGroup;
import lt2021.projektas.kindergarten.queue.QueueService;
import lt2021.projektas.kindergarten.queue.RegistrationTableItem;
import lt2021.projektas.kindergarten.queue.RegistrationTableObject;
import lt2021.projektas.kindergarten.registration.KindergartenRegistration;
import lt2021.projektas.kindergarten.registration.KindergartenRegistrationDao;

@Service
public class AdmissionService {

	@Autowired
	private AdmissionDao admissionDao;

	@Autowired
	private QueueService queueService;

	@Autowired
	private KindergartenRegistrationDao registrationDao;
	
	@Autowired 
	private ChildDao childDao;

	@Transactional
	public RegistrationTableObject getSortedAdmissionRegistrations(int pageNumber, String sortby) {
		if (pageNumber == -1) {
			pageNumber = 1;
		}
		var totalRegs = registrationDao.registrationWithAdmissionCount();
		double pageCount = Math.ceil((double)totalRegs / 15.0);
		var admissionRegistrations = registrationDao.findRegistrationsWithAdmission(PageRequest.of(pageNumber - 1, 15));
		if (sortby.equals("lastname")) {
			admissionRegistrations.sort((r1, r2) -> {
				if (r1.getChild().getLastname().compareTo(r2.getChild().getLastname()) == 0) {
					if (r1.getRating() == r2.getRating()) {
						return r1.getChild().getBirthdate().compareTo(r2.getChild().getBirthdate());
					} else {
						return r2.getRating() - r1.getRating();
					}
				} else {
					return r1.getChild().getLastname().compareTo(r2.getChild().getLastname());
				}
			});
		} else if (sortby.equals("accepted")) {
			admissionRegistrations.sort((r1, r2) -> {
				if (r1.getAcceptedKindergarten() != null && r2.getAcceptedKindergarten() != null) {
					if (r1.getRating() == r2.getRating()) {
						if (r1.getChild().getBirthdate().compareTo(r2.getChild().getBirthdate()) == 0) {
							return r1.getChild().getLastname().compareTo(r2.getChild().getLastname());
						} else {
							return r1.getChild().getBirthdate().compareTo(r2.getChild().getBirthdate());
						}
					} else {
						return r2.getRating() - r1.getRating();
					}
				} else {
					if (r1.getAcceptedKindergarten() == null) {
						return 1;
					} else {
						return -1;
					}
				}
			});
		} else {
			admissionRegistrations.sort((r1, r2) -> {
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
		}
		var registrations = admissionRegistrations.stream()
				.map(reg -> new RegistrationTableItem(reg.getChild().getId(), reg.getChild().getFirstname(),
						reg.getChild().getLastname(),
						reg.getAcceptedKindergarten() == null ? "" : reg.getAcceptedKindergarten(),
						reg.getChild().getPersonalCode(), reg.getRating(),
						reg.getAcceptedKindergarten() == null ? false : true))
				.collect(Collectors.toList());
		return new RegistrationTableObject(pageNumber, pageCount, registrations);
	}

	@Transactional
	public List<KindergartenRegistration> sortAdmissionRegistrations() {
		var admission = admissionDao.findAll().get(0);
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
		return sortedRegistrations;
	}

	@Transactional
	public void confirmRegistrations() {
		var sortedRegistrations = sortAdmissionRegistrations();
		for (KindergartenRegistration reg : sortedRegistrations) {
			var firstPriorityQueue = reg.getQueues().stream()
					.filter(q -> q.getKindergarten().getName().equals(reg.getFirstPriority())).findFirst().orElse(null);
			var howManyAccepted = firstPriorityQueue.getRegistrations().stream().map(r -> r.getAcceptedKindergarten())
					.filter(kg -> kg != null).count();
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
								.filter(q -> q.getKindergarten().getName().equals(reg.getThirdPriority())).findFirst()
								.orElse(null);
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
										reg.setAcceptedKindergarten(fourthPriorityQueue.getKindergarten().getName());
										registrationDao.save(reg);
									} else {
										var fifthPriorityQueue = reg.getQueues().stream().filter(
												q -> q.getKindergarten().getName().equals(reg.getFifthPriority()))
												.findFirst().orElse(null);
										freeSpots = fifthPriorityQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)
												? fifthPriorityQueue.getKindergarten().getSpotsInFirstAgeGroup()
												: fifthPriorityQueue.getKindergarten().getSpotsInSecondAgeGroup();
										if (fifthPriorityQueue != null) {
											howManyAccepted = fifthPriorityQueue.getRegistrations().stream()
													.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg != null)
													.count();
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
	
	public void getChildDetailsFromRegistrationList(long childId) {
		var child = childDao.findById(childId).orElse(null);
		if (child != null) {
			var mainParent = child.getParents().stream().filter(parent -> parent.getParent() != null).findFirst().orElse(null);
			if (mainParent != null) {
				
			}
		}
	}
	
	@Transactional
	public void lockAdmission() {
		var admission = admissionDao.findAll().get(0);
		admission.setActive(false);
		admission.setLastUpdatedAt(new Date());
		admissionDao.save(admission);
	}
	
	@Transactional
	public void unlockAdmission() {
		var admission = admissionDao.findAll().get(0);
		admission.setActive(true);
		admission.setLastUpdatedAt(new Date());
		admissionDao.save(admission);
		var registrations = registrationDao.findRegistrationsWithoutAdmission();
		registrations.forEach(reg -> {
			queueService.addRegistrationToQueues(reg);
		});
	}

	
}
