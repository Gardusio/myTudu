package my.spring.siw.tud.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Utente {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	//@Column(nullable = false, length = 100)
	private String name;
	//@Column(nullable = false, length = 100)
	private String surname;
	
	private String email;
	
	private boolean isRegisteredWithOAuth; // support field to reduce the security-bug with OAuth
	
	//@Column(updatable = false, nullable = false)
	private LocalDateTime creationTime;
	//@Column(nullable = false)
	private LocalDateTime lastUpdateTime;
	
	@OneToMany(mappedBy = "owner",
			fetch = FetchType.LAZY,
			cascade = CascadeType.REMOVE)
	private List<Project> ownedProjects; 
	
	@ManyToMany(mappedBy = "members",
			fetch = FetchType.LAZY)
	private List<Project> visibleProjects;
	
	@OneToMany(mappedBy ="assignedTo",
			fetch = FetchType.LAZY)
	private List<Task> assignments;
	
	
	
	public Utente() {
		this.ownedProjects = new ArrayList<Project>();
		this.visibleProjects = new ArrayList<Project>();
	}

	public Utente(String name,String surname,String username,String pw) {
		this();
		this.name=name;
		this.surname=surname;
	}
	
	
	/*
	 * OnPersist - onUpdate methods 
	 */
	@PrePersist // would it change the time on merge?
	protected void onPersist() {
		this.creationTime = LocalDateTime.now();
		this.lastUpdateTime = LocalDateTime.now();
	}
	@PreUpdate
	protected void onUpdate() {
		this.lastUpdateTime = LocalDateTime.now();
	}
	
	
	/*
	 * Business Logic methods
	 */
	public void addNewOwned(Project nuovoP) { this.ownedProjects.add(nuovoP); }
	
	public void addNewVisible(Project project) { this.visibleProjects.add(project); }
	
	//TODO
	
	
	
	/*
	 * Getter n Setter
	 */
	public Long getId() { return id; }
	public LocalDateTime getCreationTime() { return creationTime; }
	public LocalDateTime getLastUpdateTime() { return lastUpdateTime; } 
	public String getName() { return name; }
	public List<Project> getOwnedProjects() { return ownedProjects;}
	public String getSurname() { return surname; }
	public List<Project> getVisibleProjects() { return visibleProjects;}
	public List<Task> getAssignments() { return assignments; }
	public boolean getRegisteredWithOAuth() {return this.isRegisteredWithOAuth;}
	public String getEmail() {
		return email;
	}
	
	public void setName(String name) { this.name = name; }
	public void setOwnedProjects(List<Project> ownedProjects) { this.ownedProjects = ownedProjects; }
	public void setSurname(String surname) {this.surname = surname; }
	public void setVisibleProjects(List<Project> visibleProjects) { this.visibleProjects = visibleProjects; }
	public void setAssignments(List<Task> assignments) { this.assignments = assignments; }
	public void setRegisteredWithOAuth(boolean isRegisteredWithOAuth) {
		this.isRegisteredWithOAuth = isRegisteredWithOAuth;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	
	/*
	 * Equals,HashCode,ToString methods
	 */
	/*
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User: " + this.username);
		builder.append(", Account creation time: " + this.creationTime.toString());
		return builder.toString();
	}
	*/
	@Override
	public boolean equals(Object obj) {
		Utente that = (Utente) obj;
		if(that==null)
			return false;
		return this.name.equals(that.getName()) && this.surname.equals(that.getSurname()) && this.creationTime.equals(that.getCreationTime());
	}
	@Override
	public int hashCode() {
		return this.name.hashCode() + this.surname.hashCode() + this.creationTime.hashCode();
	}
	

}
