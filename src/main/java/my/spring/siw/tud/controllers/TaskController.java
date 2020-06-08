package my.spring.siw.tud.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.spring.siw.tud.model.Project;
import my.spring.siw.tud.model.Task;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.modelServices.ProjectService;
import my.spring.siw.tud.modelServices.TaskService;
import my.spring.siw.tud.modelServices.UserService;

@Controller
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService; //??
	
	@Autowired
	private ProjectService projectService; //??
	
	private Task currentTask;

	
	@RequestMapping(value="/taskPage/{id}", method=RequestMethod.GET)
	public String showTaskPage(@PathVariable("id") Long id, Model model) {
		Task thisTask = this.taskService.findById(id);
		this.currentTask = thisTask;
		model.addAttribute("thisTask", thisTask);
		return "task";
	}
	
	@RequestMapping(value="/newTask/{pId}", method=RequestMethod.POST) //changed POST
	public String newTask(Model model,@PathVariable("pId") Long id,
			@RequestParam("taskName") String taskName,
			@RequestParam("taskDescription") String taskDescription) {
		
		Task newTask = new Task(taskName,taskDescription);
		Project proj = this.projectService.findById(id);
		newTask.setThisProject(proj);
		proj.addTask(newTask);
		
		this.taskService.saveTask(newTask);
		model.addAttribute("thisProject", proj);
		model.addAttribute("tasks",proj.getProjectTasks());
		return "redirect:/projectPage" +"/" + proj.getId().toString();
	}
	
	@RequestMapping(value="/deleteTask/{id}", method=RequestMethod.GET)
	public String deleteTask(@PathVariable("id") Long id, Model model) {
		Task task=this.taskService.findById(id);
		Project thisProject = task.getThisProject();
		this.taskService.deleteTask(task);
		model.addAttribute("thisProject",thisProject);
		model.addAttribute("tasks",thisProject.getProjectTasks()); //could throw LazyExcept
		return "redirect:/projectPage" +"/" + thisProject.getId().toString();
	}

	//think about ways to avoid needing userService here
	@RequestMapping(value="/assignTask/{id}", method=RequestMethod.POST)
	public String assignTask(Model model,
			@PathVariable("id") Long id,
			@RequestParam("username") String memberUsername) {
		
		Task task=this.taskService.findById(id);
		Project project = task.getThisProject();
		Utente assignTo = this.userService.getByUsername(memberUsername); 
		List<Utente> members = this.userService.getByVisibleProjects(project);
		
		if(assignTo == null || !(members.contains(assignTo))) { 
			model.addAttribute("couldNotAssign", "this User is not a member of the Project");
		}
		else {
			task.setAssignedTo(assignTo); 
			this.taskService.saveTask(task);
			model.addAttribute("assigned", "Task Assigned to" + memberUsername);
		}
		model.addAttribute("thisProject",project);
		model.addAttribute("tasks",project.getProjectTasks());
		
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
		model.addAttribute("thisTask",t);
		return "redirect:/taskPage" + "/" + t.getId().toString();
	}
}
