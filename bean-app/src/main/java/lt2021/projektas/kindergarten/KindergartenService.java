package lt2021.projektas.kindergarten;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KindergartenService {
	
	@Autowired
	private KindergartenDao kgDao;
	
	@Transactional
	public void addKindergarten(CreateKindergartenCommand kindergarten) {
		kgDao.save(new Kindergarten(kindergarten.getName(), kindergarten.getAddress(),
				kindergarten.getSpotsInFirstAgeGroup(), kindergarten.getSpotsInSecondAgeGroup()));
	}
	
	@Transactional
	public CreateKindergartenCommand getKindergarten(long kgId) {
		Kindergarten kg = kgDao.findById(kgId).orElse(null);
		if (kg != null) {
			return new CreateKindergartenCommand(kg.getId(), kg.getName().toUpperCase(), kg.getAddress(),
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
		kg.setName(kindergarten.getName());
		kg.setAddress(kindergarten.getAddress());
		kg.setSpotsInFirstAgeGroup(kindergarten.getSpotsInFirstAgeGroup());
		kg.setSpotsInSecondAgeGroup(kindergarten.getSpotsInSecondAgeGroup());
		kgDao.save(kg);
	}
	
}
