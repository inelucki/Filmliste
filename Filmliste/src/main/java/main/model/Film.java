package main.model;

import java.util.List;

import javax.persistence.ElementCollection;
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
    @ElementCollection
    private List<String> pictures;
    @ElementCollection
    private List<Tag> tags;

    protected Film(){}
    
    @JsonCreator
    public Film(@JsonProperty("name") String name, @JsonProperty("content") String content,
    			@JsonProperty("pictures") List<String> pictures, @JsonProperty("tags") List<Tag> tags) {
        this.content = content;
        this.name = name;
        this.pictures = pictures;
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }
    
    public String getName(){
    	return name;
    }
    
    public List<Tag> getTags(){
    	return tags;
    }
    
    public List<String> getPictures(){
    	return pictures;
    }
}