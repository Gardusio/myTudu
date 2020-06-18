package my.spring.siw.tud.persistence;

import org.springframework.data.repository.CrudRepository;


import my.spring.siw.tud.model.Credentials;
import my.spring.siw.tud.model.Utente;

public interface CredentialRepository extends CrudRepository<Credentials, Long> {
	
	public Credentials findByUsername(String username);
	
	public Credentials findByUser(Utente u);
}
