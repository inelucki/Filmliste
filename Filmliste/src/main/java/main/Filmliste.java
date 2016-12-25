package main;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Filmliste extends ResourceSupport{
	private final List<String> liste;
	
	@JsonCreator
	public Filmliste(@JsonProperty("liste") List<String> liste){
		this.liste = liste;
	}
	
	public List<String> getListe(){
		return liste;
	}
}
