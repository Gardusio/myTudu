package my.spring.siw.tud.modelServices;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import my.spring.siw.tud.model.Comment;
import my.spring.siw.tud.model.Task;
import my.spring.siw.tud.persistence.CommentRepository;

@Service
public class CommentsService {
	
	@Autowired
	private CommentRepository commentsRepo;
	
	@Transactional(readOnly=true)
	public List<Comment> getAllByTask(Task t) { return this.commentsRepo.findAllByTask(t); }
	
	@Transactional
	public Comment saveComment(Comment c) {return this.commentsRepo.save(c);}
	
	@Transactional(readOnly=true)
	public Comment getCommentById(Long id) {
		Optional<Comment> retrieved = this.commentsRepo.findById(id);
		return retrieved.orElse(null);
	}
	
	@Transactional
	public void deleteComment(Comment c) {this.commentsRepo.delete(c);}
	
	@Transactional
	public void deleteById(Long id) {this.commentsRepo.deleteById(id);}

}
