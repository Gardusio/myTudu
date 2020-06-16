package my.spring.siw.tud.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Project {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	//@Column(nullable = false, length = 252)
	private String name;
	//@Column(length = 252)
	private String description;
	//@Column(nullable = false)
	private boolean isActive;
	//@Column(updatable = false, nullable = false)
	private LocalDateTime creationTime;
	//@Column(nullable = false)
	private LocalDateTime lastUpdateTime;

	@ManyToOne(fetch = FetchType.EAGER)
	private Utente owner;

	@ManyToMany()
	private List<Utente> members;

	@OneToMany(mappedBy = "project",
			cascade = CascadeType.REMOVE,
			fetch = FetchType.LAZY)
	private List<Task> projectTasks;

	/* estensione
	@OneToMany(
			cascade = CascadeType.REMOVE,
			fetch = FetchType.LAZY)
	private List<Tag> projectTags;
	 */
	
	public Project() {
		this.members = new ArrayList<Utente>();
		this.projectTasks = new ArrayList<Task>();
		this.isActive=true;
		//this.projectTags= new ArrayList<Tag>();
	}
	
	public Project(String name, String description) {
		this();
		this.name = name;
		this.description = description;
		this.isActive=true;
		//this.projectTags= new ArrayList<Tag>();
	}
	
	
	/*
	 * OnPersist - OnUpdate methods
	 */
	@PrePersist
	protected void onPersist() {
		this.creationTime = LocalDateTime.now();
		this.lastUpdateTime = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.lastUpdateTime = LocalDateTime.now();
	}

	/*
	 * Business Logic methods TODO
	 */
	public void addMember(Utente member) {
		this.members.add(member);
	}
	
	public void addTask(Task newTask) {
		this.projectTasks.add(newTask);
	}

	/*
	public void addTag(Tag tag) {
		this.projectTags.add(tag);
	}
	*/
	
	//TODO
	//deleteTag
	//deleteMember
	
	/*
	 * Getter n Setter
	 */
	public String getDescription() { return description; }
	public Long getId() {return id;}
	public List<Utente> getMembers() {return members;}
	public String getName() { return name; }
	public Utente getOwner() { return owner; }
	public List<Task> getProjectTasks() { return projectTasks; }
	//public List<Tag> getProjectTags() { return projectTags; }
	public LocalDateTime getCreationTime() { return creationTime; }
	public LocalDateTime getLastUpdateTime() { return lastUpdateTime; }
	public boolean getIsActive() {return this.isActive;}
	
	public void setDescription(String description) { this.description = description; }
	public void setMembers(List<Utente> members) { this.members = members; }
	public void setName(String name) { this.name = name; } 
	public void setOwner(Utente owner) { this.owner = owner; }
	public void setProjectTasks(List<Task> projectTasks) { this.projectTasks = projectTasks; }
	//public void setProjectTags(List<Tag> projectTags) { this.projectTags = projectTags; }
	public void setActive(boolean isActive) {this.isActive = isActive; }
	
	/* 
	@Override
	public String toString() { 	

	}
	 */

	@Override
	public boolean equals(Object obj) {
		Project that = (Project) obj;
		return this.name.equals(that.getName()); //&& this.description.equals(that.getDescription) && this.creationTime.equals(that.getCreationTime());
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() + this.description.hashCode() + this.creationTime.hashCode() + this.lastUpdateTime.hashCode();
	}


}
