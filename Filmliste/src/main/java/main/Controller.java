package main;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.jpa.FilmRepository;
import main.jpa.TagRepository;
import main.model.Film;
import main.model.Filmliste;
import main.model.Tag;
import main.model.TagListe;

@RestController
public class Controller {
	
	@Autowired
	private FilmRepository filmRepo;
	@Autowired
	private TagRepository tagRepo;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public HttpEntity<List<Link>> index(){
		List<Link> links = new LinkedList<>();
		Link taglist = linkTo(methodOn(Controller.class).tagliste()).withRel("tagliste");
		Link filmlist = linkTo(methodOn(Controller.class).filmliste()).withRel("filmliste");
		Link self = linkTo(methodOn(Controller.class).index()).withSelfRel();
		links.add(taglist);
		links.add(filmlist);
		links.add(self);
		return new ResponseEntity<List<Link>>(links, HttpStatus.OK);
	}
	
	@RequestMapping(value="/tagliste", method=RequestMethod.GET)
	public HttpEntity<TagListe> tagliste(){
		List<Tag> elements = new LinkedList<>();
    	for(Tag f : tagRepo.findAll()){
    		f.add(linkTo(methodOn(Controller.class).deleteTag(f.getIdNumber())).withRel("delete"));
    		elements.add(f);
    	}
    	
        TagListe tags = new TagListe(elements);
        tags.add(linkTo(methodOn(Controller.class).tagliste()).withSelfRel());
        tags.add(linkTo(methodOn(Controller.class).createTag(null)).withRel("create_tag"));
        
        return new ResponseEntity<TagListe>(tags, HttpStatus.OK);
	}
	
	@RequestMapping(value="/tag/{idNumber}", method=RequestMethod.DELETE)
	public HttpEntity<String> deleteTag(@PathVariable Long idNumber){
		List<Tag> tags = tagRepo.findByIdNumber(idNumber);
		if(tags.isEmpty()){
			return new ResponseEntity<String>("no_such_tag", HttpStatus.BAD_REQUEST);
		}
		else{
			tagRepo.delete(tags.get(0));
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/tag", method=RequestMethod.PUT)
	public HttpEntity<Tag> createTag(@RequestBody Map<String, Object> payload){
		Object o = payload.get("tag");
		if(o != null && o instanceof String){
			Tag tag = new Tag((String) o);
			if(tagRepo.exists(tag.getName())){
				return createErrorResponseTag("already_exists");
			}
			else{
				tagRepo.save(tag);
				tag.add(linkTo(methodOn(Controller.class).createTag(payload)).withSelfRel());
				return new ResponseEntity<Tag>(tag, HttpStatus.OK);
			}
		}
		else{
			return createErrorResponseTag("no_content");
		}
	}
	
	@RequestMapping(value="/filmliste", method=RequestMethod.GET)
    public HttpEntity<Filmliste> filmliste() {
    	List<Film> elements = new LinkedList<>();
    	for(Film f : filmRepo.findAll()){
    		f.add(linkTo(methodOn(Controller.class).filmliste()).withRel("filmliste"));
    		elements.add(f);
    	}
    	
        Filmliste film = new Filmliste(elements);
        film.add(linkTo(methodOn(Controller.class).filmliste()).withSelfRel());
        
        return new ResponseEntity<Filmliste>(film, HttpStatus.OK);
    }
	
	//create film
	
	//update film
	
	//delete film
	
	private ResponseEntity<Tag> createErrorResponseTag(String msg){
		ResponseEntity<Tag> resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		resp.getHeaders().add("error", msg);
		return resp;
	}
	
	private ResponseEntity<Film> createErrorResponseFilm(String msg){
		ResponseEntity<Film> resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		resp.getHeaders().add("error", msg);
		return resp;
	}
	
//	@RequestMapping(value="/filme/{filmname}", method=RequestMethod.GET)
//	public HttpEntity<Film> getFilm(@PathVariable String filmname){
//		String c = "GET irgendein film";
//		Film film = new Film(filmname, c, Collections.emptyList(), Collections.emptyList());
//		film.add(linkTo(methodOn(Controller.class).getFilm(filmname)).withSelfRel());
//		return new ResponseEntity<Film>(film, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value="/filme/{filmname}", method=RequestMethod.DELETE)
//	public HttpEntity<Film> deleteFilm(@PathVariable String filmname){
//		String c = "DELETE irgendein film";
//		Film film = new Film(filmname, c, Collections.emptyList(), Collections.emptyList());
//		if(filmRepo.exists(filmname)){
//			filmRepo.delete(film);
//		}
//		film.add(linkTo(methodOn(Controller.class).deleteFilm(filmname)).withSelfRel());
//		return new ResponseEntity<Film>(film, HttpStatus.OK);
//	}
//	
//	//create
//	@RequestMapping(value="/filme/{filmname}", method=RequestMethod.PUT)
//	public HttpEntity<Film> putFilm(@PathVariable String filmname, @RequestBody Map<String, Object> payload){
//		String c = "PUT irgendein film";
//		Film film = new Film(filmname, c, Collections.emptyList(), Collections.emptyList());
//		filmRepo.save(film);
//		film.add(linkTo(methodOn(Controller.class).putFilm(filmname, payload)).withSelfRel());
//		return new ResponseEntity<Film>(film, HttpStatus.OK);
//	}
//	
//	//update
//	@RequestMapping(value="/filme/{filmname}", method=RequestMethod.POST)
//	public HttpEntity<Film> postFilm(@PathVariable String filmname,
//			@RequestParam(value="name", required=false, defaultValue="") String newname
//			){
//		String name = newname.isEmpty() ? filmname : newname;
//		String c = "POST irgendein film mit neuem namen : "+name;
//		Film film = new Film(name, c, Collections.emptyList(), Collections.emptyList());
//		film.add(linkTo(methodOn(Controller.class).postFilm(name, name)).withSelfRel());
//		return new ResponseEntity<Film>(film, HttpStatus.OK);
//	}

}
