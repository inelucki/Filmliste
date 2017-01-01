package client;

public class Tag {

	private final String tag;
	private final Long id;
	private final String linkToDelete;
	
	protected Tag(String t, Long id, String link){
		tag = t;
		this.id = id;
		this.linkToDelete=link;
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
	
	public Long getId(){
		return id;
	}
	
	public String getLinkToDelete(){
		return linkToDelete;
	}
}
