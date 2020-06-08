package my.spring.siw.tud.controllers.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import my.spring.siw.tud.model.Credentials;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.persistence.CredentialRepository;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Session {
	
	private Utente currentUser;
	private Credentials currentCredentials;
	
	@Autowired
	private CredentialRepository credentialsRepo;
	
	
	public Credentials getLoggedCredentials() {
		if(this.currentCredentials == null)
			this.update();
		return this.currentCredentials;
	}
	
	public Credentials getCurrentCredentials() { return currentCredentials; }
	
	public Utente getLoggedUser() {
		if(this.currentUser == null)
			this.update();
		return this.currentUser;
	}
	
	private void update() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails currentUserDetails = (UserDetails) obj;
		this.currentCredentials = this.credentialsRepo.findByUsername(currentUserDetails.getUsername());
		this.currentCredentials.setPassword("[PROTECTED]");
		this.currentUser = this.currentCredentials.getUser();
	}
	
	public void clear() {
		this.currentCredentials = null;
		this.currentUser = null;
	}

}
