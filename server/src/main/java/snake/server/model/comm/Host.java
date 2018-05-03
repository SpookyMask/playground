package snake.server.model.comm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import snake.server.model.User;

@Entity
public class Host {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonIgnore
	public long id;
	
	@Column(unique=true, nullable=false, length=20)
	public String name;
	
	@OneToOne
	public Settings settings;
	
	public Host() {}
	
	public Host(String name, Settings settings){
		this.name = name;
		this.settings = settings;
	}
}
