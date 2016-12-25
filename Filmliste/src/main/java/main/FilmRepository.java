package main;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface FilmRepository extends CrudRepository<Film, String> {

    List<Film> findByName(String name);
}
