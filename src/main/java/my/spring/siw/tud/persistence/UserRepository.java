package my.spring.siw.tud.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import my.spring.siw.tud.model.*;


public interface UserRepository extends CrudRepository<Utente, Long> {
	
	public List<Utente> findByVisibleProjects(Project p);

}
