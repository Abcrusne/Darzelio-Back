package lt2021.projektas.child;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lt2021.projektas.parentdetails.Address;
import lt2021.projektas.parentdetails.ServiceLayerDetails;

@RestController
@RequestMapping(value = "/api/users/{userId}/parentdetails/children")
public class ChildrenController {
	
	@Autowired
	private ChildService childService;
	
	@RequestMapping(method = RequestMethod.POST)
	public void addChild(@RequestBody final CreateChildCommand child, @PathVariable("userId") final long id) {
		if (!(child.isSecondParent())) {
			childService.addChild(id, new ServiceLayerChild(child.getFirstname(), child.getLastname(), child.getPersonalCode(), child.isAdopted(), 
					child.getBirthdate(), new Address(child.getCity(), child.getStreet(), child.getHouseNumber(), child.getFlatNumber())));
		} else {
			childService.addChild(id, new ServiceLayerChild(child.getFirstname(), child.getLastname(), child.getPersonalCode(), child.isAdopted(), 
					child.getBirthdate(), new Address(child.getCity(), child.getStreet(), child.getHouseNumber(), child.getFlatNumber()), new ServiceLayerDetails(child.getSecondParentFirstname(), 
							child.getSecondParentLastname(), child.getSecondParentEmail(), child.getSecondParentPhone(), child.getSecondParentPersonalCode(), 
							new Address(child.getSecondParentCity(), child.getSecondParentStreet(), child.getSecondParentHouseNumber(), child.getSecondParentFlatNumber()), 
							child.getSecondParentNumberOfKids(), child.isSecondParentStudying(), child.getSecondParentStudyingInstitution(), child.isSecondParentHasDisability(), 
							child.isSecondParentDeclaredResidenceSameAsLiving(), new Address(child.getSecondParentDeclaredCity(), child.getSecondParentDeclaredStreet(), 
									child.getSecondParentDeclaredHouseNumber(), child.getSecondParentDeclaredFlatNumber()))));
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<CreateChildCommand> getChildren(@PathVariable("userId") final long id) {
		return childService.getChildren(id).stream()
				.map(child -> new CreateChildCommand(child.getId(), child.getFirstname(), child.getLastname(), child.getPersonalCode(), child.isAdopted(), 
						child.getBirthdate(), child.getLivingAddress().getCity(), child.getLivingAddress().getStreet(), child.getLivingAddress().getHouseNumber(), 
						child.getLivingAddress().getFlatNumber(), false, 0L, "", "", "", "", 0L, "", "", "", "", 0, false, "", false, false, "", "", "", ""))
				.collect(Collectors.toList());
	}
	
	@RequestMapping(path = "/{childId}", method = RequestMethod.GET)
	public CreateChildCommand getChildDetails(@PathVariable("userId") final long userId, @PathVariable("childId") final long childId) {
		return childService.getChildDetails(userId, childId);
	}
	
	@RequestMapping(path = "/{childId}", method = RequestMethod.PUT)
	public void updateChild(@RequestBody final CreateChildCommand child, @PathVariable("userId") final long userId, @PathVariable("childId") final long childId) {
		if (!(child.isSecondParent())) {
			childService.updateChild(new ServiceLayerChild(child.getFirstname(), child.getLastname(), child.getPersonalCode(), child.isAdopted(), 
					child.getBirthdate(), new Address(child.getCity(), child.getStreet(), child.getHouseNumber(), child.getFlatNumber())), userId, childId);
		} else {
			childService.updateChild(new ServiceLayerChild(child.getFirstname(), child.getLastname(), child.getPersonalCode(), child.isAdopted(), 
					child.getBirthdate(), new Address(child.getCity(), child.getStreet(), child.getHouseNumber(), child.getFlatNumber()), new ServiceLayerDetails(child.getSecondParentId(),
							child.getSecondParentFirstname(), child.getSecondParentLastname(), child.getSecondParentEmail(), child.getSecondParentPhone(), child.getSecondParentPersonalCode(), 
							new Address(child.getSecondParentCity(), child.getSecondParentStreet(), child.getSecondParentHouseNumber(), child.getSecondParentFlatNumber()), 
							child.getSecondParentNumberOfKids(), child.isSecondParentStudying(), child.getSecondParentStudyingInstitution(), child.isSecondParentHasDisability(), 
							child.isSecondParentDeclaredResidenceSameAsLiving(), new Address(child.getSecondParentDeclaredCity(), child.getSecondParentDeclaredStreet(), 
									child.getSecondParentDeclaredHouseNumber(), child.getSecondParentDeclaredFlatNumber()))), userId, childId);
		}
	}
	
	@RequestMapping(path = "/{childId}", method = RequestMethod.DELETE)
	public void deleteChild(@PathVariable("userId") final long userId, @PathVariable("childId") final long childId) {
		childService.deleteChild(userId, childId);
	}

}
