package main.jpa;

import org.springframework.data.repository.CrudRepository;

import main.model.PictureEntity;

public interface PictureRepository extends CrudRepository<PictureEntity, Long>{

}
