package my.spring.siw.tud.model;

import java.time.LocalDateTime;
//import java.util.*;

import javax.persistence.*;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	//@Column(nullable = false)
	private boolean isCompleted;
	//@Column(nullable = false, length = 10)
	private String name;
	//@Column(length = 252)
	private String description;
	//@Column(updatable = false, nullable = false)
	private LocalDateTime creationTime;
	//@Column(nullable = false)
	private LocalDateTime lastUptadeTime;

	@ManyToOne(fetch=FetchType.EAGER)
	private Project project;
	
	@ManyToOne
	private Utente assignedTo; // user u has to be in this.project.members

	/*
	 * estensione

	@OneToMany(//will this be bidirectional? :mappedBy = " "
			//if so, would this mean that tags are assignable just to specific tasks?
			fetch = FetchType.EAGER,
			cascade = CascadeType.REMOVE)
	private List<Tag> taskTags;

	
	@OneToMany
	@JoinColumn(name = "task")
	private List<Comment> taskComments;
*/

	public Task() {
		//this.taskComments = new ArrayList<Comment>();
		//this.taskTags = new ArrayList<Tag>();
	}

	public Task(String name,String desc) {
		//this();
		this.name = name;
		this.description = desc;
		this.isCompleted = false;
	}
	
	public Task(String name,String desc,Project thisProject) {
		this(name,desc);
		this.project = thisProject;
	}

	/*
	 * OnPersist - onUpdate methods
	 */
	@PrePersist
	protected void onPersist() {
		this.creationTime = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUptade() {
		this.lastUptadeTime = LocalDateTime.now();
	}
	
	/*
	 * Business Logic Methods
	 */
	
	//TODO
	/*
	 * estensione
	 
	public void addTag(Tag tag) {
		this.taskTags.add(tag);
	}
	
	public void addComment(Comment c) {
		this.taskComments.add(c);	
	}
	*/
	

	/*
	 * Getters n Setters
	 */
	public LocalDateTime getCreationTime() { return creationTime; }
	public String getDescription() { return description; }
	public Long getId() { return id; }
	public String getName() { return name; }
	public boolean getIsCompleted() { return this.isCompleted; }
	public LocalDateTime getLastUptadeTime() { return lastUptadeTime; }
	//public List<Tag> getTaskTags() { return taskTags; }
	public Project getThisProject() { return project; }
	public Utente getAssignedTo() { return assignedTo; }
	//public List<Comment> getTaskComments() { return taskComments; }
	
	public void setCompleted(boolean isCompleted) { this.isCompleted = isCompleted; }
	public void setCreationTime(LocalDateTime creationTime) { this.creationTime = creationTime; }
	public void setDescription(String description) { this.description = description; }
	public void setLastUptadeTime(LocalDateTime lastUptadeTime) { this.lastUptadeTime = lastUptadeTime; }
	public void setName(String name) { this.name = name; }
	//public void setTaskTags(List<Tag> taskTags) { this.taskTags = taskTags; }
	public void setThisProject(Project thisProject) { this.project = thisProject; }
	public void setAssignedTo(Utente assignedTo) { this.assignedTo = assignedTo; }
	//public void setTaskComments(List<Comment> taskComments) { this.taskComments = taskComments; }
	
	/*
	 * HashCode,Equals n ToString methods
	 */
	/*
	@Override
	public void toString() {
	}
	*/
	
	@Override
	public boolean equals(Object obj) {
		Task that = (Task) obj;
		return this.name.equals(that.getName()) && this.description.equals(that.getDescription()) && this.creationTime.equals(that.getCreationTime());
	}
	
	@Override
	public int hashCode() { return this.name.hashCode() + this.description.hashCode() + this.getCreationTime().hashCode();	}


}
