package my.spring.siw.tud.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import my.spring.siw.tud.model.Comment;
import my.spring.siw.tud.model.Task;

public interface CommentRepository extends CrudRepository<Comment, Long> {
	public List<Comment> findAllByTask(Task t);
	
}
