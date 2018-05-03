package snake.server.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import snake.server.model.comm.Game;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int id;

	@Column(unique=false, nullable=false, length=20)
	public String name;
	
	public int wins = 0;
	
	public int losses = 0;
	
	//@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JsonIgnore
	@OneToMany(orphanRemoval=true)
	public List<Game> games;
	
	public User() {}
	
	public User(String name){
		this.name = name;
	}
	
}