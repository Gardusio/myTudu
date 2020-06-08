package my.spring.siw.tud.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import my.spring.siw.tud.model.*;

public interface ProjectRepository extends CrudRepository<Project, Long> {
	
	public List<Project> findByMembers(Utente member);
	
	public List<Project> findByOwner(Utente owner);

}
