package my.spring.siw.tud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.spring.siw.tud.controllers.session.Session;
import my.spring.siw.tud.model.Credentials;
import my.spring.siw.tud.model.Project;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.modelServices.CredentialsService;
import my.spring.siw.tud.modelServices.UserService;

@SuppressWarnings("unused")

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Session sessionData;
	
	@Autowired
	private CredentialsService credService; // its in userController instead of CredentialController (used once)
	
	@RequestMapping(value="/userDetails", method = RequestMethod.GET)
	public String showUserDetails(Model model) {
		Credentials current = sessionData.getLoggedCredentials();
		model.addAttribute("currentCredentials",current);
		return "details"; 
	}
	
	//make it realistic
	@RequestMapping(value="/editDetails", method = RequestMethod.POST)
	public String editDetails(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {
		Credentials currentCredentials = sessionData.getLoggedCredentials();
		currentCredentials.setPassword(password);
		currentCredentials.setUsername(username);
		
		this.credService.saveCredentials(currentCredentials);
		
		model.addAttribute("changed", "Details changed");
		model.addAttribute("currentCredentials",currentCredentials);
		return "details"; 
	}

}
