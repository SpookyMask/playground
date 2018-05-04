package snake.server.model.comm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	@GeneratedValue
	public int id;

	@Column(unique=true, nullable=false, length=20)
	public String name;
	
	public int wins = 0;
	
	public int losses = 0;
	
	//@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
//	@JsonIgnore
//	@OneToMany(orphanRemoval=true)
//	public List<Game> games;
	
	public User() {}
	
	public User(String name){
		this.name = name;
	}
	
}
