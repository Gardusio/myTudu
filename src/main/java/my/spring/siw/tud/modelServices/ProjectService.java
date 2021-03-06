package my.spring.siw.tud.modelServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import my.spring.siw.tud.model.Project;
import my.spring.siw.tud.model.Task;
import my.spring.siw.tud.model.Utente;
import my.spring.siw.tud.persistence.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepo;
	
	@Transactional
	public Project saveProject(Project p) {
		return this.projectRepo.save(p); //do not throws DataIntegrityViolation, there's no unique field in Project
	}
	
	
	
	@Transactional(readOnly=true)
	public Project getProjectById(Long id) {
		Optional<Project> retrieved = this.projectRepo.findById(id);
		return retrieved.orElse(null);
	}
	
	@Transactional
	public void delete(Project p) {this.projectRepo.delete(p);}
	
	@Transactional
	public void deleteById(Long id) {this.projectRepo.deleteById(id);}
	
	@Transactional
	public void addMember(Project p, Utente newMember) {
		p.addMember(newMember);
		this.saveProject(p);
	}
	
	@Transactional(readOnly=true)
	public List<Project> getProjectsByMember(Utente member) {
		return this.projectRepo.findByMembers(member);
	}
	
	@Transactional(readOnly=true)
	public List<Project> getProjectsByOwner(Utente member) {
		return this.projectRepo.findByOwner(member);
	}
	
	@Transactional(readOnly=true)
	public List<Project> findByOwner(Utente current) {
		return this.projectRepo.findByOwner(current);
	}
	
	@Transactional(readOnly=true)
	public List<Project> findByMembers(Utente member) {
		return this.projectRepo.findByMembers(member);
	}
	
	@Transactional(readOnly=true)
	public Project findById(Long id) {
		Optional<Project> retrieved = this.projectRepo.findById(id);
		return retrieved.orElse(null);
	}
	
	
	public Map<Project,Integer> getProjectToTaskAssigned(List<Project> projects, Utente member) {
		Map<Project,Integer> projectToTaskAssigned = new HashMap<Project,Integer>();
		
		for(Project project : projects) {
			List<Task> assignedInProject = project.getProjectTasks().stream()
									.filter(t -> t.getAssignedTo()!=null 
												 && t.getAssignedTo().equals(member))
									.collect(Collectors.toList());
			projectToTaskAssigned.put(project,assignedInProject.size());
		}
		
		return projectToTaskAssigned;
	}



	public List<Task> getTaskAssigned(Project p , Utente member ) {
		List<Task> assigned = new ArrayList<Task>();
		
		assigned = p.getProjectTasks().stream()
				   .filter(t-> t.getAssignedTo()!=null && t.getAssignedTo().equals(member))
				   .collect(Collectors.toList());
		
		return assigned;
	}
	
	/*Estensione
	 * 

	@Transactional
	public void addTag(Project p,Tag t) {
		p.addTag(t);
		this.projectRepo.save(p);
	}
	*/
	
}
