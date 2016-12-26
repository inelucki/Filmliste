package main;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	
	@Autowired
	private FilmRepository repo;
	
	@RequestMapping(value="/filme/{filmname}", method=RequestMethod.GET)
	public HttpEntity<Film> getFilm(@PathVariable String filmname){
		String c = "GET irgendein film";
		Film film = new Film(filmname, c);
		film.add(linkTo(methodOn(Controller.class).getFilm(filmname)).withSelfRel());
		return new ResponseEntity<Film>(film, HttpStatus.OK);
	}
	
	@RequestMapping(value="/filme/{filmname}", method=RequestMethod.DELETE)
	public HttpEntity<Film> deleteFilm(@PathVariable String filmname){
		String c = "DELETE irgendein film";
		Film film = new Film(filmname, c);
		if(repo.exists(filmname)){
			repo.delete(film);
		}
		film.add(linkTo(methodOn(Controller.class).deleteFilm(filmname)).withSelfRel());
		return new ResponseEntity<Film>(film, HttpStatus.OK);
	}
	
	//create
	@RequestMapping(value="/filme/{filmname}", method=RequestMethod.PUT)
	public HttpEntity<Film> putFilm(@PathVariable String filmname){
		String c = "PUT irgendein film";
		Film film = new Film(filmname, c);
		repo.save(film);
		film.add(linkTo(methodOn(Controller.class).putFilm(filmname)).withSelfRel());
		return new ResponseEntity<Film>(film, HttpStatus.OK);
	}
	
	//update
	@RequestMapping(value="/filme/{filmname}", method=RequestMethod.POST)
	public HttpEntity<Film> postFilm(@PathVariable String filmname,
			@RequestParam(value="name", required=false, defaultValue="") String newname
			){
		String name = newname.isEmpty() ? filmname : newname;
		String c = "POST irgendein film mit neuem namen : "+name;
		Film film = new Film(name, c);
		film.add(linkTo(methodOn(Controller.class).postFilm(name, name)).withSelfRel());
		return new ResponseEntity<Film>(film, HttpStatus.OK);
	}

    @RequestMapping(value="/filmliste", method=RequestMethod.GET)
    public HttpEntity<Filmliste> filmliste() {

    	List<String> elements = new LinkedList<>();
    	repo.findAll().forEach(f -> elements.add(f.getName()));
    	
    	if(elements.isEmpty()){
    		elements.add("Keine Elemente gefunden");
    	}
    	
        Filmliste film = new Filmliste(elements);
        film.add(linkTo(methodOn(Controller.class).filmliste()).withSelfRel());
        
        return new ResponseEntity<Filmliste>(film, HttpStatus.OK);
    }
    
    
}
