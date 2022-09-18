package com.edu.ulab.app.storage;

import com.edu.ulab.app.repositories.utils.Entity;

import java.util.List;
import java.util.Optional;

public interface DataSource {
    <Ent extends Entity<ID>, ID> Ent saveOrUpdate(Ent entity);
    <Ent extends Entity<ID>, ID> Ent delete(Ent entity);
    <Ent extends Entity<ID>, ID> Ent delete(ID id, Class<Ent> entClass);
    <Ent extends Entity<ID>, ID> Optional<Ent> findById(ID id, Class<Ent> entClass);
    <Ent extends Entity<ID>, ID> List<Ent> findAll(Class<Ent> entClass);
}
