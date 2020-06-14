package my.spring.siw.tud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.spring.siw.tud.controllers.session.Session;
import my.spring.siw.tud.model.Credentials;


@Controller
public class MainController {
	
	@Autowired
	private Session sessionData;
	
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String welcome(Model model) {
		return "index";
	}
	
	@RequestMapping(value = {"/about"}, method = RequestMethod.GET)
	public String showAbout(Model model) {
		Credentials current = this.sessionData.getLoggedCredentials();
		model.addAttribute("currentCredentials",current);
		model.addAttribute("currentUser",current.getUser());
		return "about";
	}



}
