package lt2021.projektas.kindergarten.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.child.ChildDao;
import lt2021.projektas.parentdetails.ParentDetailsDao;

@Service
public class KindergartenRegistrationService {
	
	@Autowired
	private KindergartenRegistrationDao kgRegDao;
	
	@Autowired
	private ChildDao childDao;
	
	@Autowired
	private ParentDetailsDao detailsDao;
	
	
	@Transactional
	public void addRegistration(CreateRegistrationCommand registrationForm) {
		var child = childDao.findById(registrationForm.getChildId()).orElse(null);
		if (child != null) {
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
			if (!(child.getParents().stream().filter(parent -> parent.isStudying()).findFirst().isEmpty()) && 
					!(child.getParents().stream().filter(parent -> parent.getNumberOfKids() >= 3).findFirst().isEmpty())) {
				registration.setRating(registration.getRating() + 1);
			}
			if (!(child.getParents().stream().filter(parent -> parent.isHasDisability()).findFirst().isEmpty())) {
				registration.setRating(registration.getRating() + 1);
			}
			kgRegDao.save(registration);
		}
	}
	
}
