package my.spring.siw.tud.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.spring.siw.tud.controllers.session.Session;
import my.spring.siw.tud.model.Credentials;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.modelServices.CredentialsService;
import my.spring.siw.tud.security.validation.*;

@Controller
public class AuthController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private CredentialsValidator credentialsValidator;

	@Autowired
	private UserValidator userValidator;
	
	
	private static String authorizationRequestBaseUri= "oauth2/authorization";
	
	Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;
	
	@Autowired
	private Session sessionData;

	
	@RequestMapping(value="/signUp", method = RequestMethod.GET)
	public String showSignUpForm(Model model) {
		model.addAttribute("userForm", new Utente());
		model.addAttribute("credentialsForm", new Credentials());
		return "signUp";
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST) 
	public String signUpUser(@Valid @ModelAttribute("userForm") Utente user,BindingResult userBindingResult,
			@Valid @ModelAttribute("credentialsForm") Credentials credentials,BindingResult credentialsBindingResult
			,Model model) {
		
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
		
		if(!(userBindingResult.hasErrors()) && !(credentialsBindingResult.hasErrors())) {
			credentials.setUser(user);
			this.credentialsService.saveCredentials(credentials);
			model.addAttribute("registrationCompleted", "congrats!");
			
			if(this.sessionData.getCurrentCredentials()!=null) { //users can create new accounts while logged in, but have to logout after one is created
				return "redirect:/logout";
			}
			else
				return "loginPage";		
		}
		return "signUp";
	}
		
		@SuppressWarnings("unchecked")
		@RequestMapping(value="/login", method = RequestMethod.GET)
		public String customLoginHandler(Model model) {  
			
			/* redirect to profile if already logged-in */
			if(sessionData.getCurrentCredentials() != null) {
				model.addAttribute("currentUser",sessionData.getLoggedUser());
				model.addAttribute("currentCredentials",sessionData.getLoggedCredentials());
				return "redirect:/profile";
			}
			
			/* get clients registrations infos */
			 Iterable<ClientRegistration> clientRegistrations = null;
			    ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
			    if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
			        clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
			    }
			 
			    clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), 
			    							authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
			    
			    model.addAttribute("urls", oauth2AuthenticationUrls);
			return "loginPage";
		}
		

		
		@RequestMapping(value="/logout", method = RequestMethod.GET)
		public String logout(Model model) {
			this.sessionData.clear();
			return "index";
		}
		
		@RequestMapping(value="/profile", method = RequestMethod.GET)
		public String showProfile(Model model) {
			return "redirect:/showOwnedProjects";
		}
		
		@RequestMapping(value="/admin",method = RequestMethod.GET)
		public String admin(Model model) {
			return "admin";
		}
}
