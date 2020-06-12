package my.spring.siw.tud.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private Session sessionData;

	
	
	@RequestMapping(value="/signUp", method = RequestMethod.GET)
	public String showSignUpForm(Model model) {
		model.addAttribute("userForm", new Utente());
		model.addAttribute("credentialsForm", new Credentials());
		return "signUp";
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST) //maybe u can call this signUp with post?
	public String signUpUser(@Valid @ModelAttribute("userForm") Utente user,BindingResult userBindingResult,
			@Valid @ModelAttribute("credentialsForm") Credentials credentials,BindingResult credentialsBindingResult
			,Model model) {
		
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
		
		if(!(userBindingResult.hasErrors()) && !(credentialsBindingResult.hasErrors())) {
			credentials.setUser(user);
			System.out.println(user.getOwnedProjects().toString());
			this.credentialsService.saveCredentials(credentials);
			model.addAttribute("registrationCompleted", "congrats!");
			
			if(this.sessionData.getCurrentCredentials()!=null) {
				return "redirect:/logout";
			}
			else
				return "loginPage";		
		}
		return "signUp";
	}
		
		@RequestMapping(value="/login", method = RequestMethod.GET)
		public String customLoginHandler(Model model) {  
			if(sessionData.getCurrentCredentials() != null) {
				model.addAttribute("currentUser",sessionData.getLoggedUser());
				model.addAttribute("currentCredentials",sessionData.getLoggedCredentials());
				return "redirect:/profile";
			}
			return "loginPage";
		}
		
		
		@RequestMapping(value="/logout", method = RequestMethod.GET)
		public String logout(Model model) {
			this.sessionData.clear();
			return "index";
		}
		
		@RequestMapping(value="/profile", method = RequestMethod.GET)
		public String showProfile(Model model) {
			Credentials currentCredentials = this.sessionData.getLoggedCredentials(); 
			model.addAttribute("currentCredentials", currentCredentials);
			model.addAttribute("currentUser", currentCredentials.getUser());
			return "profile";
		}
		
		@RequestMapping(value="/admin",method = RequestMethod.GET)
		public String admin(Model model) {
			return "admin";
		}
}
