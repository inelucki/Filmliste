package client;

import java.util.LinkedList;
import java.util.List;

public class Film {

	private List<Tag> tags = new LinkedList<>();
	private final String name;
	private final int id;
	private String content="";
	
	public Film(String name, int id){
		this.name = name;
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String c){
		content = c;
	}
	
	public void addTag(Tag t){
		tags.add(t);
	}
	
	public boolean hasTag(Tag t){
		return tags.contains(t);
	}
	
	public List<Tag> getTags(){
		return tags;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Film){
			return ((Film) o).getName().equals(this.getName());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.getName().hashCode();
	}
}
