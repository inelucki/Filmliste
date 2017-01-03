package client;

import java.util.LinkedList;
import java.util.List;

public class Film {

	private List<Tag> tags = new LinkedList<>();
	private List<PictureData> pictures	= new LinkedList<>();
	private final String name;
	private final Long id;
	private final String linkToDelete;
	private final String linkToUpdate;
	private final String linkToUpload;
	private String content="";
	
	public Film(String name, Long id, String updateLink, String deleteLink, String linkToUpload){
		this.name = name;
		this.id = id;
		this.linkToDelete = deleteLink;
		this.linkToUpdate = updateLink;
		this.linkToUpload = linkToUpload;
	}
	
	public String getName(){
		return name;
	}
	
	public Long getId(){
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

	public String getLinkToUpdate() {
		return linkToUpdate;
	}

	public String getLinkToDelete() {
		return linkToDelete;
	}
	
	public String getLinkToUpload(){
		return linkToUpload;
	}

	public List<PictureData> getPictures() {
		return pictures;
	}

	public void setPictures(List<PictureData> pictures) {
		this.pictures = pictures;
	}
}
