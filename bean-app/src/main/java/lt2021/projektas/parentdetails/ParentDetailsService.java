package lt2021.projektas.parentdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.userRegister.User;
import lt2021.projektas.userRegister.UserDao;

@Service
public class ParentDetailsService {
	
	@Autowired
	private ParentDetailsDao detailsDao;
	
	@Autowired
	private UserDao userDao;
	
	@Transactional
	public ServiceLayerDetails getParentDetails(long id) {
		User parent = userDao.findById(id).orElse(null);
		return new ServiceLayerDetails(parent.getParentDetails().getId(), parent.getParentDetails().getFirstname(), parent.getParentDetails().getLastname(), parent.getParentDetails().getEmail(),
				parent.getParentDetails().getPhone(), parent.getParentDetails().getPersonalCode(), parent.getParentDetails().getLivingAddress(), 
				parent.getParentDetails().getNumberOfKids(), parent.getParentDetails().isStudying(), parent.getParentDetails().getStudyingInstitution(),
				parent.getParentDetails().isHasDisability(), parent.getParentDetails().isDeclaredResidenceSameAsLiving(), parent.getParentDetails().getDeclaredAddress());
	}
	
	@Transactional
	public void addParentDetails(ServiceLayerDetails parentDetails) {
		User parent = userDao.findById(parentDetails.getId()).orElse(null);
		if (!(parent.equals(null))) {
			parent.setParentDetails(new ParentDetails(parentDetails.getFirstname(), parentDetails.getLastname(), parentDetails.getEmail(), parentDetails.getPhone(),
					parentDetails.getPersonalCode(), parentDetails.getLivingAddress(), 
					parentDetails.getNumberOfKids(), parentDetails.isStudying(), parentDetails.getStudyingInstitution(), parentDetails.isHasDisability(), 
					parentDetails.isDeclaredResidenceSameAsLiving(), parentDetails.getDeclaredAddress()));
			userDao.save(parent);
		}
	}
	
	@Transactional
	public void updateParentDetails(ServiceLayerDetails parentDetails, long userId) {
		User parent = userDao.findById(userId).orElse(null);
		if (parent != null) {
			ParentDetails details = parent.getParentDetails();
			if (details != null) {
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
			}
		}
	}
	
}
