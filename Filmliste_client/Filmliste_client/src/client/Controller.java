package client;

import java.util.LinkedList;
import java.util.List;

public class Controller {

	private List<Film> filme = new LinkedList<>();
	
	public List<Film> getFilme(){
		return filme;
	}
	
	public Controller(){
		dummydata();
		//TODO init Data
	}
	
	public String createFilm(String s){
		//TODO
		return null;
	}
	
	public String updateFilm(Film f, String content){
		//TODO
				return null;
	}
	
	public String deleteFilm(Film f){
		//TODO
				return null;
	}
	
	public String createTag(String s){
		//TODO
				return null;
	}
	
	public String deleteTag(String s){
		//TODO
				return null;
	}
	
	public String deleteTagFromFilm(String s){
		//TODO
				return null;
	}
	
	public String addTagToFilm(String s){
		//TODO
				return null;
	}
	
	private void dummydata(){
		Tag a = Tag.getTag("Action");
		Tag b = Tag.getTag("comedy");
		Tag c = Tag.getTag("uhu");
		
		Film d = new Film("Hugo", 1);
		d.addTag(a);
		d.addTag(c);
		d.setContent("Sein Name der war Hugo");

		Film e = new Film("Blond", 2);
		e.addTag(a);
		e.addTag(b);
		e.setContent("Sein Name der war Blond");
		
		Film f = new Film("Doof", 3);
		f.addTag(b);
		f.addTag(c);
		f.setContent("Sein Name der war Doof");
		
		Film g = new Film("Blasssss", 4);
		g.addTag(c);
		g.addTag(c);
		g.setContent("Sein Name der war Blasss");
		
		filme.add(d);
		filme.add(e);
		filme.add(f);
		filme.add(g);
	}
	
}
