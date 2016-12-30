package main.jpa;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import main.model.Film;

public interface FilmRepository extends CrudRepository<Film, String> {

    List<Film> findByName(String name);
}
