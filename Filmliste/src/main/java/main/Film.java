package main;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Film extends ResourceSupport {

	@Id
	private String name;
    private String content;

    protected Film(){}
    
    @JsonCreator
    public Film(@JsonProperty("name") String name, @JsonProperty("content") String content) {
        this.content = content;
        this.name = name;
    }

    public String getContent() {
        return content;
    }
    
    public String getName(){
    	return name;
    }
}