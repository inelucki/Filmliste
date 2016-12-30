package main.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import main.model.Tag;

public interface TagRepository extends CrudRepository<Tag, String> {

    List<Tag> findByName(String name);
}
