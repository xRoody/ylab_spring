package com.edu.ulab.app.repositories.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.repositories.BookRepo;
import com.edu.ulab.app.storage.DataSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRepoImpl extends CrudRepoImpl<Book, Long> implements BookRepo {
    public BookRepoImpl(DataSource dataSource) {
        super(dataSource, Book.class);
    }

    public List<Book> findAllBooksByUserId(Long id){
        return dataSource.findById(id, User.class).stream().flatMap(x->x.getBookSet().stream()).collect(Collectors.toList());
    }
}
