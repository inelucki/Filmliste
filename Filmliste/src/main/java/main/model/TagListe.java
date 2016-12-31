package main.model;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TagListe  extends ResourceSupport{
	private final List<Tag> liste;
	
	@JsonCreator
	public TagListe(@JsonProperty("liste") List<Tag> liste){
		this.liste = liste;
	}
	
	public List<Tag> getListe(){
		return liste;
	}
}
