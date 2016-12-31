package main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Tag extends ResourceSupport {

	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idNumber;
	@Id
	private String name;

    protected Tag(){}
    
    @JsonCreator
    public Tag(@JsonProperty("name") String name, @JsonProperty("idNumber") Long idNumber) {
        this.name = name;
        this.idNumber = idNumber;
    }
    
    public Tag(String name){
    	this.name = name;
    }
    
    public Long getIdNumber(){
    	return idNumber;
    }
    
    public String getName(){
    	return name;
    }
}