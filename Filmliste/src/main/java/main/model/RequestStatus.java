package main.model;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestStatus extends ResourceSupport{

	private boolean statusOK;
	private String errormessage;
	
	@JsonCreator
	public RequestStatus(@JsonProperty("statusOK") boolean statusOK, @JsonProperty("errormessage") String errormessage){
		this.statusOK = statusOK;
		this.errormessage = errormessage;
	}
	
	public RequestStatus(){}
	
	public boolean getStatusOK(){
		return statusOK;
	}
	
	public String getErrormessage(){
		return errormessage;
	}
	
	public void setErrormessage(String errormessage){
		this.errormessage = errormessage;
	}
	
	public void setStatusOK(boolean statusOK){
		this.statusOK = statusOK;
	}
}
