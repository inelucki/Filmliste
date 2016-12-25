package main;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Controller {

    private static final String TEMPLATE = "Hello, %s!";

    @RequestMapping("/film")
    public HttpEntity<Film> film(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name) {

        Film film = new Film(String.format(TEMPLATE, name));
        film.add(linkTo(methodOn(Controller.class).film(name)).withSelfRel());

        return new ResponseEntity<Film>(film, HttpStatus.OK);
    }
}
