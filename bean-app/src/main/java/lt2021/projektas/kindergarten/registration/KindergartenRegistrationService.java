package lt2021.projektas.kindergarten.registration;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.child.Child;
import lt2021.projektas.child.ChildDao;

@Service
public class KindergartenRegistrationService {
	
	@Autowired
	private KindergartenRegistrationDao kgRegDao;
	
	@Autowired
	private ChildDao childDao;
	
	
	@Transactional
	public ResponseEntity<String> addRegistration(CreateRegistrationCommand registrationForm) {
		var child = childDao.findById(registrationForm.getChildId()).orElse(null);
		if (child != null) {
			if (child.getRegistrationForms().stream().allMatch(reg -> reg.getAdmission() != null)) {
				var registration = new KindergartenRegistration(child, registrationForm.getFirstPriority(), registrationForm.getSecondPriority(), 
						registrationForm.getThirdPriority(), registrationForm.getFourthPriority(), registrationForm.getFifthPriority());
				if (child.getLivingAddress().getCity().toLowerCase().equals("vilnius")) {
					registration.setRating(registration.getRating() + 5);
				}
				if (child.isAdopted()) {
					registration.setRating(registration.getRating() + 1);
				}
				if (!(child.getParents().stream().filter(parent -> parent.isStudying()).findFirst().isEmpty())) {
					registration.setRating(registration.getRating() + 1);
				}
				if (!(child.getParents().stream().filter(parent -> parent.getNumberOfKids() >= 3).findFirst().isEmpty())) {
					registration.setRating(registration.getRating() + 1);
				}
				if (!(child.getParents().stream().filter(parent -> parent.isHasDisability()).findFirst().isEmpty())) {
					registration.setRating(registration.getRating() + 1);
				}
				var registrations = child.getRegistrationForms();
				registrations.add(registration);
				child.setRegistrationForms(registrations);
				kgRegDao.save(registration);
				return new ResponseEntity<String>("Vaiko registracija išsaugota", HttpStatus.OK);
			}
			return new ResponseEntity<String>("Šio vaiko registracija jau užpildyta!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Vaikas nerastas sistemoje", HttpStatus.BAD_REQUEST);
	}
	
	@Transactional
	public void updateRegistration(Child updatedChild) {
		var childRegistrations = kgRegDao.findByChild(updatedChild);
		if (childRegistrations != null) {
			var childRegistration = childRegistrations.stream().filter(reg -> reg.getAdmission() == null).findFirst().orElse(null);
			if (childRegistration != null) {
				childRegistration.setRating(0);
				if (updatedChild.getLivingAddress().getCity().toLowerCase().equals("vilnius")) {
					childRegistration.setRating(childRegistration.getRating() + 5);
				}
				if (updatedChild.isAdopted()) {
					childRegistration.setRating(childRegistration.getRating() + 1);
				}
				if (!(updatedChild.getParents().stream().filter(parent -> parent.isStudying()).findFirst().isEmpty())) {
					childRegistration.setRating(childRegistration.getRating() + 1);
				}
				if (!(updatedChild.getParents().stream().filter(parent -> parent.getNumberOfKids() >= 3).findFirst().isEmpty())) {
					childRegistration.setRating(childRegistration.getRating() + 1);
				}
				if (!(updatedChild.getParents().stream().filter(parent -> parent.isHasDisability()).findFirst().isEmpty())) {
					childRegistration.setRating(childRegistration.getRating() + 1);
				}
				kgRegDao.save(childRegistration);
			}
		}
	}
	
	@Transactional
	public List<CreateRegistrationCommand> getAllRegistrations() {
		return kgRegDao.findAll().stream()
					.map(reg -> new CreateRegistrationCommand(reg.getId(), reg.getChild().getId(), reg.getFirstPriority(), 
							reg.getSecondPriority(), reg.getThirdPriority(), reg.getFourthPriority(), reg.getFifthPriority(), reg.getRating()))
					.collect(Collectors.toList());
	}
	
	@Transactional
	public List<CreateRegistrationCommand> getRegistrationsWithSpecifiedKindergarten(String kindergartenName) {
		return kgRegDao.findRegistrationsWithSpecifiedKindergarten(kindergartenName).stream()
					.map(kgReg -> new CreateRegistrationCommand(kgReg.getId(), kgReg.getChild().getId(), kgReg.getFirstPriority(), kgReg.getSecondPriority(),
							kgReg.getThirdPriority(), kgReg.getFourthPriority(), kgReg.getFifthPriority(), kgReg.getRating()))
					.collect(Collectors.toList());
	}
	
}
