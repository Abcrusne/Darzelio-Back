package lt2021.projektas.kindergarten;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.kindergarten.queue.QueueService;
import lt2021.projektas.kindergarten.registration.KindergartenRegistrationDao;

@Service
public class KindergartenService {
	
	@Autowired
	private KindergartenDao kgDao;
	
	@Autowired
	private KindergartenRegistrationDao registrationDao;
	
	@Autowired
	private QueueService queueService;
	
	@Transactional
	public void addKindergarten(CreateKindergartenCommand kindergarten) {
		queueService.createNewQueuesForKindergarten(new Kindergarten(kindergarten.getName().toUpperCase(), kindergarten.getAddress(),
				kindergarten.getSpotsInFirstAgeGroup(), kindergarten.getSpotsInSecondAgeGroup()));
	}
	
	@Transactional
	public CreateKindergartenCommand getKindergarten(long kgId) {
		Kindergarten kg = kgDao.findById(kgId).orElse(null);
		if (kg != null) {
			return new CreateKindergartenCommand(kg.getId(), kg.getName(), kg.getAddress(),
					kg.getSpotsInFirstAgeGroup(), kg.getSpotsInSecondAgeGroup());
		} else {
			return null;
		}
	}
	
	@Transactional
	public List<CreateKindergartenCommand> getAllKindergartens() {
		return kgDao.findAll().stream()
					.map(kg -> new CreateKindergartenCommand(kg.getId(), kg.getName().toUpperCase(), kg.getAddress(), kg.getSpotsInFirstAgeGroup(), kg.getSpotsInSecondAgeGroup()))
					.collect(Collectors.toList());
	}
	
	@Transactional
	public List<CreateKindergartenCommand> deleteKindergarten(long kgId) {
		kgDao.deleteById(kgId);
		return getAllKindergartens();
	}
	
	@Transactional
	public void updateKindergarten(CreateKindergartenCommand kindergarten, long kgId) {
		Kindergarten kg = kgDao.findById(kgId).orElse(null);
		var registrations = registrationDao.findRegistrationsWithSpecifiedKindergarten(kg.getName());
		registrations.forEach(reg -> {
			if (reg.getFirstPriority().equals(kg.getName())) {
				reg.setFirstPriority(kindergarten.getName().toUpperCase());
			}
			if (reg.getSecondPriority().equals(kg.getName())) {
				reg.setSecondPriority(kindergarten.getName().toUpperCase());
			}
			if (reg.getThirdPriority().equals(kg.getName())) {
				reg.setThirdPriority(kindergarten.getName().toUpperCase());
			}
			if (reg.getFourthPriority().equals(kg.getName())) {
				reg.setFourthPriority(kindergarten.getName().toUpperCase());
			}
			if (reg.getFifthPriority().equals(kg.getName())) {
				reg.setFifthPriority(kindergarten.getName().toUpperCase());
			}
			registrationDao.save(reg);
		});
		kg.setName(kindergarten.getName().toUpperCase());
		kg.setAddress(kindergarten.getAddress());
		kg.setSpotsInFirstAgeGroup(kindergarten.getSpotsInFirstAgeGroup());
		kg.setSpotsInSecondAgeGroup(kindergarten.getSpotsInSecondAgeGroup());
		kgDao.save(kg);
	}
	
}
