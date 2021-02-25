package lt2021.projektas.kindergarten;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lt2021.projektas.kindergarten.admission.AdmissionService;
import lt2021.projektas.kindergarten.admission.AdmissionTableObject;
import lt2021.projektas.kindergarten.queue.AgeGroup;
import lt2021.projektas.kindergarten.queue.QueueService;
import lt2021.projektas.kindergarten.queue.QueueTableObject;
import lt2021.projektas.kindergarten.queue.RegistrationTableObject;
import lt2021.projektas.kindergarten.registration.CreateRegistrationCommand;
import lt2021.projektas.kindergarten.registration.KindergartenRegistrationService;

@RestController
@RequestMapping(value = "/api/kindergartens")
public class KindergartenController {
	
	@Autowired
	private KindergartenService kgService;
	
	@Autowired
	private KindergartenRegistrationService kgRegService;
	
	@Autowired
	private AdmissionService admissionService;
	
	@Autowired
	private QueueService queueService;
	
	/*
	 * Kindergarten CRUD
	 */
	
	@RequestMapping(path = "/{kgId}", method = RequestMethod.GET)
	public CreateKindergartenCommand getKindergarten(@PathVariable final long kgId) {
		return kgService.getKindergarten(kgId);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<CreateKindergartenCommand> getAllKindergartens() {
		return kgService.getAllKindergartens();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void createKindergarten(@RequestBody final CreateKindergartenCommand kg) {
		kgService.addKindergarten(kg);
	}
	
	@RequestMapping(path = "/{kgId}", method = RequestMethod.PUT)
	public void updateKindergarten(@RequestBody final CreateKindergartenCommand kg, @PathVariable final long kgId) {
		kgService.updateKindergarten(kg, kgId);
	}
	
	@RequestMapping(path = "/{kgId}", method = RequestMethod.DELETE)
	public List<CreateKindergartenCommand> deleteKinderkarten(@PathVariable final long kgId) {
		return kgService.deleteKindergarten(kgId);
	}
	
	/*
	 * Kindergarten registration CRUD
	 */
	
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public ResponseEntity<String> addRegistration(@RequestBody final CreateRegistrationCommand reg) {
		return kgRegService.addRegistration(reg);
	}
	
	@RequestMapping(path = "/register", method = RequestMethod.GET)
	public List<CreateRegistrationCommand> getAllRegistrations() {
		return kgRegService.getAllRegistrations();
	}
	
	/*
	 * Admission CRUD
	 */
	
	@RequestMapping(path = "/startadmission", method = RequestMethod.POST)
	public ResponseEntity<String> startAdmissionProcess() {
		return admissionService.createNewAdmissionProcess();
	}
	
	
	@RequestMapping(path = "/stopadmission", method = RequestMethod.POST)
	public ResponseEntity<String> stopAdmission() {
		return admissionService.closeAdmissionProcess();
	}
	
	@RequestMapping(path = "/admissions", method = RequestMethod.GET)
	public List<AdmissionTableObject> getAllAdmissions() {
		return admissionService.getAllAdmissionProcesses();
	}
	
	/*
	 * Admission queue CRUD
	 */
	
	@RequestMapping(path = "/admissions/{admissionId}/queues", method = RequestMethod.GET)
	public List<QueueTableObject> getCurrentAdmissionQueues(@PathVariable final long admissionId) {
		return queueService.getCurrentAdmissionProcessQueues(admissionId).stream()
				.map(queue -> new QueueTableObject(queue.getId(), queue.getKindergarten().getName(), queue.getAgeGroup().toString(),
						queue.getRegistrations().size(),
						queue.getAgeGroup() == AgeGroup.PRESCHOOL ? queue.getKindergarten().getSpotsInFirstAgeGroup() : queue.getKindergarten().getSpotsInSecondAgeGroup(),
								queue.isApproved()))
				.collect(Collectors.toList());
	}
	
	@RequestMapping(path = "/admissions/{admissionId}/queues/{queueId}/confirm", method = RequestMethod.POST)
	public ResponseEntity<String> approveAdmissionQueue(@PathVariable("admissionId") final long admissionId, @PathVariable("queueId") final long queueId) {
		return queueService.approveAdmissionQueue(admissionId, queueId);
	}
	
	@RequestMapping(path = "/admissions/{admissionId}/queues/{queueId}/registrations", method = RequestMethod.GET)
	public List<RegistrationTableObject> getAdmissionQueueRegistrations(@PathVariable("admissionId") final long admissionId, @PathVariable("queueId") final long queueId) {
		return queueService.getQueueRegistrations(admissionId, queueId);
	}
	
	
}
