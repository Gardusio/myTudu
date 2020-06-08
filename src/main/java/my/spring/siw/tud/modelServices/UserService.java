package my.spring.siw.tud.modelServices;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import my.spring.siw.tud.model.*;
import my.spring.siw.tud.persistence.CredentialRepository;
import my.spring.siw.tud.persistence.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CredentialRepository credentialRepo;
	
	@Transactional(readOnly=true)
	public Utente getById(Long id) {
		Optional<Utente> retrieved = this.userRepo.findById(id);
		return retrieved.orElse(null);
	}
	
	/*
	@Transactional(readOnly=true)
	public Utente getByUsername(String username) {
		Optional<Utente> retrieved = this.userRepo.findByUsername(username);
		return retrieved.orElse(null);
	}
	*/
	
	@Transactional
	public Utente saveUser(Utente u) {
		return this.userRepo.save(u); //throws DataIntegrityViolation exception: the username is marked as unique
	}
	
	@Transactional(readOnly=true)
	public List<Utente> getAll() {
		List<Utente> users = new ArrayList<Utente>();
		Iterable<Utente> retrievedUsers = this.userRepo.findAll();
		for(Utente u : retrievedUsers) {
			users.add(u);
		}
		return users;
	}
	
	@Transactional
	public void delete(Utente u) {
		this.userRepo.delete(u);
	}
	
	@Transactional(readOnly=true)
	public List<Utente> getByVisibleProjects(Project p) {
		return this.userRepo.findByVisibleProjects(p);
	}
	
	@Transactional(readOnly=true)
	public Utente getByUsername(String memberUsername) {
		Credentials retrieved = this.credentialRepo.findByUsername(memberUsername);
		if(retrieved != null)
			return retrieved.getUser();
		return null;
	}


	
	


	

}
