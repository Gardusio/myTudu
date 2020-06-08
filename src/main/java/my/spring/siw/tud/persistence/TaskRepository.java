package my.spring.siw.tud.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import my.spring.siw.tud.model.*;

public interface TaskRepository extends CrudRepository<Task, Long> {

	List<Task> findByProject(Project thisProject);

}
