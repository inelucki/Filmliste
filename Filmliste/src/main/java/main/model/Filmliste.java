package main.model;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Filmliste extends ResourceSupport{
	private final List<Film> liste;
	
	@JsonCreator
	public Filmliste(@JsonProperty("liste") List<Film> liste){
		this.liste = liste;
	}
	
	public List<Film> getListe(){
		return liste;
	}
}
