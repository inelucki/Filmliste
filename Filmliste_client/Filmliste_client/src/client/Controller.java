package client;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

public class Controller {

	private static final String HOSTINDEX = "http://localhost:8080/index";
	private String linkToFilmlist="";
	private String linkToTaglist="";
	private List<Film> filme = new LinkedList<>();
	private List<Tag> tags = new LinkedList<>();
	
	public List<Tag> getTags(){
		return tags;
	}
	
	public List<Film> getFilme(){
		return filme;
	}
	
	public boolean existTag(String s){
		return tags.stream().filter(f -> f.getTag().equals(s)).findFirst().isPresent();
	}
	
	public Tag getTag(String s){
		return tags.stream().filter(f -> f.getTag().equals(s)).findFirst().get();
	}
	
	public ResponseObject init(){
		ResponseObject resp;
		
		JSONObject baseinfo = HttpHandler.sendSimpleRequest(HOSTINDEX, "GET");
		if(baseinfo.has("statusOK") && baseinfo.getBoolean("statusOK")){
			if(baseinfo.has("_links")){
				JSONObject links = baseinfo.getJSONObject("_links");
				if(links.has("tagliste") && links.has("filmliste")){
					linkToFilmlist = links.getJSONObject("filmliste").getString("href");
					linkToTaglist = links.getJSONObject("tagliste").getString("href");
					
					ResponseObject respReadTagliste = readTagliste();
					if(respReadTagliste.isOk()){
						resp = readFilmliste();
					}
					else{resp = respReadTagliste;}
				}
				else{resp = new ResponseObject(false, "einer der links im index fehlt.", null);}
			}
			else{resp = new ResponseObject(false, "keine Links im Index enthalten.", null);}
		}
		else{resp = new ResponseObject(false, baseinfo.getString("errormessage"), null);}
		
		return resp;
	}
	
	private ResponseObject readFilmliste(){
		JSONObject baseinfo = HttpHandler.sendSimpleRequest(linkToFilmlist, "GET");
		if(baseinfo.has("statusOK") && baseinfo.getBoolean("statusOK")){
			//TODO
			System.out.println(baseinfo.toString());
			return new ResponseObject(true, null, null);
		}
		else{return new ResponseObject(false, baseinfo.getString("errormessage"), null);}
	}
	
	private ResponseObject readTagliste(){
		JSONObject baseinfo = HttpHandler.sendSimpleRequest(linkToTaglist, "GET");
		if(baseinfo.has("statusOK") && baseinfo.getBoolean("statusOK")){
			//TODO
			System.out.println(baseinfo.toString());
			return new ResponseObject(true, null, null);
		}
		else{return new ResponseObject(false, baseinfo.getString("errormessage"), null);}
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
		JSONObject req = new JSONObject();
		req.put("tag", s);
		JSONObject resp = HttpHandler.sendRequestWithPayload(linkToTaglist, "PUT", req);
		System.out.println(resp.toString());
		
		if(resp.has("statusOK") && resp.getBoolean("statusOK")){
			if(resp.has("name") && resp.has("idNumber") && resp.has("_links")){
				JSONObject links = resp.getJSONObject("_links");
				if(links.has("delete")){
					Tag tag = new Tag(resp.getString("name"), resp.getLong("idNumber"),
										links.getJSONObject("delete").getString("href"));
					tags.add(tag);
					return new ResponseObject(true, null, null);
				}
				else{return new ResponseObject(false, "der link zur resource fehlt in der antwort.", null);}
			}
			else{return new ResponseObject(false, "in der antwort waren nicht alle erwarteten felder enthalten.", null);}
		}
		else{return new ResponseObject(false, resp.getString("errormessage"), null);}
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
		Tag a = new Tag("Action", 1L, "");
		Tag b = new Tag("comedy", 2L, "");
		Tag c = new Tag("uhu", 3L, "");
		
		tags.add(a);
		tags.add(b);
		tags.add(c);
		
		Film d = new Film("Hugo", 1L, "", "");
		d.addTag(a);
		d.addTag(c);
		d.setContent("Sein Name der war Hugo");

		Film e = new Film("Blond", 2L, "", "");
		e.addTag(a);
		e.addTag(b);
		e.setContent("Sein Name der war Blond");
		
		Film f = new Film("Doof", 3L, "", "");
		f.addTag(b);
		f.addTag(c);
		f.setContent("Sein Name der war Doof");
		
		Film g = new Film("Blasssss", 4L, "", "");
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
