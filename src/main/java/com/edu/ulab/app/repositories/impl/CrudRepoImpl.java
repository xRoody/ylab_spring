package com.edu.ulab.app.repositories.impl;

import com.edu.ulab.app.repositories.CrudRepository;
import com.edu.ulab.app.repositories.utils.Entity;
import com.edu.ulab.app.storage.DataSource;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CrudRepoImpl<Entity extends com.edu.ulab.app.repositories.utils.Entity<ID>, ID> implements CrudRepository<Entity, ID> {
    protected DataSource dataSource;
    private Class<Entity> target;

    @Override
    public Entity persist(Entity entity) {
        return dataSource.saveOrUpdate(entity);
    }

    @Override
    public Entity delete(Entity entity) {
        return dataSource.delete(entity);
    }

    @Override
    public Entity deleteById(ID id) {
        return dataSource.delete(id, target);
    }

    @Override
    public Optional<Entity> findById(ID id) {
        return dataSource.findById(id,target);
    }

    @Override
    public List<Entity> findAll() {
        return dataSource.findAll(target);
    }
}
