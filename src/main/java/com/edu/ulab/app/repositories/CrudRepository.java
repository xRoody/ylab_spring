package com.edu.ulab.app.repositories;

import com.edu.ulab.app.repositories.utils.Entity;

import java.util.List;
import java.util.Optional;

public interface CrudRepository <Entity extends com.edu.ulab.app.repositories.utils.Entity<ID>,ID>{
    Entity persist(Entity entity);
    Entity delete(Entity entity);
    Entity deleteById(ID id);
    Optional<Entity> findById(ID id);
    List<Entity> findAll();
}
