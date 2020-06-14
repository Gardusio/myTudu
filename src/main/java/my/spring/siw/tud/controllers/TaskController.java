package my.spring.siw.tud.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.spring.siw.tud.controllers.session.Session;
import my.spring.siw.tud.model.Comment;
import my.spring.siw.tud.model.Project;
import my.spring.siw.tud.model.Task;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.modelServices.CommentsService;
import my.spring.siw.tud.modelServices.ProjectService;
import my.spring.siw.tud.modelServices.TaskService;
import my.spring.siw.tud.modelServices.UserService;

@Controller
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CommentsService commentsService;
	
	@Autowired
	private Session sessionData;
	
	private Task currentTask;
	
	
	
	@RequestMapping(value="/taskPage/{id}", method=RequestMethod.GET)
	public String showTaskPage(@PathVariable("id") Long id, Model model) {
		Task thisTask = this.taskService.findById(id);
		this.currentTask = thisTask;
		model.addAttribute("thisTask", thisTask);
		model.addAttribute("currentUser", sessionData.getLoggedCredentials().getUser());
		return "task";
	}
	
	@RequestMapping(value="/newTask/{pId}", method=RequestMethod.POST) //changed POST
	public String newTask(Model model,@PathVariable("pId") Long id,
			@RequestParam("taskName") String taskName,
			@RequestParam("taskDescription") String taskDescription) {
		
		Task newTask = new Task(taskName,taskDescription);
		Project proj = this.projectService.findById(id);
		newTask.setProject(proj);
		proj.addTask(newTask);
		
		this.taskService.saveTask(newTask);
		return "redirect:/projectPage" +"/" + proj.getId().toString();
	}
	
	@RequestMapping(value="/deleteTask/{id}", method=RequestMethod.GET)
	public String deleteTask(@PathVariable("id") Long id, Model model) {
		Task task=this.taskService.findById(id);
		Project thisProject = task.getProject();
		this.taskService.deleteTask(task);
		return "redirect:/projectPage" +"/" + thisProject.getId().toString();
	}

	//think about ways to avoid needing userService here
	@RequestMapping(value="/assignTask/{id}", method=RequestMethod.POST)
	public String assignTask(Model model,
			@PathVariable("id") Long id,
			@RequestParam("username") String memberUsername) {
		
		Task task=this.taskService.findById(id);
		Project project = task.getProject();
		Utente assignTo = this.userService.getByUsername(memberUsername); 
		List<Utente> members = this.userService.getByVisibleProjects(project);
		
		if(assignTo == null || !(members.contains(assignTo))) { 
			String couldNotAssign ="this User is not a member of the Project";
			model.addAttribute("couldNotAssign", couldNotAssign);
		}
		else {
			task.setAssignedTo(assignTo); 
			this.taskService.saveTask(task);
			model.addAttribute("assigned", "Task Assigned to" + memberUsername);
		}
		
		return "redirect:/projectPage" + "/" + project.getId().toString();
	}
	
	
	@RequestMapping(value="/editTask/{id}", method = RequestMethod.POST)
	public String editTask(@PathVariable("id") Long id, Model model,
			@RequestParam("taskName") String newName,
			@RequestParam("taskDescription") String newDescription) {
		
		Task t = this.currentTask;
		t.setName(newName);
		t.setDescription(newDescription);
		this.taskService.saveTask(t);
		
		return "redirect:/taskPage" + "/" + t.getId().toString();
	}
	
	@RequestMapping(value="/addComment/{id}", method= RequestMethod.POST)
	private String addComment(Model model, @PathVariable("id") Long id, 
			@RequestParam("comment") String newCommentText) {
		
		Task t = this.taskService.findById(id);
		Project p = t.getProject();
		Comment newComment = new Comment(newCommentText);
		newComment.setUserCred(this.sessionData.getLoggedCredentials());
		newComment.setTask(t);
		t.addComment(newComment);
		
		this.commentsService.saveComment(newComment);
	
		if(p.getOwner().equals(this.sessionData.getLoggedCredentials().getUser()))
			return "redirect:/projectPage/" + p.getId().toString();
		return "redirect:/visibleProjectPage/" + p.getId().toString();
	}
	
	

	@RequestMapping(value="/deleteComment/{id}", method=RequestMethod.GET)
	public String deleteComment(@PathVariable("id") Long id, Model model) {
		Comment toDelete = this.commentsService.getCommentById(id);
		Project thisProject = toDelete.getTask().getProject();
		
		this.commentsService.deleteComment(toDelete);
		
		if(thisProject.getOwner().equals(this.sessionData.getLoggedCredentials().getUser()))
			return "redirect:/projectPage/" + thisProject.getId().toString();
		return "redirect:/visibleProjectPage/" + thisProject.getId().toString();
	}
	
	@RequestMapping(value="/setCompleted/{id}", method= RequestMethod.GET)
	public String setCompleted(Model model, @PathVariable("id") Long id) {
		Task t = this.taskService.findById(id);
		if(t.getIsCompleted() == false)
			t.setCompleted(true);
		else
			t.setCompleted(false);
		
		this.taskService.saveTask(t);
		return "redirect:/taskPage/" + id.toString();
	}
}
