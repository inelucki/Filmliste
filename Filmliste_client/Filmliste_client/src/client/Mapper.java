package client;

import org.json.JSONArray;
import org.json.JSONObject;

public class Mapper {
	
	private final Controller controller;
	
	public Mapper(Controller con){
		controller = con;
	}

	public JSONObject filmToJson(Film film){
		JSONObject json = new JSONObject();
		json.put("name", film.getName());
		json.put("idNumber", film.getId());
		json.put("content", film.getContent());
		
		JSONArray colTags = new JSONArray();
		for(Tag t : film.getTags()){
			colTags.put(tagToJson(t));
		}
		json.put("tags", colTags);
		
		JSONArray colPics = new JSONArray();
		for(PictureData pidId : film.getPictures()){
			colPics.put(pidId.getNumberID());
		}
		json.put("pictures", colPics);
		
		return json;
	}
	
	public Film jsonToFilm(JSONObject json) throws NullPointerException{
		if(json.has("idNumber") && json.has("name") && json.has("content") && json.has("_links")){
			JSONObject links = json.getJSONObject("_links");
			if(links.has("delete") && links.has("update") && links.has("uploadpicture")){
				String name = json.getString("name");
				Long id = json.getLong("idNumber");
				String updateLink = links.getJSONObject("update").getString("href");
				String deleteLink = links.getJSONObject("delete").getString("href");
				String upLoadLink = links.getJSONObject("uploadpicture").getString("href");
				Film film = new Film(name, id, updateLink, deleteLink, upLoadLink);
				
				if(json.has("content") && !json.isNull("content")){
					String content = json.getString("content");
					film.setContent(content);
				}
						
				if(json.has("tags") && !json.isNull("tags")){
					JSONArray colTags = json.getJSONArray("tags");
					for(int i = 0; i<colTags.length(); i++){
						Long tagID = colTags.getLong(i);
						if(controller.existTag(tagID)){
							film.addTag(controller.getTag(tagID));
						}
					}
				}
				
				if(json.has("pictures") && !json.isNull("pictures")){
					JSONArray colPics = json.getJSONArray("pictures");
					for(int i = 0; i<colPics.length(); i++){
						Long picID = colPics.getLong(i);
						
						String picLink = "";
						if(links.has(""+picID)){
							picLink = links.getJSONObject(""+picID).getString("href");
						}
						PictureData dt = new PictureData(picID, picLink);
						film.getPictures().add(dt);
					}
				}
				
				return film;
			}
			else{throw new NullPointerException("nicht alle links sind in der antwort enthalten.");}
		}
		else{throw new NullPointerException("nicht alle felder in der antwort enthalten.");}
	}
	
	public JSONObject tagToJson(Tag tag){
		JSONObject json = new JSONObject();
		json.put("name", tag.getTag());
		json.put("idNumber", tag.getId());
		return json;
	}
	
	public Tag jsonToTag(JSONObject json) throws NullPointerException{
		if(json.has("name") && json.has("idNumber") && json.has("_links")){
			JSONObject links = json.getJSONObject("_links");
			if(links.has("delete")){
				Tag tag = new Tag(json.getString("name"), json.getLong("idNumber"),
									links.getJSONObject("delete").getString("href"));
				return tag;
			}
			else{throw new NullPointerException("der link zur resource fehlt in der antwort.");}
		}
		else{throw new NullPointerException("in der antwort waren nicht alle erwarteten felder enthalten.");}
	}
}
