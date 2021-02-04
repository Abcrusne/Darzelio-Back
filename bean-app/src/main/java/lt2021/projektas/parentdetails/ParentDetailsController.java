package lt2021.projektas.parentdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users/{id}/parentdetails")
public class ParentDetailsController {
	
	@Autowired
	private ParentDetailsService detailsService;
	
	@RequestMapping(method = RequestMethod.GET)
	public CreateDetailsCommand getParentDetails(@PathVariable final long id) {
		ServiceLayerDetails parent = detailsService.getParentDetails(id);
		return new CreateDetailsCommand(id, parent.getPhone(), parent.getPersonalCode(), parent.getLivingAddress().getCity(), 
				parent.getLivingAddress().getStreet(), parent.getLivingAddress().getHouseNumber(), parent.getLivingAddress().getFlatNumber(), 
				parent.getNumberOfKids(), parent.isStudying(), parent.getStudyingInstitution(), parent.isHasDisability(), parent.isDeclaredResidenceSameAsLiving(), 
				parent.getDeclaredAddress().getCity(), parent.getDeclaredAddress().getStreet(), parent.getDeclaredAddress().getHouseNumber(), parent.getDeclaredAddress().getFlatNumber());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void addParentDetails(@RequestBody final CreateDetailsCommand details, @PathVariable final long id) {
		detailsService.addParentDetails(new ServiceLayerDetails(id, details.getPhone(), details.getPersonalCode(), new Address(details.getCity(), details.getStreet(), 
				details.getHouseNumber(), details.getFlatNumber()), details.getNumberOfKids(), details.isStudying(), details.getStudyingInstitution(), 
				details.isHasDisability(), details.isDeclaredResidenceSameAsLiving(), new Address(details.getDeclaredCity(), details.getDeclaredStreet(), 
						details.getDeclaredHouseNumber(), details.getDeclaredFlatNumber())));
	}
}
