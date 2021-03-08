package lt2021.projektas.userRegister;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	
}
