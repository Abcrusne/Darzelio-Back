package lt2021.projektas.kindergarten.registration;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import lt2021.projektas.child.Child;

public interface KindergartenRegistrationDao extends JpaRepository<KindergartenRegistration, Long> {
	
	Optional<KindergartenRegistration> findByChild(Child child);
	
}
