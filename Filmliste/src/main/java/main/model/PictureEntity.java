package main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class PictureEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idNumber;
	@Lob
	private byte[] pic;
	
	protected PictureEntity(){}
	
	public PictureEntity(byte[] bytes){
		pic = bytes;
	}
	
	public void setPic(byte[] pic){
		this.pic = pic;
	}
	
	public byte[] getPic(){
		return pic;
	}

	public Long getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Long idNumber) {
		this.idNumber = idNumber;
	}
}
