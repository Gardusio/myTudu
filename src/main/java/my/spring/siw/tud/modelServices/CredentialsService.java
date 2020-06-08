package my.spring.siw.tud.modelServices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import my.spring.siw.tud.model.Credentials;
import my.spring.siw.tud.persistence.CredentialRepository;

@Service
public class CredentialsService {
	
	@Autowired
	private CredentialRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public Credentials getCredentialsById(long id) {
		Optional<Credentials> retrieved = this.repo.findById(id);
		return retrieved.orElse(null);
	}
	
	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);
		credentials.setPassword(this.encoder.encode(credentials.getPassword()));
		return this.repo.save(credentials);
	}

	public Credentials retrieveCredentialsByUsername(String username) {
		return this.repo.findByUsername(username);
	}
	
	
	
	
}
