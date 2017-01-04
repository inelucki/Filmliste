package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotEmpty(message="Username is required.")
	@Column(unique=true, nullable=false)
	private String name;
    private String password;
    
    protected User(){}
    
    public User(String name, String password){
    	this.name = name;
    	this.password = password;
    }

    public Long getId(){
    	return id;
    }
    
	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
}
