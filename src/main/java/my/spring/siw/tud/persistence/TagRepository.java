package my.spring.siw.tud.persistence;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import my.spring.siw.tud.model.Tag;

public interface TagRepository extends CrudRepository<Tag, Long> {

	public Optional<Tag> findByName(String name);

}
