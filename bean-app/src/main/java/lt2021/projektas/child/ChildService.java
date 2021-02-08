package lt2021.projektas.child;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.parentdetails.ParentDetails;
import lt2021.projektas.parentdetails.ParentDetailsDao;
import lt2021.projektas.parentdetails.ServiceLayerDetails;
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
		if (!(mainParent == null)) {
			Set<Child> childrenSet = mainParent.getParentDetails().getChildren();
			Child newChild = new Child(child.getFirstname(), child.getLastname(), child.getPersonalCode(),
					child.isAdopted(), child.getBirthdate(), child.getLivingAddress());
			Set<ParentDetails> parentSet = newChild.getParents();
			parentSet.add(mainParent.getParentDetails());
			newChild.setParents(parentSet);
			childrenSet.add(newChild);
			mainParent.getParentDetails().setChildren(childrenSet);
			if (!(child.getSecondParentDetails() == null)) {
				if (!(detailsDao.findByPersonalCode(child.getSecondParentDetails().getPersonalCode()).isPresent())) {
					ParentDetails secondParent = new ParentDetails(child.getSecondParentDetails().getFirstname(),
							child.getSecondParentDetails().getLastname(), child.getSecondParentDetails().getEmail(),
							child.getSecondParentDetails().getPhone(), child.getSecondParentDetails().getPersonalCode(),
							child.getSecondParentDetails().getLivingAddress(),
							child.getSecondParentDetails().getNumberOfKids(),
							child.getSecondParentDetails().isStudying(),
							child.getSecondParentDetails().getStudyingInstitution(),
							child.getSecondParentDetails().isHasDisability(),
							child.getSecondParentDetails().isDeclaredResidenceSameAsLiving(),
							child.getSecondParentDetails().getDeclaredAddress());
					childrenSet = secondParent.getChildren();
					childrenSet.add(newChild);
					secondParent.setChildren(childrenSet);
					parentSet.add(secondParent);
					newChild.setParents(parentSet);
					detailsDao.save(secondParent);
				} else {
					ParentDetails secondParent = detailsDao
							.findByPersonalCode(child.getSecondParentDetails().getPersonalCode()).get();
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

	@Transactional
	public List<ServiceLayerChild> getChildren(long parentId) {
		User parent = userDao.findById(parentId).orElse(null);
		if (!(parent == null)) {
			return parent.getParentDetails().getChildren().stream()
					.map(child -> new ServiceLayerChild(child.getId(), child.getFirstname(), child.getLastname(),
							child.getPersonalCode(), child.isAdopted(), child.getBirthdate(), child.getLivingAddress()))
					.collect(Collectors.toList());
		} else {
			return null;
		}
	}

	@Transactional
	public CreateChildCommand getChildDetails(long parentId, long childId) {
		User currentParent = userDao.findById(parentId).orElse(null);
		if (!(currentParent == null)) {
			Child currentChild = currentParent.getParentDetails().getChildren().stream()
					.filter(child -> child.getId().equals(childId)).findFirst().orElse(null);
			if (!(currentChild == null)) {
				ParentDetails secondParent = currentChild.getParents().stream()
						.filter(parent -> !(parent.getId().equals(parentId))).findFirst().orElse(null);
				if (!(secondParent == null)) {
					return new CreateChildCommand(currentChild.getId(), currentChild.getFirstname(),
							currentChild.getLastname(), currentChild.getPersonalCode(), currentChild.isAdopted(),
							currentChild.getBirthdate(), currentChild.getLivingAddress().getCity(),
							currentChild.getLivingAddress().getStreet(), currentChild.getLivingAddress().getHouseNumber(),
							currentChild.getLivingAddress().getFlatNumber(), true, secondParent.getFirstname(),
							secondParent.getLastname(), secondParent.getEmail(), secondParent.getPhone(),
							secondParent.getPersonalCode(), secondParent.getLivingAddress().getCity(),
							secondParent.getLivingAddress().getStreet(), secondParent.getLivingAddress().getHouseNumber(),
							secondParent.getLivingAddress().getFlatNumber(), secondParent.getNumberOfKids(),
							secondParent.isStudying(), secondParent.getStudyingInstitution(),
							secondParent.isHasDisability(), secondParent.isDeclaredResidenceSameAsLiving(),
							secondParent.getDeclaredAddress().getCity(), secondParent.getDeclaredAddress().getStreet(),
							secondParent.getDeclaredAddress().getHouseNumber(),
							secondParent.getDeclaredAddress().getFlatNumber());
				} else {
					return new CreateChildCommand(currentChild.getId(), currentChild.getFirstname(),
							currentChild.getLastname(), currentChild.getPersonalCode(), currentChild.isAdopted(),
							currentChild.getBirthdate(), currentChild.getLivingAddress().getCity(),
							currentChild.getLivingAddress().getStreet(), currentChild.getLivingAddress().getHouseNumber(),
							currentChild.getLivingAddress().getFlatNumber(), false, "", "", "", "", 0L, "", "", "", "", 0,
							false, "", false, false, "", "", "", "");
				}
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

}
