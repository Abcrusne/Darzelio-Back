package lt2021.projektas.userRegister;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	
}
