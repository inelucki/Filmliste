package main.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Film extends RequestStatus {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idNumber;
	private String name;
    private String content;
    @ElementCollection
    private List<Long> pictures;
    @ElementCollection
    private List<Long> tags;

    protected Film(){}
    
    @JsonCreator
    public Film(boolean statusOK, String errormessage, @JsonProperty("idNumber") Long idNumber,
    			@JsonProperty("name") String name, @JsonProperty("content") String content,
    			@JsonProperty("pictures") List<Long> pictures, @JsonProperty("tags") List<Long> tags) {
    	super(statusOK, errormessage);
    	this.idNumber = idNumber;
        this.content = content;
        this.name = name;
        this.pictures = pictures;
        this.tags = tags;
    }
    
    public Film(String name) {
	    this.name = name;
	    pictures = new LinkedList<>();
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
    
    public List<Long> getTags(){
    	return tags;
    }
    
    public List<Long> getPictures(){
    	return pictures;
    }
    
    public void setContent(String content){
    	this.content=content;
    }
    
	public void setTags(List<Long> tags){
    	this.tags=tags;
    }

	public void setPictures(List<Long> pictures){
		this.pictures=pictures;
	}
	
	public void setIdNumber(Long idNumber){
		this.idNumber=idNumber;
	}
}