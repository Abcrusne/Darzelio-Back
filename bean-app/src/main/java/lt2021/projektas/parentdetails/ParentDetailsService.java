package lt2021.projektas.parentdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.child.ChildDao;
import lt2021.projektas.kindergarten.registration.KindergartenRegistrationService;
import lt2021.projektas.userRegister.User;
import lt2021.projektas.userRegister.UserDao;

@Service
public class ParentDetailsService {
	
	@Autowired
	private ParentDetailsDao detailsDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired 
	private ChildDao childDao;
	
	@Autowired
	private KindergartenRegistrationService kgRegService;
	
	@Transactional
	public ServiceLayerDetails getParentDetails(long id) {
		User parent = userDao.findById(id).orElse(null);
		return new ServiceLayerDetails(parent.getParentDetails().getId(), parent.getParentDetails().getFirstname(), parent.getParentDetails().getLastname(), parent.getParentDetails().getEmail(),
				parent.getParentDetails().getPhone(), parent.getParentDetails().getPersonalCode(), parent.getParentDetails().getLivingAddress(), 
				parent.getParentDetails().getNumberOfKids(), parent.getParentDetails().isStudying(), parent.getParentDetails().getStudyingInstitution(),
				parent.getParentDetails().isHasDisability(), parent.getParentDetails().isDeclaredResidenceSameAsLiving(), parent.getParentDetails().getDeclaredAddress());
	}
	
	@Transactional
	public ResponseEntity<String> addParentDetails(ServiceLayerDetails parentDetails) {
		User parent = userDao.findById(parentDetails.getId()).orElse(null);
		if (parent != null) {
			if (childDao.findByPersonalCode(parentDetails.getPersonalCode()).isPresent()) {
				return new ResponseEntity<String>("Asmens kodas jau užimtas", HttpStatus.BAD_REQUEST);
			}
			parent.setParentDetails(new ParentDetails(parentDetails.getFirstname(), parentDetails.getLastname(), parentDetails.getEmail(), parentDetails.getPhone(),
					parentDetails.getPersonalCode(), parentDetails.getLivingAddress(), 
					parentDetails.getNumberOfKids(), parentDetails.isStudying(), parentDetails.getStudyingInstitution(), parentDetails.isHasDisability(), 
					parentDetails.isDeclaredResidenceSameAsLiving(), parentDetails.getDeclaredAddress()));
			userDao.save(parent);
			return new ResponseEntity<String>("Tėvo/globėjo duomenys išsaugoti", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Tėvas/globėjas neregistruotas sistemoje", HttpStatus.BAD_REQUEST);
	}
	
	@Transactional
	public ResponseEntity<String> updateParentDetails(ServiceLayerDetails parentDetails, long userId) {
		User parent = userDao.findById(userId).orElse(null);
		if (parent != null) {
			ParentDetails details = parent.getParentDetails();
			if (details != null) {
				if (childDao.findByPersonalCode(parentDetails.getPersonalCode()).isPresent()) {
					return new ResponseEntity<String>("Toks asmens kodas jau užimtas", HttpStatus.BAD_REQUEST);
				}
				details.setFirstname(parentDetails.getFirstname());
				details.setLastname(parentDetails.getLastname());
				details.setEmail(parentDetails.getEmail());
				details.setPhone(parentDetails.getPhone());
				details.setPersonalCode(parentDetails.getPersonalCode());
				details.setLivingAddress(parentDetails.getLivingAddress());
				details.setNumberOfKids(parentDetails.getNumberOfKids());
				details.setStudying(parentDetails.isStudying());
				details.setStudyingInstitution(parentDetails.getStudyingInstitution());
				details.setHasDisability(parentDetails.isHasDisability());
				details.setDeclaredResidenceSameAsLiving(parentDetails.isDeclaredResidenceSameAsLiving());
				details.setDeclaredAddress(parentDetails.getDeclaredAddress());
				detailsDao.save(details);
				if (details.getChildren().stream().filter(child -> child.getRegistrationForm() != null).findAny().orElse(null) != null) {
					details.getChildren().stream().forEach(child -> kgRegService.updateRegistration(child));
				}
				return new ResponseEntity<String>("Duomenys sekmingai pakeisti", HttpStatus.OK);
			}
			return new ResponseEntity<String>("Tėvo/globėjo duomenys neužpildyti", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Tėvas/globėjas neregistruotas sistemoje", HttpStatus.BAD_REQUEST);
	}
	
}
