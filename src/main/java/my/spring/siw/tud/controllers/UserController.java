package my.spring.siw.tud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.spring.siw.tud.controllers.session.Session;
import my.spring.siw.tud.model.Project;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.modelServices.UserService;

@SuppressWarnings("unused")

@Controller
public class UserController {
	
	@Autowired
	private Session sessionData;
	
	@Autowired
	private UserService userService;
	
	private Utente currentUser;
	
	@RequestMapping(value="/userDetails", method = RequestMethod.GET)
	public String showUserDetails(Model model) {
		return "details"; 
	}

	
	public void setCurrentUser(Utente current) { this.currentUser = current; }
	public Utente getCurrentUser() { return currentUser; }
	


}
