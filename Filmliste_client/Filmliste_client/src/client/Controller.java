package client;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.image.Image;

import org.json.JSONArray;
import org.json.JSONObject;

public class Controller {

	private static final String HOSTINDEX = "https://localhost:8443/index";
	private Mapper mapper;
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
	
	public boolean existTag(Long l){
		return tags.stream().filter(f -> f.getId().equals(l)).findFirst().isPresent();
	}
	
	public Tag getTag(String s){
		return tags.stream().filter(f -> f.getTag().equals(s)).findFirst().get();
	}
	
	public Tag getTag(Long l){
		return tags.stream().filter(f -> f.getId().equals(l)).findFirst().get();
	}
	
	public ResponseObject init(){
		mapper = new Mapper(this);
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
						for(Film f : filme){
							ResponseObject ign = loadMissingPictures(f);
							if(!ign.isOk()){
								System.out.println(ign.getInfoMessage());
							}
						}
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
		System.out.println(baseinfo.toString());
		if(baseinfo.has("statusOK") && baseinfo.getBoolean("statusOK") && baseinfo.has("liste")){
			filme.clear();
			JSONArray colFilme = baseinfo.getJSONArray("liste");
			LinkedList<Film> tmpFilme = new LinkedList<>();
			for(int i = 0; i<colFilme.length(); i++){
				JSONObject jsonFilm = colFilme.getJSONObject(i);
				try{
					Film film = mapper.jsonToFilm(jsonFilm);
					tmpFilme.add(film);
				}
				catch(NullPointerException e){return new ResponseObject(false, e.getMessage(), null);}
			}
			filme.addAll(tmpFilme);
			return new ResponseObject(true, null, null);
		}
		else{return new ResponseObject(false, baseinfo.getString("errormessage"), null);}
	}
	
	private ResponseObject readTagliste(){
		JSONObject baseinfo = HttpHandler.sendSimpleRequest(linkToTaglist, "GET");
		if(baseinfo.has("statusOK") && baseinfo.getBoolean("statusOK") && baseinfo.has("liste")){
			tags.clear();
			JSONArray colTags = baseinfo.getJSONArray("liste");
			LinkedList<Tag> tmpTags = new LinkedList<>();
			for(int i = 0; i<colTags.length(); i++){
				JSONObject jsonTag = colTags.getJSONObject(i);
				try{
					Tag	tag = mapper.jsonToTag(jsonTag);
					tmpTags.add(tag);
				}
				catch(NullPointerException e){return new ResponseObject(false, e.getMessage(), null);}
			}
			tags.addAll(tmpTags);
			return new ResponseObject(true, null, null);
		}
		else{return new ResponseObject(false, baseinfo.getString("errormessage"), null);}
	}
	
	public ResponseObject createFilm(String s){
		JSONObject req = new JSONObject();
		req.put("film", s);
		JSONObject resp = HttpHandler.sendRequestWithPayload(linkToFilmlist, "PUT", req);
		System.out.println(resp.toString());
		
		if(resp.has("statusOK") && resp.getBoolean("statusOK")){
			try{
				Film film = mapper.jsonToFilm(resp);
				filme.add(film);
				return new ResponseObject(true, null, film);
			}
			catch(NullPointerException e){return new ResponseObject(false, e.getMessage(), null);}
		}
		else{return new ResponseObject(false, resp.getString("errormessage"), null);}
	}
	
	public ResponseObject updateFilm(Film f, String content){
		JSONObject req = mapper.filmToJson(f);
		if(req.has("content")){
			req.remove("content");
			req.put("content", content);
		}
		JSONObject resp = HttpHandler.sendRequestWithPayload(f.getLinkToUpdate(), "POST", req);
		System.out.println(resp.toString());
		
		if(resp.has("statusOK") && resp.getBoolean("statusOK")){
			f.setContent(content);
			return new ResponseObject(true, null, null);
		}
		else{return new ResponseObject(false, resp.getString("errormessage"), null);}
	}
	
	public ResponseObject deleteFilm(Film f){
		JSONObject resp = HttpHandler.sendSimpleRequest(f.getLinkToDelete(), "DELETE");
		System.out.println(resp.toString());
		
		if(resp.has("statusOK") && resp.getBoolean("statusOK")){
			filme.remove(f);
			return new ResponseObject(true, null, null);
		}
		else{return new ResponseObject(false, resp.getString("errormessage"), null);}
	}
	
	public ResponseObject createTag(String s){
		JSONObject req = new JSONObject();
		req.put("tag", s);
		JSONObject resp = HttpHandler.sendRequestWithPayload(linkToTaglist, "PUT", req);
		System.out.println(resp.toString());
		
		if(resp.has("statusOK") && resp.getBoolean("statusOK")){
			try{
				Tag tag = mapper.jsonToTag(resp);
				tags.add(tag);
				return new ResponseObject(true, null, null);
			}
			catch(NullPointerException e){return new ResponseObject(false, e.getMessage(), null);}
		}
		else{return new ResponseObject(false, resp.getString("errormessage"), null);}
	}
	
	public ResponseObject deleteTag(String s){
		Tag tag = getTag(s);
		JSONObject resp = HttpHandler.sendSimpleRequest(tag.getLinkToDelete(), "DELETE");
		System.out.println(resp.toString());
		
		if(resp.has("statusOK") && resp.getBoolean("statusOK")){
			tags.remove(tag);
			ResponseObject respRefreshFilme = readFilmliste();
			if(respRefreshFilme.isOk()){
				return new ResponseObject(true, null, null);
			}
			else{return respRefreshFilme;}
		}
		else{return new ResponseObject(false, resp.getString("errormessage"), null);}
	}
	
	public ResponseObject deleteTagFromFilm(Film f, String s){
		Tag tmp = getTag(s);
		f.getTags().remove(tmp);
		JSONObject req = mapper.filmToJson(f);
		JSONObject resp = HttpHandler.sendRequestWithPayload(f.getLinkToUpdate(), "POST", req);
		System.out.println(resp.toString());
		
		if(resp.has("statusOK") && resp.getBoolean("statusOK")){
			return new ResponseObject(true, null, null);
		}
		else{
			f.addTag(tmp);
			return new ResponseObject(false, resp.getString("errormessage"), null);
		}
	}
	
	public ResponseObject addTagToFilm(Film f, String s){
		JSONObject req = mapper.filmToJson(f);
		if(req.has("tags")){
			JSONArray filmtags = req.getJSONArray("tags");
			filmtags.put(mapper.tagToJson(getTag(s)));
		}
		JSONObject resp = HttpHandler.sendRequestWithPayload(f.getLinkToUpdate(), "POST", req);
		System.out.println(resp.toString());
		
		if(resp.has("statusOK") && resp.getBoolean("statusOK")){
			f.addTag(getTag(s));
			return new ResponseObject(true, null, null);
		}
		else{return new ResponseObject(false, resp.getString("errormessage"), null);}
	}
	
	public ResponseObject addPicture(Film film, File file){
		JSONObject resp = HttpHandler.sendPicture(film.getLinkToUpload(), file);
		System.out.println(resp.toString());
		
		if(resp.has("statusOK") && resp.getBoolean("statusOK")){
			try{
				Film tmp = mapper.jsonToFilm(resp);
				for(PictureData dt : tmp.getPictures()){
					if(!film.getPictures().contains(dt)){
						film.getPictures().add(dt);
					}
				}
				
				return loadMissingPictures(film);
			}
			catch(NullPointerException e){
				return new ResponseObject(false, "Nullpointer im Mapper", null);
			}
		}
		else{return new ResponseObject(false, resp.getString("errormessage"), null);}
	}
	
	private ResponseObject loadMissingPictures(Film film){
		for(PictureData dt : film.getPictures()){
			if(dt.getData() == null){
				Image im = HttpHandler.retrievePicture(dt.getLink());
				if(im != null){
					dt.setData(im);
				}
				else{
					return new ResponseObject(false, "Bild konnte nicht geladen werden.", null);
				}
			}
		}
		return new ResponseObject(true, null, null);
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
