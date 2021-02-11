package lt2021.projektas.child;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> addChild(Long parentId, ServiceLayerChild child) throws ParseException {
		User mainParent = userDao.findById(parentId).orElse(null);
		if (new SimpleDateFormat("y-MM-dd").parse(child.getBirthdate()).after(new Date())) {
			return new ResponseEntity<>("The birthdate can't be from the future", HttpStatus.BAD_REQUEST);
		}
		if (detailsDao.findByPersonalCode(child.getPersonalCode()).isPresent()) {
			return new ResponseEntity<>("This personal code already exists", HttpStatus.BAD_REQUEST);
		} else if (child.getSecondParentDetails() != null) {
			if (child.getSecondParentDetails().getPersonalCode() == child.getPersonalCode() || 
					childDao.findByPersonalCode(child.getSecondParentDetails().getPersonalCode()).isPresent()) {
				return new ResponseEntity<>("This personal code already exists", HttpStatus.BAD_REQUEST);
			}
		}
		if (mainParent != null) {
			Set<Child> childrenSet = mainParent.getParentDetails().getChildren();
				Child newChild = new Child(child.getFirstname(), child.getLastname(), child.getPersonalCode(),
						child.isAdopted(), new SimpleDateFormat("yyyy-MM-dd").parse(child.getBirthdate()), child.getLivingAddress());
			Set<ParentDetails> parentSet = newChild.getParents();
			parentSet.add(mainParent.getParentDetails());
			newChild.setParents(parentSet);
			childrenSet.add(newChild);
			mainParent.getParentDetails().setChildren(childrenSet);
			if (child.getSecondParentDetails() != null) {
				if (!(detailsDao.findByPersonalCode(child.getSecondParentDetails().getPersonalCode()).isPresent())) {
					if (child.getSecondParentDetails().isDeclaredResidenceSameAsLiving()) {
						ParentDetails secondParent = new ParentDetails(child.getSecondParentDetails().getFirstname(),
								child.getSecondParentDetails().getLastname(), child.getSecondParentDetails().getEmail(),
								child.getSecondParentDetails().getPhone(), child.getSecondParentDetails().getPersonalCode(),
								child.getSecondParentDetails().getLivingAddress(),
								child.getSecondParentDetails().getNumberOfKids(),
								child.getSecondParentDetails().isStudying(),
								child.getSecondParentDetails().getStudyingInstitution(),
								child.getSecondParentDetails().isHasDisability(),
								child.getSecondParentDetails().isDeclaredResidenceSameAsLiving(),
								child.getSecondParentDetails().getLivingAddress());
						childrenSet = secondParent.getChildren();
						childrenSet.add(newChild);
						secondParent.setChildren(childrenSet);
						parentSet.add(secondParent);
						newChild.setParents(parentSet);
						detailsDao.save(secondParent);
					} else {
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
					}
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
			return new ResponseEntity<>("Child created", HttpStatus.OK);
		}
		return new ResponseEntity<>("Bad information passed", HttpStatus.BAD_REQUEST);
	}

	@Transactional
	public List<ServiceLayerChild> getChildren(long parentId) throws ParseException {
		User parent = userDao.findById(parentId).orElse(null);
		if (parent != null) {
			Set<Child> children = parent.getParentDetails().getChildren();
			List<ServiceLayerChild> childArray = new ArrayList<>();
			for (Child ch : children) {
				ServiceLayerChild child = new ServiceLayerChild(ch.getId(), ch.getFirstname(), ch.getLastname(),
						ch.getPersonalCode(), ch.isAdopted(), new SimpleDateFormat("yyyy-MM-dd").format(ch.getBirthdate()), ch.getLivingAddress());
				ParentDetails secondParent = ch.getParents().stream()
						.filter(p -> !(p.getId().equals(parent.getParentDetails().getId()))).findFirst().orElse(null);
				if (secondParent != null) {
					child.setSecondParentDetails(new ServiceLayerDetails(secondParent.getId(),
							secondParent.getFirstname(), secondParent.getLastname(), secondParent.getEmail(),
							secondParent.getPhone(), secondParent.getPersonalCode(), secondParent.getLivingAddress(),
							secondParent.getNumberOfKids(), secondParent.isStudying(),
							secondParent.getStudyingInstitution(), secondParent.isHasDisability(),
							secondParent.isDeclaredResidenceSameAsLiving(), secondParent.getDeclaredAddress()));
				}
				childArray.add(child);
			}
			return childArray;
		} else {
			return null;
		}
	}

	@Transactional
	public CreateChildCommand getChildDetails(long parentId, long childId) throws ParseException {
		User currentParent = userDao.findById(parentId).orElse(null);
		if (currentParent != null) {
			Child currentChild = currentParent.getParentDetails().getChildren().stream()
					.filter(child -> child.getId().equals(childId)).findFirst().orElse(null);
			if (currentChild != null) {
				ParentDetails secondParent = currentChild.getParents().stream()
						.filter(parent -> !(parent.getId().equals(currentParent.getParentDetails().getId())))
						.findFirst().orElse(null);
				if (secondParent != null) {
					return new CreateChildCommand(currentChild.getId(), currentChild.getFirstname(),
							currentChild.getLastname(), currentChild.getPersonalCode(), currentChild.isAdopted(),
							new SimpleDateFormat("yyyy-MM-dd").format(currentChild.getBirthdate()), currentChild.getLivingAddress().getCity(),
							currentChild.getLivingAddress().getStreet(),
							currentChild.getLivingAddress().getHouseNumber(),
							currentChild.getLivingAddress().getFlatNumber(), true, secondParent.getId(),
							secondParent.getFirstname(), secondParent.getLastname(), secondParent.getEmail(),
							secondParent.getPhone(), secondParent.getPersonalCode(),
							secondParent.getLivingAddress().getCity(), secondParent.getLivingAddress().getStreet(),
							secondParent.getLivingAddress().getHouseNumber(),
							secondParent.getLivingAddress().getFlatNumber(), secondParent.getNumberOfKids(),
							secondParent.isStudying(), secondParent.getStudyingInstitution(),
							secondParent.isHasDisability(), secondParent.isDeclaredResidenceSameAsLiving(),
							secondParent.getDeclaredAddress().getCity(), secondParent.getDeclaredAddress().getStreet(),
							secondParent.getDeclaredAddress().getHouseNumber(),
							secondParent.getDeclaredAddress().getFlatNumber());
				} else {
					return new CreateChildCommand(currentChild.getId(), currentChild.getFirstname(),
							currentChild.getLastname(), currentChild.getPersonalCode(), currentChild.isAdopted(),
							new SimpleDateFormat("yyyy-MM-dd").format(currentChild.getBirthdate()), currentChild.getLivingAddress().getCity(),
							currentChild.getLivingAddress().getStreet(),
							currentChild.getLivingAddress().getHouseNumber(),
							currentChild.getLivingAddress().getFlatNumber(), false, 0L, "", "", "", "", 0L, "", "", "",
							"", 0, false, "", false, false, "", "", "", "");
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Transactional
	public void updateChild(ServiceLayerChild updatedChild, long userId, long childId) throws ParseException {
		User mainParent = userDao.findById(userId).orElse(null);
		if (mainParent != null) {
			Child child = mainParent.getParentDetails().getChildren().stream().filter(ch -> ch.getId().equals(childId))
					.findFirst().orElse(null);
			if (updatedChild.getSecondParentDetails() != null) {
				child.setFirstname(updatedChild.getFirstname());
				child.setLastname(updatedChild.getLastname());
				child.setPersonalCode(updatedChild.getPersonalCode());
				child.setAdopted(updatedChild.isAdopted());
				child.setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse(updatedChild.getBirthdate()));
				child.setLivingAddress(updatedChild.getLivingAddress());
				ParentDetails secondParent = detailsDao.findById(updatedChild.getSecondParentDetails().getId())
						.orElse(null);
				if (secondParent != null) {
					secondParent.setFirstname(updatedChild.getSecondParentDetails().getFirstname());
					secondParent.setLastname(updatedChild.getSecondParentDetails().getLastname());
					secondParent.setEmail(updatedChild.getSecondParentDetails().getEmail());
					secondParent.setPhone(updatedChild.getSecondParentDetails().getPhone());
					secondParent.setPersonalCode(updatedChild.getSecondParentDetails().getPersonalCode());
					secondParent.setLivingAddress(updatedChild.getSecondParentDetails().getLivingAddress());
					secondParent.setNumberOfKids(updatedChild.getSecondParentDetails().getNumberOfKids());
					secondParent.setStudying(updatedChild.getSecondParentDetails().isStudying());
					secondParent.setStudyingInstitution(updatedChild.getSecondParentDetails().getStudyingInstitution());
					secondParent.setHasDisability(updatedChild.getSecondParentDetails().isHasDisability());
					secondParent.setDeclaredResidenceSameAsLiving(
							updatedChild.getSecondParentDetails().isDeclaredResidenceSameAsLiving());
					secondParent.setDeclaredAddress(updatedChild.getSecondParentDetails().getDeclaredAddress());
					detailsDao.save(secondParent);
					childDao.save(child);
				}
			} else {
				child.setFirstname(updatedChild.getFirstname());
				child.setLastname(updatedChild.getLastname());
				child.setPersonalCode(updatedChild.getPersonalCode());
				child.setAdopted(updatedChild.isAdopted());
				child.setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse(updatedChild.getBirthdate()));
				child.setLivingAddress(updatedChild.getLivingAddress());
				childDao.save(child);
			}
		}
	}

	@Transactional
	public void deleteChild(long userId, long childId) {
		User parent = userDao.findById(userId).orElse(null);
		if (parent != null) {
			ParentDetails details = parent.getParentDetails();
			Set<Child> childList = details.getChildren();
			Child childToDelete = childList.stream().filter(ch -> ch.getId().equals(childId)).findFirst().orElse(null);
			if (childToDelete != null) {
				var parents = childToDelete.getParents();
				parents.clear();
				childToDelete.setParents(parents);
				childList.remove(childToDelete);
				details.setChildren(childList);
				userDao.save(parent);
				childDao.delete(childToDelete);
			}
		}
	}

}
