package my.spring.siw.tud.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import my.spring.siw.tud.model.*;


public interface UserRepository extends CrudRepository<Utente, Long> {
	
	public List<Utente> findByVisibleProjects(Project p);
	
	@Query("FROM Utente WHERE name = :name AND surname = :surname AND email = :email AND isRegisteredWithOAuth = :bool ")
	public Utente findByNameSurnameEmailOAuth(@Param("name") String name, @Param("surname") String surname, @Param("email") String email, @Param("bool") boolean is);

}
