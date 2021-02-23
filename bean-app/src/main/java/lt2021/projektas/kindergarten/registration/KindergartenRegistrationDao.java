package lt2021.projektas.kindergarten.registration;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lt2021.projektas.child.Child;
import lt2021.projektas.kindergarten.admission.AdmissionProcess;

public interface KindergartenRegistrationDao extends JpaRepository<KindergartenRegistration, Long> {
	
	List<KindergartenRegistration> findByChild(Child child);
	
	@Query("select kgreg from KindergartenRegistration kgreg where kgreg.admission = :admission")
	List<KindergartenRegistration> findRegistrationsbyAdmission(@Param("admission")AdmissionProcess admission);
	
	@Query("select kgreg from KindergartenRegistration kgreg where kgreg.admission is null")
	List<KindergartenRegistration> findRegistrationsWithNullAdmission();
	
	@Query("select kgreg from KindergartenRegistration kgreg where (kgreg.firstPriority = :kgName or kgreg.secondPriority = :kgName "
			+ "or kgreg.thirdPriority = :kgName or kgreg.fourthPriority = :kgName or kgreg.fifthPriority = :kgName) and kgreg.admission is null")
	List<KindergartenRegistration> findRegistrationsWithSpecifiedKindergarten(@Param("kgName") String kindergartenName);
	
}
