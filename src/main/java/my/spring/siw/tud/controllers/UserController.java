package my.spring.siw.tud.controllers;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import my.spring.siw.tud.controllers.session.Session;
import my.spring.siw.tud.model.Credentials;
import my.spring.siw.tud.model.Project;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.modelServices.CredentialsService;
import my.spring.siw.tud.modelServices.UserService;
import my.spring.siw.tud.security.validation.CredentialsValidator;

@SuppressWarnings("unused")

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Session sessionData;
	
	@Autowired
	private CredentialsService credService; // its in userController instead of CredentialController (used once)
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	
	@RequestMapping(value="/userDetails", method = RequestMethod.GET)
	public String showUserDetails(Model model) {
		Credentials current = sessionData.getLoggedCredentials();
		
		model.addAttribute("currentCredentials",current);
		model.addAttribute("currentUser",current.getUser());

		return "details"; 
	}
	
	//BUG: Users can only update both username and password (due to CredentialValidator logic)
	@RequestMapping(value="/editDetails", method = RequestMethod.POST)
	public String editDetails(Model model, RedirectAttributes redirectAttributes,
			@RequestParam("username") String username, @RequestParam("password") String password) {
		
		Credentials currentCredentials = sessionData.getLoggedCredentials();
		
		Credentials newCredentials = new Credentials();
		newCredentials.setPassword(password);
		newCredentials.setUsername(username);
		
		DataBinder dataBinder = new DataBinder(newCredentials);
		BindingResult credentialsBindingResult = dataBinder.getBindingResult();
		
		this.credentialsValidator.validate(newCredentials, credentialsBindingResult);
		if(!credentialsBindingResult.hasErrors()) { // has error if the user only update pwd, username already exists
			currentCredentials.setPassword(password);
			currentCredentials.setUsername(username);
			this.credService.saveCredentials(currentCredentials);
			redirectAttributes.addFlashAttribute("changed", "Details changed");
			
		}
		else
			redirectAttributes.addFlashAttribute("notValid", "Enter valid credentials");
		
		return "redirect:/userDetails"; 
	}

}
