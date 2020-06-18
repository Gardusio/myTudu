package my.spring.siw.tud.model;

import javax.persistence.*;

@Entity
public class Tag {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	//@Column(nullable = false)
	private String name;
	
	//@Column(nullable = false)
	private String color;
	
	//@Column(nullable = false)
	private String description;
	
	@ManyToOne()
	private Utente user;
	
	
	public Utente getUser() {
		return user;
	}
	public String getColor() { return color; }
	public String getDescription() { return description; }
	public String getName() { return name; }
	public Long getId() {
		return id;
	}
	
	public void setColor(String color) { this.color = color;}
	public void setDescription(String description) { this.description = description; }
	public void setName(String name) { this.name = name; }
	public void setUser(Utente user) {
		this.user = user;
	}
	
	@Override
	public boolean equals(Object obj) {
		Tag that = (Tag) obj;
		return this.name.equals(that.getName()) && this.color.equals(that.getColor());
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode() + this.color.hashCode();
	}
	
}
