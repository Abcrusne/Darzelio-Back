package lt2021.projektas.userRegister;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	Optional<User> searchByEmail(String email);
	
}
