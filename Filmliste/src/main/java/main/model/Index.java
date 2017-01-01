package main.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Index extends RequestStatus{

	private final String filmanzahl;
	private final String taganzahl;
	
	@JsonCreator
	public Index(boolean statusOK, String errormessage,
				@JsonProperty("filmanzahl") String filmanzahl, @JsonProperty("taganzahl") String taganzahl){
		super(statusOK, errormessage);
		this.filmanzahl = filmanzahl;
		this.taganzahl = taganzahl;
	}
	
	public String getFilmanzahl(){
		return filmanzahl;
	}
	
	public String getTaganzahl(){
		return taganzahl;
	}
}
