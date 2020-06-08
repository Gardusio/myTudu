package my.spring.siw.tud.modelServices;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import my.spring.siw.esame.model.Comment;
//import my.spring.siw.esame.model.Tag;
import my.spring.siw.tud.model.*;
import my.spring.siw.tud.persistence.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepo;

	@Transactional
	public Task saveTask(Task t) {
		return this.taskRepo.save(t);
	}

	
	@Transactional
	public void deleteTask(Task t)  {
		this.taskRepo.delete(t);
	}

	@Transactional
	public void setCompleted(Task t) {
		t.setCompleted(true);
		this.saveTask(t);
	}

	@Transactional(readOnly=true)
	public List<Task> getByProject(Project thisProject) {
		return this.taskRepo.findByProject(thisProject);
	}
	
	@Transactional(readOnly=true)
	public Task findById(Long id) {
		Optional<Task> retrieved = this.taskRepo.findById(id);
		return retrieved.orElse(null);
	}

	/* Estensione
	@Transactional
	public void addTag(Task t, Tag tag) {
		t.addTag(tag);
		this.taskRepo.save(t);
	}

	@Transactional
	public void addComment(Task t, Comment c) {
		t.addComment(c);
		this.taskRepo.save(t);
	}
 */
}
