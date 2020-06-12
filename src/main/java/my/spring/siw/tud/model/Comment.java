package my.spring.siw.tud.model;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Long natId;
	
	//@Column(nullable = false, length=252)
	private String text;

	//@Column(nullable = false, updatable=false)
	private LocalDateTime creationTime;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Credentials userCred;
	
	@ManyToOne()
	private Task task;
	
	public Comment() {}
	
	public Comment(String newCommentText) {
		this.text = newCommentText;
	}

	@PrePersist
	protected void onPersist() {
		this.creationTime = LocalDateTime.now();
	}
	

	
	public Task getTask() {
		return task;
	}
	
	public Credentials getUserCred() {
		return this.userCred;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getNatId() {
		return natId;
	}
	
	public String getText() {
		return text;
	}
	
	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	
	public void setNatId(Long natId) {
		this.natId = natId;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
	public void setUserCred(Credentials userCred) {
		this.userCred = userCred;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		Comment that = (Comment) obj;
		return this.natId==that.getNatId() && this.text.equals(that.getText());
	}
	
	@Override
	public int hashCode() {
		return this.natId.hashCode() + this.text.hashCode();
	}
	

}
