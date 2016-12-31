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
	
	public ResponseObject createFilm(String s){
		//TODO
		return null;
	}
	
	public ResponseObject updateFilm(Film f, String content){
		//TODO
				return null;
	}
	
	public ResponseObject deleteFilm(Film f){
		//TODO
				return null;
	}
	
	public ResponseObject createTag(String s){
		//TODO
				return null;
	}
	
	public ResponseObject deleteTag(String s){
		//TODO
				return null;
	}
	
	public ResponseObject deleteTagFromFilm(Film f, String s){
		//TODO
				return null;
	}
	
	public ResponseObject addTagToFilm(Film f, String s){
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

//@Override
//public void start(Stage primaryStage) {
//  primaryStage.setTitle("Client");
//  
//  Button btnPut = new Button("Film hinzufuegen");
//  btnPut.setOnAction(f->{
//  	JSONObject json = new JSONObject();
//  	json.put("content", descriptionfield.getText());
//  	String response = HttpHandler.sendRequestWithPayload("filme/"+textfield.getText(), "PUT", json.toString());
//  	textarea.clear();
//  	textarea.setText(response);
//  });
//  Button btnDelete = new Button("Film loeschen");
//  btnDelete.setOnAction(f->{
//  	String response = HttpHandler.sendSimpleRequest("filme/"+textfield.getText(), "DELETE");
//  	textarea.clear();
//  	textarea.setText(response);
//  });
//  Button btnGetAll = new Button("alle Filme auslesen");
//  btnGetAll.setOnAction(f->{
//  	String response = HttpHandler.sendSimpleRequest("filmliste", "GET");
//  	textarea.clear();
//  	textarea.setText(response);
//  });
//  
//  StackPane root = new StackPane();
//  VBox vbox = new VBox();
//  vbox.getChildren().addAll(textfield, descriptionfield, btnPut, btnDelete, btnGetAll, textarea);
//  root.getChildren().add(vbox);
//  primaryStage.setScene(new Scene(root, 300, 250));
//  primaryStage.show();
//}
