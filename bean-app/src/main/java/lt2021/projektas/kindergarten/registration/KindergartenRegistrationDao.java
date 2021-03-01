package lt2021.projektas.kindergarten.registration;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lt2021.projektas.child.Child;

public interface KindergartenRegistrationDao extends JpaRepository<KindergartenRegistration, Long> {
	
	List<KindergartenRegistration> findByChild(Child child);
	
	@Query("select count(kgreg) from KindergartenRegistration kgreg where kgreg.admission is not null")
	long registrationWithAdmissionCount();
	
	@Query("select kgreg from KindergartenRegistration kgreg where kgreg.admission is not null")
	List<KindergartenRegistration> findRegistrationsWithAdmission(Pageable pageable);
	
	@Query("select kgreg from KindergartenRegistration kgreg where kgreg.admission is null")
	List<KindergartenRegistration> findRegistrationsWithoutAdmission();
	
	@Query("select kgreg from KindergartenRegistration kgreg where (kgreg.firstPriority = :kgName or kgreg.secondPriority = :kgName "
			+ "or kgreg.thirdPriority = :kgName or kgreg.fourthPriority = :kgName or kgreg.fifthPriority = :kgName) and kgreg.admission is null")
	List<KindergartenRegistration> findRegistrationsWithSpecifiedKindergarten(@Param("kgName") String kindergartenName);
	
}
