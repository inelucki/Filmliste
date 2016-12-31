package main.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Film extends ResourceSupport {

	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idNumber;
	@Id
	private String name;
    private String content;
    @ElementCollection
    private List<String> pictures;
    @ElementCollection
    private List<Tag> tags;

    protected Film(){}
    
    @JsonCreator
    public Film(@JsonProperty("idNumber") Long idNumber,
    			@JsonProperty("name") String name, @JsonProperty("content") String content,
    			@JsonProperty("pictures") List<String> pictures, @JsonProperty("tags") List<Tag> tags) {
    	this.idNumber = idNumber;
        this.content = content;
        this.name = name;
        this.pictures = pictures;
        this.tags = tags;
    }
    
    public Film(String name) {
	    this.name = name;
	}

    public Long getIdNumber(){
    	return idNumber;
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
    
    public void setContent(String content){
    	this.content=content;
    }
    
	public void setTags(List<Tag> tags){
    	this.tags=tags;
    }

	public void setPictures(List<String> pictures){
		this.pictures=pictures;
	}
}