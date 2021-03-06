package main.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import main.model.Tag;

public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> findByName(String name);
    List<Tag> findByIdNumber(Long idNumber);
}
