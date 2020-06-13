package my.spring.siw.tud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;
import my.spring.siw.tud.controllers.session.Session;
import my.spring.siw.tud.model.Project;
import my.spring.siw.tud.model.Task;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.modelServices.ProjectService;
import my.spring.siw.tud.modelServices.TaskService;
import my.spring.siw.tud.modelServices.UserService;

@Controller
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private Session sessionData; 
	
	//controller can be used better to avoid db txs
	
	//need to better handle the owned proj..

	
	@RequestMapping(value="/newProject", method=RequestMethod.GET)
	public  String showProjectForm(Model model) {
		Project newProject = new Project();
		model.addAttribute("newProject",newProject);
		return "projectForm";
	}
	
	
	@RequestMapping(value="/saveProject", method= RequestMethod.POST) 
	public String saveProject(Model model, @ModelAttribute("project") Project toPersist, RedirectAttributes redirectAttributes) {
		Utente currentUser = sessionData.getLoggedUser(); 
		toPersist.setOwner(currentUser);
		List<Project> owned = this.projectService.findByOwner(currentUser); //extract method findAndAdd
		owned.add(toPersist);
		currentUser.setOwnedProjects(owned);
		
		this.projectService.saveProject(toPersist);
		this.userService.saveUser(currentUser);
		
		model.addAttribute("projects", owned);
		model.addAttribute("currentUser",currentUser);
		return "redirect:/showOwnedProjects";
}
	
	@RequestMapping(value="/showOwnedProjects", method=RequestMethod.GET)
	public String showOwned(Model model) {
		Utente currentUser = sessionData.getLoggedUser(); 
		List<Project> owned = this.projectService.findByOwner(currentUser); 
		model.addAttribute("currentUser",currentUser); 
		model.addAttribute("projects",owned);
		return "ownedProjects";
	}
	
	@RequestMapping(value="/deleteProject/{id}", method= RequestMethod.GET)
	public String deleteProject(@PathVariable("id") Long id,Model model) {
		Utente currentUser = sessionData.getLoggedUser(); 
		this.projectService.deleteById(id);
		model.addAttribute("currentUser",currentUser);
		List<Project> owned = this.projectService.findByOwner(currentUser);
		model.addAttribute("projects", owned);
		return "redirect:/showOwnedProjects";
	}
	
	@RequestMapping(value="/showVisibleProjects", method=RequestMethod.GET)
	public String showVisible(Model model)  {
		Utente currentUser = sessionData.getLoggedUser(); 
		List<Project> visibles = this.projectService.findByMembers(currentUser);
		model.addAttribute("visibleProjects", visibles);
		model.addAttribute("currentUser", currentUser);
		return "visibleProjects";
	}
	
	@RequestMapping(value="/projectPage/{id}", method= RequestMethod.GET)
	public String showProject(Model model, @PathVariable("id") Long id) {
		Utente currentUser = sessionData.getLoggedUser(); 
		Project thisProject = this.projectService.findById(id);
		List<Task> thisProjectTasks = this.taskService.getByProject(thisProject);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("thisProject",thisProject);
		model.addAttribute("tasks", thisProjectTasks);
		return "project";
	}
	
	@RequestMapping(value="/visibleProjectPage/{id}", method= RequestMethod.GET)
	public String showVisibleProject(Model model, @PathVariable("id") Long id) {
		Utente currentUser = sessionData.getLoggedUser(); 
		Project thisProject = this.projectService.findById(id);
		List<Task> thisProjectTasks = this.taskService.getByProject(thisProject);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("thisProject",thisProject);
		model.addAttribute("tasks", thisProjectTasks);
		return "visibleProjectPage";
	}
	
	
	//why i cant get projects/user from model?
	@RequestMapping(value="/addMember/{id}", method=RequestMethod.POST)
	public String addMember(Model model,
			@PathVariable("id") Long projectId,
			@RequestParam("memberUsername") String memberUsername) {
		
		Utente currentUser = sessionData.getLoggedUser(); 
		List<Project> owned = this.projectService.findByOwner(currentUser);
		
		Project addedTo = listFindById(projectId, owned);
		Utente newMember = this.userService.getByUsername(memberUsername);	
		
		if(newMember != null) { // && not already a member
			addedTo.addMember(newMember);
			newMember.addNewVisible(addedTo);
			this.userService.saveUser(newMember);
			this.projectService.saveProject(addedTo); 
			model.addAttribute("added", memberUsername +" added succesfully"); 
		}
		else {
			model.addAttribute("failed", memberUsername +" the User do not exist");  
		}
		
		model.addAttribute("currentUser",currentUser);
		model.addAttribute("projects",owned);
		return "ownedProjects";
	}
	
	@RequestMapping(value="/editProject/{id}", method =RequestMethod.POST)
	public String editProject(Model model, @PathVariable("id") Long id,
			@RequestParam("name") String newName,
			@RequestParam("description") String newDesc) {
		Project p = this.projectService.findById(id);
		
		if(newName != null && !newName.trim().isEmpty()) {
			p.setName(newName);
		}
		
		p.setDescription(newDesc);
		this.projectService.saveProject(p);

		return "redirect:/projectPage/" + id.toString();
	}
	
	//move this util
	private Project listFindById(Long projectId, List<Project> owned) {
		Iterator<Project> it = owned.iterator();
		
		while(it.hasNext()) {
			Project p = it.next();
			if (p.getId().equals(projectId))
				return p;
		}
		return null;
	}
	
	

}
