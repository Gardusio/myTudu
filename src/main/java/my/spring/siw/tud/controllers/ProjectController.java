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
import my.spring.siw.tud.model.Credentials;
import my.spring.siw.tud.model.Project;
import my.spring.siw.tud.model.Tag;
import my.spring.siw.tud.model.Task;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.modelServices.ProjectService;
import my.spring.siw.tud.modelServices.TagService;
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

	@Autowired
	private TagService tagService; 


	@RequestMapping(value="/saveProject", method= RequestMethod.POST) 
	public String saveProject(Model model, @ModelAttribute("project") Project toPersist, RedirectAttributes redirectAttributes) {
		Utente currentUser = sessionData.getLoggedUser(); 
		
		toPersist.setOwner(currentUser);
		List<Project> owned = this.projectService.findByOwner(currentUser);
		owned.add(toPersist);
		currentUser.setOwnedProjects(owned);
		
		this.projectService.saveProject(toPersist);
		this.userService.saveUser(currentUser);

		return "redirect:/showOwnedProjects";
	}

	@RequestMapping(value="/showOwnedProjects", method=RequestMethod.GET)
	public String showOwned(Model model) {
		Credentials currentCredentials = sessionData.getLoggedCredentials();
		Utente currentUser = currentCredentials.getUser();
		
		List<Project> owned = this.projectService.findByOwner(currentUser); 

		model.addAttribute("currentUser",currentUser);
		model.addAttribute("currentCredentials",currentCredentials); 
		model.addAttribute("projects",owned);
		model.addAttribute("newProject",new Project());
		return "ownedProjects";
	}

	@RequestMapping(value="/deleteProject/{id}", method= RequestMethod.GET)
	public String deleteProject(@PathVariable("id") Long id,Model model) {
		this.projectService.deleteById(id);
		return "redirect:/showOwnedProjects";
	}

	@RequestMapping(value="/showVisibleProjects", method=RequestMethod.GET)
	public String showVisible(Model model)  {
		Credentials currentCredentials = sessionData.getLoggedCredentials();
		Utente currentUser = currentCredentials.getUser();
		
		List<Project> visibles = this.projectService.findByMembers(currentUser);
		
		model.addAttribute("visibleProjects", visibles);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("currentCredentials", currentCredentials);
		
		model.addAttribute("projectToTaskAssigned", this.projectService.getProjectToTaskAssigned(visibles, currentUser));
		
		return "visibleProjects";
	}

	@RequestMapping(value="/projectPage/{id}", method= RequestMethod.GET)
	public String showProject(Model model, @PathVariable("id") Long id) {
		Credentials currentCredentials = sessionData.getLoggedCredentials();
		Utente currentUser = currentCredentials.getUser();
		
		Project thisProject = this.projectService.findById(id);
		List<Task> thisProjectTasks = this.taskService.getByProject(thisProject);

		model.addAttribute("currentUser", currentUser);
		model.addAttribute("currentCredentials", currentCredentials);
		model.addAttribute("thisProject",thisProject);
		model.addAttribute("tasks", thisProjectTasks);
		model.addAttribute("newTask",new Task());
		
		return "project";
	}

	@RequestMapping(value="/visibleProjectPage/{id}", method= RequestMethod.GET)
	public String showVisibleProject(Model model, @PathVariable("id") Long id) {
		Credentials currentCredentials = sessionData.getLoggedCredentials();
		Utente currentUser = currentCredentials.getUser();
		
		Project thisProject = this.projectService.findById(id);
		List<Task> thisProjectTasks = this.taskService.getByProject(thisProject);
		
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("currentCredentials", currentCredentials);
		model.addAttribute("thisProject",thisProject);
		model.addAttribute("tasks", thisProjectTasks);
		
		return "visibleProjectPage";
	}


	@RequestMapping(value="/addMember/{id}", method=RequestMethod.POST)
	public String addMember(@PathVariable("id") Long projectId, @RequestParam("memberUsername") String memberUsername, RedirectAttributes redAtts) {
		Utente newMember = this.userService.getByUsername(memberUsername);
		Project addedTo = this.projectService.findById(projectId);	

		if(newMember == null )
			redAtts.addFlashAttribute("couldNotAdd",memberUsername +" does not exist"); 

		else if(addedTo.getMembers().contains(newMember))
			redAtts.addFlashAttribute("couldNotAdd",memberUsername +" is already a member of this project!");

		else 
		{
			addedTo.addMember(newMember);
			newMember.addNewVisible(addedTo);

			this.userService.saveUser(newMember);
			this.projectService.saveProject(addedTo); 

			redAtts.addFlashAttribute("added", memberUsername +" added succesfully"); 
		}

		return "redirect:/projectPage/" + addedTo.getId().toString();
	}
	

	@RequestMapping(value="/editProject/{id}", method =RequestMethod.POST)
	public String editProject(Model model, @PathVariable("id") Long id,
			@RequestParam("name") String newName,
			@RequestParam("description") String newDesc, RedirectAttributes redAtts) {

		//TODO better validation
		Project p = this.projectService.findById(id);

		if(newName != null && !newName.trim().isEmpty() && newName.length() <= 252) { // TODO define constants
			p.setName(newName);
		}

		if(newDesc.length() >= 252)
			redAtts.addFlashAttribute("tooLong", "The description must be less than 253 characters ");
		else {
			p.setDescription(newDesc);
			this.projectService.saveProject(p);
		}

		return "redirect:/projectPage/" + id.toString();
	}

	@RequestMapping(value="/leaveProject/{id}", method =RequestMethod.GET)
	public String leaveProject(Model model, @PathVariable("id") Long pId) {
		Project toLeave = this.projectService.findById(pId);
		Utente currentUser = this.sessionData.getLoggedUser();
		List<Utente> members = toLeave.getMembers();
		List<Project> visible = this.projectService.findByMembers(currentUser);

		deleteFromList(members,currentUser);
		deleteFromList(visible,toLeave);

		this.userService.saveUser(currentUser);
		this.projectService.saveProject(toLeave);

		return "redirect:/showVisibleProjects";
	}

	@RequestMapping(value="/kickMember/{mId}/from/{pId}", method= RequestMethod.GET) 
	public String kickMemberFromProject(Model model, @PathVariable("mId") Long mId, @PathVariable("pId") Long pId) {
		Project toKickFrom = this.projectService.findById(pId);
		Utente toKick = this.userService.getById(mId);

		List<Utente> members = toKickFrom.getMembers();
		List<Project> visible = toKick.getVisibleProjects(); 

		deleteFromList(members,toKick);
		deleteFromList(visible,toKickFrom);
		
		List<Task> taskAssigned = this.projectService.getTaskAssigned(toKickFrom,toKick);
		for(Task t : taskAssigned) { t.setAssignedTo(null);}

		this.userService.saveUser(toKick);
		this.projectService.saveProject(toKickFrom);

		return "redirect:/showOwnedProjects";
	}
	
	@RequestMapping(value="/addTag/{id}", method=RequestMethod.POST) 
	public String addTagToProject(Model model, @PathVariable("id") Long id, @RequestParam("tagName") String tagName){
		Tag t = this.tagService.findByName(tagName);
		Project p = this.projectService.findById(id);
		p.getProjectTags().add(t);
		this.projectService.saveProject(p);
		return "redirect:/showOwnedProjects";
	}


	private <T> void deleteFromList(List<T> list, T item) {
		Iterator<T> it = list.iterator();
		while(it.hasNext()) {
			T current = it.next();
			if(current.equals(item))
				it.remove();
		}
	}

}
