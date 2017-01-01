package main;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
import org.springframework.web.bind.annotation.RestController;

import main.jpa.FilmRepository;
import main.jpa.TagRepository;
import main.model.Film;
import main.model.Filmliste;
import main.model.Index;
import main.model.RequestStatus;
import main.model.Tag;
import main.model.TagListe;

@RestController
public class Controller {
	
	@Autowired
	private FilmRepository filmRepo;
	@Autowired
	private TagRepository tagRepo;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public HttpEntity<Index> index(){
		Link taglist = linkTo(methodOn(Controller.class).tagliste()).withRel("tagliste");
		Link filmlist = linkTo(methodOn(Controller.class).filmliste()).withRel("filmliste");
		Link self = linkTo(methodOn(Controller.class).index()).withSelfRel();
		
		Index ind = new Index(true, null, ""+filmRepo.count(), ""+tagRepo.count());
		ind.add(taglist);
		ind.add(filmlist);
		ind.add(self);
		return new ResponseEntity<Index>(ind, HttpStatus.OK);
	}
	
	@RequestMapping(value="/tag", method=RequestMethod.GET)
	public HttpEntity<TagListe> tagliste(){
		List<Tag> elements = new LinkedList<>();
    	for(Tag f : tagRepo.findAll()){
    		f.add(linkTo(methodOn(Controller.class).deleteTag(f.getIdNumber())).withRel("delete"));
    		elements.add(f);
    	}
    	
        TagListe tags = new TagListe(true, null, elements);
        tags.add(linkTo(methodOn(Controller.class).tagliste()).withSelfRel());
        tags.add(linkTo(methodOn(Controller.class).createTag(null)).withRel("create_tag"));
        
        return new ResponseEntity<TagListe>(tags, HttpStatus.OK);
	}
	
	@RequestMapping(value="/tag/{idNumber}", method=RequestMethod.DELETE)
	public HttpEntity<RequestStatus> deleteTag(@PathVariable Long idNumber){
		Tag tag = tagRepo.findOne(idNumber);
		if(tag == null){
			return new ResponseEntity<RequestStatus>(new RequestStatus(false, "no_such_tag"), HttpStatus.OK);
		}
		else{
			tagRepo.delete(tag);
			return new ResponseEntity<RequestStatus>(new RequestStatus(true, null), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/tag", method=RequestMethod.PUT)
	public HttpEntity<Tag> createTag(@RequestBody Map<String, Object> payload){
		Object o = payload.get("tag");
		if(o != null && o instanceof String){
			Tag tag = new Tag((String) o);
			List<Tag> tagsWithName = tagRepo.findByName(tag.getName());
			if(!tagsWithName.isEmpty()){
				tag.setStatusOK(false);
				tag.setErrormessage("der Tag existiert bereits.");
				return new ResponseEntity<Tag>(tag, HttpStatus.OK);
			}
			else{
				tagRepo.save(tag);
				List<Tag> djks = new LinkedList<>();
				tagRepo.findAll().forEach(f -> djks.add(f));
				tag.setStatusOK(true);
				tag.add(linkTo(methodOn(Controller.class).tagliste()).withRel("tagliste"));
				tag.add(linkTo(methodOn(Controller.class).deleteTag(tag.getIdNumber())).withRel("delete"));
				return new ResponseEntity<Tag>(tag, HttpStatus.OK);
			}
		}
		else{
			Tag t = new Tag("");
			t.setErrormessage("es wurde kein Tag uebermittelt.");
			t.setStatusOK(false);
			return new ResponseEntity<Tag>(t, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/film", method=RequestMethod.GET)
    public HttpEntity<Filmliste> filmliste() {
    	List<Film> elements = new LinkedList<>();
    	for(Film f : filmRepo.findAll()){
    		f.add(linkTo(methodOn(Controller.class).deleteFilm(f.getIdNumber())).withRel("delete"));
    		f.add(linkTo(methodOn(Controller.class).updateFilm(f.getIdNumber(), null)).withRel("update"));
    		elements.add(f);
    	}
    	
        Filmliste film = new Filmliste(true, "", elements);
        film.add(linkTo(methodOn(Controller.class).filmliste()).withSelfRel());
        film.add(linkTo(methodOn(Controller.class).createFilm(null)).withRel("create_film"));
        
        return new ResponseEntity<Filmliste>(film, HttpStatus.OK);
    }
	
	@RequestMapping(value="/film", method=RequestMethod.PUT)
    public HttpEntity<Film> createFilm(@RequestBody Map<String, Object> payload) {
		Object o = payload.get("film");
		if(o != null && o instanceof String){
			Film film = new Film((String) o);
			List<Film> filmeWithName = filmRepo.findByName(film.getName());
			if(!filmeWithName.isEmpty()){
				film.setStatusOK(false);
				film.setErrormessage("der Film existiert bereits.");
				return new ResponseEntity<Film>(film, HttpStatus.OK);
			}
			else{
				filmRepo.save(film);
				film.setStatusOK(true);
				film.add(linkTo(methodOn(Controller.class).filmliste()).withRel("filmliste"));
				film.add(linkTo(methodOn(Controller.class).deleteFilm(film.getIdNumber())).withRel("delete"));
				return new ResponseEntity<Film>(film, HttpStatus.OK);
			}
		}
		else{
			Film film = new Film("");
			film.setStatusOK(false);
			film.setErrormessage("es wurde kein Filmname gesendet!");
			return new ResponseEntity<Film>(film, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/film/{idNumber}", method=RequestMethod.POST)
    public HttpEntity<Film> updateFilm(@PathVariable Long idNumber, @RequestBody Map<String, Object> payload) {
		Film film = filmRepo.findOne(idNumber);
		if(film == null){
			Film f = new Film("");
			f.setStatusOK(false);
			f.setErrormessage("unter dieser id gibt es keinen Film.");
			return new ResponseEntity<Film>(f, HttpStatus.OK); 
		}
		else{
			//TODO
			film.setStatusOK(false);
			film.setErrormessage("der else zweig dieser methode : TODO");
			return new ResponseEntity<Film>(film, HttpStatus.OK); 
		}
	}
	
	@RequestMapping(value="/film/{idNumber}", method=RequestMethod.DELETE)
    public HttpEntity<RequestStatus> deleteFilm(@PathVariable Long idNumber) {
		Film film = filmRepo.findOne(idNumber);
		if(film == null){
			return new ResponseEntity<RequestStatus>(new RequestStatus(false, "no_such_film"), HttpStatus.OK);
		}
		else{
			filmRepo.delete(film);
			return new ResponseEntity<RequestStatus>(new RequestStatus(true, null), HttpStatus.OK);
		}
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
