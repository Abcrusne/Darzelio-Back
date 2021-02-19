package lt2021.projektas.kindergarten.registration;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lt2021.projektas.child.Child;

public interface KindergartenRegistrationDao extends JpaRepository<KindergartenRegistration, Long> {
	
	Optional<KindergartenRegistration> findByChild(Child child);
	
	@Query("select kgreg from KindergartenRegistration kgreg where kgreg.firstPriority = :kgName or kgreg.secondPriority = :kgName "
			+ "or kgreg.thirdPriority = :kgName or kgreg.fourthPriority = :kgName or kgreg.fifthPriority = :kgName")
	List<KindergartenRegistration> findRegistrationsWithSpecifiedKindergarten(@Param("kgName") String kindergartenName);
	
}
