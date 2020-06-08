package my.spring.siw.tud.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Credentials {
	
	public static final String DEFAULT_ROLE = "DEFAULT";
	public static final String ADMIN_ROLE = "ADMIN";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true, nullable = false, length = 100)
	private String username;
	@Column(nullable = false, length = 100)
	private String password;
	@Column(nullable = false)
	private String role;
	
	@OneToOne(fetch = FetchType.EAGER,
			cascade = CascadeType.ALL)
	private Utente user;
	
	
	public Utente getUser() { return user;}
	public String getRole() { return role; }
	public String getUsername() { return username; }
	public Utente getUtente() { return user; }
	
	public void setRole(String role) { this.role = role; }
	public void setUsername(String username) { this.username = username; } 
	public void setPassword(String password) { this.password = password; }
	public void setUser(Utente user) { this.user = user; }
	public String getPassword() { return this.password; }

}
