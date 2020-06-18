package my.spring.siw.tud.controllers.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

import my.spring.siw.tud.model.Credentials;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.persistence.CredentialRepository;
import my.spring.siw.tud.persistence.UserRepository;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Session {

	private Utente currentUser;
	private Credentials currentCredentials;

	@Autowired
	private CredentialRepository credentialsRepo;

	@Autowired
	private UserRepository userRepo;

	public Credentials getLoggedCredentials() {
		if(this.currentCredentials == null)
			this.update();
		return this.currentCredentials;
	}

	public Utente getLoggedUser() {
		if(this.currentUser == null)
			this.update();
		return this.currentUser;
	}
	
	private void update() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(obj.getClass().equals(DefaultOidcUser.class)) { // user is logged in with OAuth
			DefaultOidcUser currentCredentials = (DefaultOidcUser) obj;
			
			/*Infos from Principal*/
			String username = currentCredentials.getEmail();
			String fullName = currentCredentials.getAttribute("name");
			String name = fullName.substring(0, fullName.indexOf(' '));
			String surname = currentCredentials.getAttribute("family_name");
			String email = currentCredentials.getEmail();
			
			Utente retrieved = this.userRepo.findByNameSurnameEmailOAuth(name, surname, email,true);
			
			if( retrieved == null) { /* first time login with google, create a user with infos from google-account*/
				Credentials newCred = new Credentials();
				newCred.setUsername(username);
				newCred.setPassword("[PROTECTED]");
				newCred.setRole(Credentials.DEFAULT_ROLE);
				Utente newUser = new Utente();
				newUser.setRegisteredWithOAuth(true);
				newUser.setName(name);
				newUser.setSurname(surname);
				newUser.setEmail(email);
				
				newCred.setUser(newUser);
				this.currentCredentials = this.credentialsRepo.save(newCred);
			}
			else { 
				/* if the user is already registered with google then retrieve its credentials */
				/* this allows users registered with google to change account details */
				/* also allows further authentication with Homemade Login-flow */
				this.currentUser = retrieved;
				this.currentCredentials = this.credentialsRepo.findByUser(retrieved);
			}
		}
		else 
			getHomeMadeAuth(obj);
	}
	

	public void getHomeMadeAuth(Object obj) {
		UserDetails currentUserDetails = (UserDetails) obj;	
		this.currentCredentials = this.credentialsRepo.findByUsername(currentUserDetails.getUsername());
		this.currentCredentials.setPassword("[PROTECTED]");
		this.currentUser = this.currentCredentials.getUser();
	}


	public Credentials getCurrentCredentials() {
		return currentCredentials;
	}

	public void clear() {
		this.currentCredentials = null;
		this.currentUser = null;
	}

}
