package my.spring.siw.tud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MainController {
	
	
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String welcome(Model model) {
		return "index";
	}
	
	@RequestMapping(value = {"/about"}, method = RequestMethod.GET)
	public String showAbout(Model model) {
		return "about";
	}



}
