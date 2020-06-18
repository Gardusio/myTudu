package my.spring.siw.tud.modelServices;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import my.spring.siw.tud.model.Tag;
import my.spring.siw.tud.persistence.TagRepository;

@Service
public class TagService {
	
	@Autowired
	private TagRepository tagsRepo;
	
	@Transactional
	public Tag save(Tag t) {
		return this.tagsRepo.save(t);
	}
	
	@Transactional(readOnly=true)
	public Tag getById(Long id) {
		Optional<Tag> retrieved = this.tagsRepo.findById(id);
		return retrieved.orElse(null);
	}
	
	@Transactional
	public void deleteTag(Tag t) {
		this.tagsRepo.delete(t);
	}
	
	@Transactional
	public void deleteById(Long id) {
		this.tagsRepo.deleteById(id);
	}

	public List<Tag> getAllTags() {
		return (List<Tag>) this.tagsRepo.findAll();
	}

	public Tag findByName(String tagName) {
		Optional<Tag> retrieved = this.tagsRepo.findByName(tagName);
		return retrieved.orElse(null);
	}
	

}
