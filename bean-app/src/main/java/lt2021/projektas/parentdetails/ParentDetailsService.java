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
		ParentDetails parent = detailsDao.findById(id).orElse(null);
		return new ServiceLayerDetails(id, parent.getFirstname(), parent.getLastname(), parent.getEmail(), parent.getPhone(), parent.getPersonalCode(), parent.getLivingAddress(), 
				parent.getNumberOfKids(), parent.isStudying(), parent.getStudyingInstitution(), parent.isHasDisability(), 
				parent.isDeclaredResidenceSameAsLiving(), parent.getDeclaredAddress());
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
	
}
