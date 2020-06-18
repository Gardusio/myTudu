package my.spring.siw.tud.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import my.spring.siw.tud.model.Tag;
import my.spring.siw.tud.model.Utente;

public interface TagRepository extends CrudRepository<Tag, Long> {

	public Optional<Tag> findByName(String name);

	public Optional<List<Tag>> findByUser(Utente u);

}
