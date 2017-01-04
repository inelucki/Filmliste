package main;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import main.jpa.FilmRepository;
import main.jpa.PictureRepository;
import main.jpa.TagRepository;
import main.jpa.UserRepository;
import main.model.Film;
import main.model.Filmliste;
import main.model.Index;
import main.model.PictureEntity;
import main.model.RequestStatus;
import main.model.Tag;
import main.model.TagListe;
import main.model.User;
import main.security.CurrentUser;

@RestController
public class Controller {
	
	private FilmRepository filmRepo;
	private TagRepository tagRepo;
	private PictureRepository picRepo;
	private UserRepository userRepo;
	
	@Autowired
	public Controller(FilmRepository filmRepo, TagRepository tagRepo,
					PictureRepository picRepo, UserRepository userRepo){
		this.filmRepo=filmRepo;
		this.tagRepo=tagRepo;
		this.picRepo=picRepo;
		this.userRepo=userRepo;
	}
	
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
			for(Film f : filmRepo.findAll()){
				f.getTags().remove(tag.getIdNumber());
				filmRepo.save(f);
			}
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
    		f = addLinksToFilm(f);
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
				film = addLinksToFilm(film);
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
			if(payload.containsKey("content") && payload.get("content") instanceof String){
				film.setContent((String) payload.get("content"));
			}
			if(payload.containsKey("tags") && payload.get("tags") instanceof List<?>){
				List<HashMap<String, Integer>> colTags = (List<HashMap<String, Integer>>) payload.get("tags");
				List<Long> tagIDs = new LinkedList<>();
				for(HashMap<String, Integer> map : colTags){
					tagIDs.add(map.get("idNumber").longValue());
				}
				film.getTags().clear();
				film.getTags().addAll(tagIDs);
			}
			if(payload.containsKey("pictures") && payload.get("pictures") instanceof List<?>){
				List<Integer> picList = (List<Integer>) payload.get("pictures");
				List<Long> picIds = new LinkedList<>();
				for(Integer i : picList){picIds.add(i.longValue());}
				film.getPictures().clear();
				film.getPictures().addAll(picIds);
			}
			
			filmRepo.save(film);
			film.setStatusOK(true);
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
			List<Long> picIDs = film.getPictures();
			for(PictureEntity ent : picRepo.findAll()){
				if(picIDs.contains(ent.getIdNumber())){
					picRepo.delete(ent);
				}
			}
			filmRepo.delete(film);
			
			return new ResponseEntity<RequestStatus>(new RequestStatus(true, null), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/picture/{idNumber}", method=RequestMethod.PUT)
    public HttpEntity<Film> uploadPicture(@PathVariable Long idNumber, @RequestParam(value="picture", required=false) MultipartFile file,
            RedirectAttributes redirectAttributes) {
		Film film = filmRepo.findOne(idNumber);
		if(film == null){
			film = new Film("");
			film.setStatusOK(false);
			film.setErrormessage("no such film");
			return new ResponseEntity<Film>(film, HttpStatus.OK);
		}
		else{
			try{
				PictureEntity pic = new PictureEntity(file.getBytes());
				picRepo.save(pic);
				film.getPictures().add(pic.getIdNumber());
				filmRepo.save(film);
				
				film.setStatusOK(true);
				film = addLinksToFilm(film);
				return new ResponseEntity<Film>(film, HttpStatus.OK);
			}
			catch(IOException e){
				Film f = new Film("");
				f.setStatusOK(false);
				f.setErrormessage("IOException beim lesen des bytearray!");
				return new ResponseEntity<Film>(f, HttpStatus.OK);
			}
		}
	}
	
	@RequestMapping(value="/picture/{idNumber}", method=RequestMethod.GET)
    public ResponseEntity<Resource> getPicture(@PathVariable Long idNumber) {
		PictureEntity pic = picRepo.findOne(idNumber);
		Resource res = new ByteArrayResource(pic.getPic());
		return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"picture\"")
                .body(res);
	}
	
	@RequestMapping(value="/picture/{idNumber}", method=RequestMethod.DELETE)
    public HttpEntity<RequestStatus> deletePicture(@PathVariable Long idNumber) {
		PictureEntity pic = picRepo.findOne(idNumber);
		Long picId = pic.getIdNumber();
		picRepo.delete(pic);
		for(Film film : filmRepo.findAll()){
			if(film.getPictures().contains(picId)){
				film.getPictures().remove(picId);
				filmRepo.save(film);
			}
		}
		
		return new ResponseEntity<RequestStatus>(new RequestStatus(true, null), HttpStatus.OK);
	}
	
	@RequestMapping(value="/changepassw", method=RequestMethod.POST)
	public HttpEntity<RequestStatus> changePassword(@RequestParam(value="pw", required=true) String newpw,
			@CurrentUser User user){
		user.setPassword(newpw);
		userRepo.save(user);
		return new ResponseEntity<RequestStatus>(new RequestStatus(true, null), HttpStatus.OK);
	}
	
	private Film addLinksToFilm(Film film){
		film.add(linkTo(methodOn(Controller.class).deleteFilm(film.getIdNumber())).withRel("delete"));
		film.add(linkTo(methodOn(Controller.class).updateFilm(film.getIdNumber(), null)).withRel("update"));
		film.add(linkTo(methodOn(Controller.class).uploadPicture(film.getIdNumber(), null, null)).withRel("uploadpicture"));
		
		for(Long picID : film.getPictures()){
			film.add(linkTo(methodOn(Controller.class).getPicture(picID)).withRel(""+picID));
		}
		
		return film;
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
