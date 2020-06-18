package my.spring.siw.tud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.spring.siw.tud.controllers.session.Session;
import my.spring.siw.tud.model.Tag;
import my.spring.siw.tud.modelServices.TagService;

@Controller
public class TagController {
	
	@Autowired
	private Session sessionData;
	
	@Autowired
	private TagService tagService;
	
	@RequestMapping(value="/showTagsMenu", method=RequestMethod.GET)
	public String showTagsMenu(Model model) {
		model.addAttribute("currentCredentials", this.sessionData.getLoggedCredentials());
		model.addAttribute("currentUser", this.sessionData.getLoggedUser());
		
		model.addAttribute("available",this.tagService.getAllTags());
		
		model.addAttribute("newTag" ,new Tag());
		return "tagsMenu";
	}
	
	@RequestMapping(value="/newTag", method=RequestMethod.POST)
	public String saveTag(Model model, @ModelAttribute("newTag") Tag newTag, @RequestParam("radio") String color) {
		Tag tag = new Tag();
		tag.setColor(color);
		tag.setName(newTag.getName());
		tag.setDescription(newTag.getDescription());
		
		this.tagService.save(tag);
		
		return "redirect:/showTagsMenu";
	}
	
	
}
