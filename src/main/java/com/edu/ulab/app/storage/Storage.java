package com.edu.ulab.app.storage;

import com.edu.ulab.app.repositories.utils.Entity;
import com.edu.ulab.app.repositories.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class Storage implements DataSource{
    //todo создать хранилище в котором будут содержаться данные
    // сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
    // продумать что у узера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции

    private Map<String,  Map<Object, Object>> tables=new HashMap<>();
    private Map<Class<?>, IdGenerator<?,?>> sequences;



    @Autowired
    public Storage(List<IdGenerator<?,?>> gens) {
        this.sequences =new HashMap<>();
        for (IdGenerator<?,?> gen:gens) sequences.put(gen.getTargetClass(), gen);
    }

    {
        tables.put("user", new ConcurrentHashMap<>());
        tables.put("book", new ConcurrentHashMap<>());
    }

    public <Ent extends Entity<ID>, ID> Ent saveOrUpdate(Ent entity){
        String tab=entity.getClass().getSimpleName().toLowerCase();
        if (!tables.containsKey(tab)){
            throw new RuntimeException("Table "+tab+" is not exists");
        }
        if (entity.getId()!=null){
            return update(entity);
        }
        entity.setId((ID) sequences.get(entity.getClass()).genNextValue());
        tables.get(tab).put(entity.getId(), entity);
        return entity;
    }

    private <Ent extends Entity<ID>, ID> Ent update(Ent entity){
        String tab=entity.getClass().getSimpleName().toLowerCase();
        if (!tables.containsKey(tab)){
            throw new RuntimeException("Table "+tab+" is not exists");
        }
        if (!tables.get(tab).containsKey(entity.getId())){
            throw new RuntimeException("Try to save detached or deleted entity");
        }
        tables.get(tab).put(entity.getId(), entity);
        return entity;
    }

    public <Ent extends Entity<ID>, ID> Ent delete(Ent entity){
        String tab=entity.getClass().getSimpleName().toLowerCase();
        if (!tables.containsKey(tab)){
            throw new RuntimeException("Table "+tab+" is not exists");
        }
        return (Ent) deleteById(entity.getId(), tab);
    }

    public <Ent extends Entity<ID>, ID> Ent delete(ID id, Class<Ent> entClass){
        String tab=entClass.getSimpleName().toLowerCase();
        if (!tables.containsKey(tab)){
            throw new RuntimeException("Table "+tab+" is not exists");
        }
        return (Ent) deleteById(id, tab);
    }

    private <ID> Object deleteById(ID id, String tab){
        return tables.get(tab).remove(id);
    }

    public <Ent extends Entity<ID>, ID> Optional<Ent> findById(ID id, Class<Ent> entClass){
        String tab=entClass.getSimpleName().toLowerCase();
        if (!tables.containsKey(tab)){
            throw new RuntimeException("Table "+tab+" is not exists");
        }
        Object o=tables.get(tab).get(id);
        return o==null?Optional.empty():Optional.of((Ent) o);
    }

    public <Ent extends Entity<ID>, ID> List<Ent> findAll(Class<Ent> entClass){
        String tab=entClass.getSimpleName().toLowerCase();
        if (!tables.containsKey(tab)){
            throw new RuntimeException("Table "+tab+" is not exists");
        }
        return tables.get(tab).values().stream().map(x -> (Ent) x).toList();
    }
}
