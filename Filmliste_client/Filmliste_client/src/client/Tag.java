package client;

public class Tag {

	private final String tag;
	private final int id;
	
	protected Tag(String t, int id){
		tag = t;
		this.id = id;
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
	
	public int getId(){
		return id;
	}
	
}
