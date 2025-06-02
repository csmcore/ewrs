package app.ewarehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Role;
import app.ewarehouse.entity.Users;



@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByEmail(String email);
	
	Users findByRole(Role role);
	
	@Query("Select id from Users order by id desc  limit 1")
	String getId();
	 
}
