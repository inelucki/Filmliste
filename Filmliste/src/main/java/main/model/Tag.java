package main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Tag extends RequestStatus {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idNumber;
	private String name;

    protected Tag(){}
    
    @JsonCreator
    public Tag(boolean statusOK, String errormessage,
    			@JsonProperty("name") String name, @JsonProperty("idNumber") Long idNumber) {
    	super(statusOK, errormessage);
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