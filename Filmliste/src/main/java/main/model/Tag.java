package main.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Tag extends ResourceSupport {

	@Id
	private String name;

    protected Tag(){}
    
    @JsonCreator
    public Tag(@JsonProperty("name") String name) {
        this.name = name;
    }
    
    public String getName(){
    	return name;
    }
}