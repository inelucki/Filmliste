package main.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Filmliste extends RequestStatus{
	private final List<Film> liste;
	
	@JsonCreator
	public Filmliste(boolean statusOK, String errormessage,
			@JsonProperty("liste") List<Film> liste){
		super(statusOK, errormessage);
		this.liste = liste;
	}
	
	public List<Film> getListe(){
		return liste;
	}
}
