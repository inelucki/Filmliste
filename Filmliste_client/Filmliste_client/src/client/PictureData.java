package client;

import javafx.scene.image.Image;

public class PictureData {

	private final Long numberID;
	private final String link;
	private Image data = null;
	
	public PictureData(Long numberID, String link){
		this.numberID=numberID;
		this.link=link;
	}
	
	public long getNumberID(){
		return numberID;
	}
	
	public String getLink(){
		return link;
	}

	public Image getData() {
		return data;
	}

	public void setData(Image data) {
		this.data = data;
	}
	
	@Override
	public int hashCode(){
		return numberID.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof PictureData){
			return ((PictureData) o).getNumberID() == this.numberID;
		}
		else{return false;}
	}
}
