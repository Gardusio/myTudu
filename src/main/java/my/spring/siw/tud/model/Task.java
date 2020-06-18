package my.spring.siw.tud.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
//import java.util.*;
import java.util.List;

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
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Utente assignedTo;

	/*
	@OneToMany(
			fetch = FetchType.LAZY,
			cascade = CascadeType.REMOVE)
	private List<Tag> taskTags;
	*/

	@OneToMany(mappedBy="task", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<Comment> comments;


	public Task() {
		this.comments = new ArrayList<Comment>();
		//this.taskTags = new ArrayList<Tag>();
	}

	public Task(String name,String desc) {
		this();
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
	
	/*
	public void addTag(Tag tag) {
		this.taskTags.add(tag);
	}
	*/
	
	public void addComment(Comment c) {
		this.comments.add(c);	
	}

	/*
	 * Getters n Setters
	 */
	public LocalDate getCreationTime() { return creationTime.toLocalDate(); }
	public String getDescription() { return description; }
	public Long getId() { return id; }
	public String getName() { return name; }
	public boolean getIsCompleted() { return this.isCompleted; }
	public LocalDateTime getLastUptadeTime() { return lastUptadeTime; }
	//public List<Tag> getTaskTags() { return taskTags; }
	public Project getProject() { return project; }
	public Utente getAssignedTo() { return assignedTo; }
	public List<Comment> getComments() { return comments; }
	
	public void setCompleted(boolean isCompleted) { this.isCompleted = isCompleted; }
	public void setCreationTime(LocalDateTime creationTime) { this.creationTime = creationTime; }
	public void setDescription(String description) { this.description = description; }
	public void setLastUptadeTime(LocalDateTime lastUptadeTime) { this.lastUptadeTime = lastUptadeTime; }
	public void setName(String name) { this.name = name; }
	//public void setTaskTags(List<Tag> taskTags) { this.taskTags = taskTags; }
	public void setProject(Project thisProject) { this.project = thisProject; }
	public void setAssignedTo(Utente assignedTo) { this.assignedTo = assignedTo; }
	public void setTaskComments(List<Comment> taskComments) { this.comments = taskComments; }
	
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
