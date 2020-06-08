package my.spring.siw.tud.persistence;

import org.springframework.data.repository.CrudRepository;

import my.spring.siw.tud.model.Credentials;

public interface CredentialRepository extends CrudRepository<Credentials, Long> {
	
	public Credentials findByUsername(String username);
}
