package lt2021.projektas.child;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lt2021.projektas.parentdetails.Address;
import lt2021.projektas.parentdetails.ServiceLayerDetails;

@RestController
@RequestMapping(value = "/api/users/{id}/parentdetails/children")
public class ChildrenController {
	
	@Autowired
	private ChildService childService;
	
	@RequestMapping(method = RequestMethod.POST)
	public void addChild(@RequestBody final CreateChildCommand child, @PathVariable final long id) {
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

}
