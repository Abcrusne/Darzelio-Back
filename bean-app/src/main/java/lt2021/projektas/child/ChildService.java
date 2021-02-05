package lt2021.projektas.child;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.parentdetails.ParentDetails;
import lt2021.projektas.parentdetails.ParentDetailsDao;
import lt2021.projektas.userRegister.User;
import lt2021.projektas.userRegister.UserDao;

@Service
public class ChildService {

	@Autowired
	private ChildDao childDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	ParentDetailsDao detailsDao;

	@Transactional
	public void addChild(Long parentId, ServiceLayerChild child) {
		User mainParent = userDao.findById(parentId).orElse(null);
		if (!(mainParent.equals(null))) {
			Set<Child> childrenSet = mainParent.getParentDetails().getChildren();
			Child newChild = new Child(child.getFirstname(), child.getLastname(), child.getPersonalCode(), child.isAdopted(), 
					child.getBirthdate(), child.getLivingAddress());
			Set<ParentDetails> parentSet = newChild.getParents();
			parentSet.add(mainParent.getParentDetails());
			newChild.setParents(parentSet);
			childrenSet.add(newChild);
			mainParent.getParentDetails().setChildren(childrenSet);
			if (!(child.getSecondParentDetails() == null)) {
				if (!(detailsDao.findByPersonalCode(child.getSecondParentDetails().getPersonalCode()).isPresent())) {
					ParentDetails secondParent = new ParentDetails(child.getSecondParentDetails().getFirstname(), child.getSecondParentDetails().getLastname(), 
							child.getSecondParentDetails().getEmail(), child.getSecondParentDetails().getPhone(), child.getSecondParentDetails().getPersonalCode(), 
							child.getSecondParentDetails().getLivingAddress(), child.getSecondParentDetails().getNumberOfKids(), child.getSecondParentDetails().isStudying(), 
							child.getSecondParentDetails().getStudyingInstitution(), child.getSecondParentDetails().isHasDisability(), child.getSecondParentDetails().isDeclaredResidenceSameAsLiving(), 
							child.getSecondParentDetails().getDeclaredAddress());
					childrenSet = secondParent.getChildren();
					childrenSet.add(newChild);
					secondParent.setChildren(childrenSet);
					parentSet.add(secondParent);
					newChild.setParents(parentSet);
					detailsDao.save(secondParent);
				} else {
					ParentDetails secondParent = detailsDao.findByPersonalCode(child.getSecondParentDetails().getPersonalCode()).get();
					childrenSet = secondParent.getChildren();
					childrenSet.add(newChild);
					secondParent.setChildren(childrenSet);
					parentSet.add(secondParent);
					newChild.setParents(parentSet);
					detailsDao.save(secondParent);
				}
			}
			
			userDao.save(mainParent);
		}
	}

}
