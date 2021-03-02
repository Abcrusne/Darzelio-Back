package lt2021.projektas.kindergarten.admission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lt2021.projektas.kindergarten.queue.QueueService;
import lt2021.projektas.kindergarten.queue.QueueTableObject;
import lt2021.projektas.kindergarten.queue.RegistrationTableObject;
import lt2021.projektas.kindergarten.registration.KindergartenRegistrationService;

@RestController
@RequestMapping(value = "/api/kindergartens/admission")
public class AdmissionController {

	@Autowired
	private AdmissionService admissionService;

	@Autowired
	private KindergartenRegistrationService kgRegService;

	@Autowired
	private QueueService queueService;

	@RequestMapping(path = "/registrations", method = RequestMethod.GET)
	public RegistrationTableObject getAdmissionRegistrations() {
		return admissionService.getSortedAdmissionRegistrations(-1, "", "");
	}

	@RequestMapping(path = "/registrations", method = RequestMethod.GET, params = "page")
	public RegistrationTableObject getAdmissionRegistrations(@RequestParam int page) {
		return admissionService.getSortedAdmissionRegistrations(page, "", "");
	}

	@RequestMapping(path = "/registrations", method = RequestMethod.GET, params = { "page", "sortby" })
	public RegistrationTableObject getAdmissionRegistrations(@RequestParam int page, @RequestParam String sortby) {
		return admissionService.getSortedAdmissionRegistrations(page, sortby, "");
	}

	@RequestMapping(path = "/registrations", method = RequestMethod.GET, params = { "page", "sortby", "lastname" })
	public RegistrationTableObject getAdmissionRegistrations(@RequestParam int page, @RequestParam String sortby,
			@RequestParam String lastname) {
		return admissionService.getSortedAdmissionRegistrations(page, sortby, lastname.toLowerCase());
	}

	@RequestMapping(path = "/registrations/{childId}", method = RequestMethod.GET)
	public ChildAndParentDetailsObject getChildAndParentDetails(@PathVariable final long childId) {
		return admissionService.getChildDetailsFromRegistrationList(childId);
	}

	@RequestMapping(path = "/registrations/{childId}/delete", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteAdmissionRegistration(@PathVariable("childId") long childId) {
		/*
		if (!admissionService.areAdmissionsLocked()) {
			kgRegService.deleteRegistration(childId);
			return new ResponseEntity<String>("Vartotojas ištrintas", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Sąrašų redagavimas užrakintas sistemos administratoriaus",
					HttpStatus.BAD_REQUEST);
		}
		*/
		kgRegService.deleteRegistration(childId);
		return new ResponseEntity<String>("Vartotojas ištrintas", HttpStatus.OK);
	}

	@RequestMapping(path = "/registrations/confirm", method = RequestMethod.POST)
	public void confirmAdmissionRegistrations() {
		admissionService.confirmRegistrations();
	}
	
	@RequestMapping(path = "/status", method = RequestMethod.GET)
	public AdmissionStatusObject getAdmissionStatus() {
		return admissionService.admissionStatus();
	}

	@RequestMapping(path = "/activate", method = RequestMethod.POST)
	public void activateAdmission() {
		admissionService.activateAdmission();
	}

	@RequestMapping(path = "/deactivate", method = RequestMethod.POST)
	public void deactivateAdmission() {
		admissionService.deactivateAdmission();
	}
	
	/*

	@RequestMapping(path = "/lock", method = RequestMethod.POST)
	public void lockAdmission() {
		admissionService.lockAdmission();
	}

	@RequestMapping(path = "/unlock", method = RequestMethod.POST)
	public void unlockAdmission() {
		admissionService.unlockAdmission();
	}
	
	*/

	@RequestMapping(path = "/queues", method = RequestMethod.GET)
	public List<QueueTableObject> getAdmissionQueues() {
		return queueService.getAllAdmissionQueues();
	}
}
