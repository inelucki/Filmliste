package main.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TagListe  extends RequestStatus{
	private final List<Tag> liste;
	
	@JsonCreator
	public TagListe(boolean statusOK, String errormessage,
					@JsonProperty("liste") List<Tag> liste){
		super(statusOK, errormessage);
		this.liste = liste;
	}
	
	public List<Tag> getListe(){
		return liste;
	}
}
