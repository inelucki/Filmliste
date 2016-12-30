package client;

import java.util.LinkedList;
import java.util.List;

public class Tag {

	private static List<Tag> tags = new LinkedList<Tag>();
	private final String tag;
	
	protected Tag(String t){
		tag = t;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Tag){
			return ((Tag) o).getTag().equals(tag);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return tag.hashCode();
	}
	
	public String getTag(){
		return tag;
	}
	
	public static Tag getTag(String s){
		Tag n = new Tag(s);
		if(tags.contains(n)){
			return tags.get(tags.indexOf(n));
		}
		else{
			tags.add(n);
			return n;
		}
	}
	
	public static List<Tag> getAllTags(){
		return tags;
	}
	
}
