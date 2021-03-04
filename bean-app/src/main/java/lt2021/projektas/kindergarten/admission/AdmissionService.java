package lt2021.projektas.kindergarten.admission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.child.ChildDao;
import lt2021.projektas.kindergarten.Kindergarten;
import lt2021.projektas.kindergarten.KindergartenDao;
import lt2021.projektas.kindergarten.queue.AgeGroup;
import lt2021.projektas.kindergarten.queue.RegistrationTableItem;
import lt2021.projektas.kindergarten.queue.RegistrationTableObject;
import lt2021.projektas.kindergarten.registration.KindergartenRegistration;
import lt2021.projektas.kindergarten.registration.KindergartenRegistrationDao;

@Service
public class AdmissionService {

	@Autowired
	private AdmissionDao admissionDao;

	@Autowired
	private KindergartenDao kindergartenDao;

	@Autowired
	private KindergartenRegistrationDao registrationDao;

	@Autowired
	private ChildDao childDao;

	private JavaMailSender emailSender;

	@Autowired
	public AdmissionService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	@Transactional
	public RegistrationTableObject getSortedAdmissionRegistrations(int pageNumber, String sortby, String lastname) {
		if (pageNumber == -1) {
			pageNumber = 1;
		}
		List<KindergartenRegistration> admissionRegistrations = new ArrayList<>();
		int pageCount = 1;
		var totalRegs = 0L;
		if (lastname.length() == 0) {
			if (sortby.equals("lastname")) {
				admissionRegistrations = registrationDao.findRegistrationsWithAdmission(PageRequest.of(pageNumber - 1, 15, 
						Sort.by(Sort.Order.asc("child.lastname"), Sort.Order.desc("rating"), Sort.Order.desc("child.birthdate"))));
			} else if (sortby.equals("lastnamedesc")) {
				admissionRegistrations = registrationDao.findRegistrationsWithAdmission(PageRequest.of(pageNumber - 1, 15, 
						Sort.by(Sort.Order.desc("child.lastname"), Sort.Order.desc("rating"), Sort.Order.desc("child.birthdate"))));
			} else if (sortby.equals("accepted")) {
				admissionRegistrations = registrationDao.findRegistrationsWithAdmission(PageRequest.of(pageNumber - 1, 15, 
						Sort.by(Sort.Order.desc("acceptedKindergarten"), Sort.Order.desc("rating"), Sort.Order.desc("child.birthdate"), Sort.Order.asc("child.lastname"))));
			} else {
				admissionRegistrations = registrationDao.findRegistrationsWithAdmission(PageRequest.of(pageNumber - 1, 15, 
						Sort.by(Sort.Order.desc("rating"), Sort.Order.desc("child.birthdate"), Sort.Order.asc("child.lastname"))));
			}
			
			totalRegs = registrationDao.registrationWithAdmissionCount();
			pageCount = (int) Math.ceil((double) totalRegs / 15.0);
		} else {
			admissionRegistrations = registrationDao.findRegistrationByChildLastname(lastname,
					PageRequest.of(pageNumber - 1, 15, 
							Sort.by(Sort.Order.desc("rating"), Sort.Order.desc("child.birthdate"), Sort.Order.asc("child.lastname"))));
			totalRegs = admissionRegistrations.size();
			pageCount = (int) Math.ceil((double) totalRegs / 15.0);
		}
		/*
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
		} else if (sortby.equals("lastnamedesc")) {
			admissionRegistrations.sort((r1, r2) -> {
				if (r1.getChild().getLastname().compareTo(r2.getChild().getLastname()) == 0) {
					if (r1.getRating() == r2.getRating()) {
						return r1.getChild().getBirthdate().compareTo(r2.getChild().getBirthdate());
					} else {
						return r2.getRating() - r1.getRating();
					}
				} else {
					return r2.getChild().getLastname().compareTo(r1.getChild().getLastname());
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
				} else if (r1.getAcceptedKindergarten() == null && r2.getAcceptedKindergarten() == null) {
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
		*/
		var registrations = admissionRegistrations.stream()
				.map(reg -> new RegistrationTableItem(reg.getChild().getId(), reg.getChild().getFirstname(),
						reg.getChild().getLastname(),
						reg.getAcceptedKindergarten() == null ? "" : reg.getAcceptedKindergarten(),
						reg.getChild().getPersonalCode(), reg.getRating(),
						reg.getAcceptedKindergarten() == null ? false : true))
				.collect(Collectors.toList());
		return new RegistrationTableObject(pageNumber, pageCount, totalRegs, registrations);
	}

	@Transactional
	public List<KindergartenRegistration> sortAdmissionRegistrations() {
		var admission = admissionDao.findAll().get(0);
		var admissionRegistrations = admission.getRegistrations();
		var sortedRegistrations = admissionRegistrations.stream().filter(reg -> reg.getAcceptedKindergarten() == null).collect(Collectors.toList());
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
			if (firstPriorityQueue != null) {
				var howManyAccepted = firstPriorityQueue.getRegistrations().stream()
						.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg == null ? false : kg.equals(reg.getFirstPriority())).count();
				var freeSpots = firstPriorityQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)
						? firstPriorityQueue.getKindergarten().getSpotsInFirstAgeGroup()
						: firstPriorityQueue.getKindergarten().getSpotsInSecondAgeGroup();
				if (howManyAccepted < freeSpots) {
					reg.setAcceptedKindergarten(firstPriorityQueue.getKindergarten().getName());
					registrationDao.save(reg);
					/*
					 * var mainParent = reg.getChild().getParents().stream().filter(parent ->
					 * parent.getParent() != null).findFirst().orElse(null); SimpleMailMessage
					 * message = new SimpleMailMessage();
					 * message.setFrom("bean.vaidar.mailinformer@gmail.com");
					 * message.setTo(mainParent.getEmail());
					 * message.setSubject("Vaiko registracija į darželį");
					 * message.setText("Sveiki, " + mainParent.getFirstname() + " " +
					 * mainParent.getLastname() + ", \n" + "Informuojame, jog jūsų vaikas: " +
					 * reg.getChild().getFirstname() + " " + reg.getChild().getLastname() +
					 * ", yra priimtas į darželį: " + reg.getAcceptedKindergarten() + ".\n" +
					 * "Artimiausiu metu su jumis susisieks darželio administracija dėl dokumentų pasirašymo.");
					 * emailSender.send(message);
					 */
				} else {
					var secondPriorityQueue = reg.getQueues().stream()
							.filter(q -> q.getKindergarten().getName().equals(reg.getSecondPriority())).findFirst()
							.orElse(null);
					if (secondPriorityQueue != null) {
						howManyAccepted = secondPriorityQueue.getRegistrations().stream()
								.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg == null ? false : kg.equals(reg.getSecondPriority())).count();
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
										.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg == null ? false : kg.equals(reg.getThirdPriority())).count();
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
												.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg == null ? false : kg.equals(reg.getFourthPriority())).count();
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
											freeSpots = fifthPriorityQueue.getAgeGroup().equals(AgeGroup.PRESCHOOL)
													? fifthPriorityQueue.getKindergarten().getSpotsInFirstAgeGroup()
													: fifthPriorityQueue.getKindergarten().getSpotsInSecondAgeGroup();
											if (fifthPriorityQueue != null) {
												howManyAccepted = fifthPriorityQueue.getRegistrations().stream()
														.map(r -> r.getAcceptedKindergarten()).filter(kg -> kg == null ? false : kg.equals(reg.getFifthPriority()))
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
	}

	public ChildAndParentDetailsObject getChildDetailsFromRegistrationList(long childId) {
		var child = childDao.findById(childId).orElse(null);
		if (child != null) {
			var mainParent = child.getParents().stream().filter(parent -> parent.getParent() != null).findFirst()
					.orElse(null);
			if (mainParent != null) {
				var secondParent = child.getParents().stream().filter(parent -> parent.getParent() == null).findFirst()
						.orElse(null);
				if (secondParent != null) {
					return new ChildAndParentDetailsObject(childId, mainParent.getFirstname(), mainParent.getLastname(),
							mainParent.getEmail(), mainParent.getPhone(), mainParent.getLivingAddress().toString(),
							mainParent.getNumberOfKids(), mainParent.isStudying(), mainParent.getStudyingInstitution(),
							mainParent.isHasDisability(), child.getFirstname(), child.getLastname(), new SimpleDateFormat("yyyy-MM-dd").format(child.getBirthdate()),
							child.getLivingAddress().toString(), child.isAdopted(), child.getRegistrationForm().getRating(),
							child.getRegistrationForm().getAcceptedKindergarten() == null ? "" : child.getRegistrationForm().getAcceptedKindergarten(),
							true, secondParent.getFirstname(), secondParent.getLastname(), secondParent.getNumberOfKids(), secondParent.isStudying(),
							secondParent.getStudyingInstitution(), secondParent.isHasDisability());
				} else {
					return new ChildAndParentDetailsObject(childId, mainParent.getFirstname(), mainParent.getLastname(),
							mainParent.getEmail(), mainParent.getPhone(), mainParent.getLivingAddress().toString(),
							mainParent.getNumberOfKids(), mainParent.isStudying(), mainParent.getStudyingInstitution(),
							mainParent.isHasDisability(), child.getFirstname(), child.getLastname(), new SimpleDateFormat("yyyy-MM-dd").format(child.getBirthdate()),
							child.getLivingAddress().toString(), child.isAdopted(), child.getRegistrationForm().getRating(), 
							child.getRegistrationForm().getAcceptedKindergarten() == null ? "" : child.getRegistrationForm().getAcceptedKindergarten(), false, "", "", 0, false, "", false);
				}
			}
		}
		return null;
	}

	@Transactional
	public AdmissionStatusObject activateAdmission() {
		var admission = admissionDao.findAll().get(0);
		admission.setActive(true);
		admission.setLastUpdatedAt(new Date());
		admissionDao.save(admission);
		return admissionStatus();
	}

	@Transactional
	public AdmissionStatusObject deactivateAdmission() {
		var admission = admissionDao.findAll().get(0);
		admission.setActive(false);
		admission.setLastUpdatedAt(new Date());
		admissionDao.save(admission);
		return admissionStatus();
	}

	@Transactional
	public AdmissionStatusObject admissionStatus() {
		var admission = admissionDao.findAll().get(0);
		var kindergartens = kindergartenDao.findAll();
		int firstAgeGroupCount = 0;
		int secondAgeGroupCount = 0;
		int spotsInFirstAgeGroup = 0;
		int spotsInSecondAgeGroup = 0;
		Date today = new Date();
		for (KindergartenRegistration reg : admission.getRegistrations()) {
			var timeDiff = today.getTime() - reg.getChild().getBirthdate().getTime();
			var timeDiffDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
			var yearDiff = timeDiffDays / 365;
			if (yearDiff >= 2 && yearDiff < 3) {
				firstAgeGroupCount++;
			} else if (yearDiff >= 3 && yearDiff <= 6) {
				secondAgeGroupCount++;
			}
		}
		for (Kindergarten kg : kindergartens) {
			spotsInFirstAgeGroup += kg.getSpotsInFirstAgeGroup();
			spotsInSecondAgeGroup += kg.getSpotsInSecondAgeGroup();
		}
		return new AdmissionStatusObject(firstAgeGroupCount, secondAgeGroupCount, spotsInFirstAgeGroup,
				spotsInSecondAgeGroup, admission.isActive(), admission.isAdminLock());
	}

	@Transactional
	public AdmissionStatusObject lockAdmission() {
		var admission = admissionDao.findAll().get(0);
		admission.setAdminLock(true);
		admission.setLastUpdatedAt(new Date());
		admissionDao.save(admission);
		return admissionStatus();
	}

	@Transactional
	public AdmissionStatusObject unlockAdmission() {
		var admission = admissionDao.findAll().get(0);
		admission.setAdminLock(false);
		admission.setLastUpdatedAt(new Date());
		admissionDao.save(admission);
		return admissionStatus();
	}

	@Transactional
	public boolean areAdmissionsLocked() {
		var admission = admissionDao.findAll().get(0);
		return admission.isAdminLock();
	}

}
