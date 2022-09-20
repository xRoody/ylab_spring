package com.edu.ulab.app.repositories;

import com.edu.ulab.app.entity.Book;

import java.util.List;

public interface BookRepo extends CrudRepository<Book, Long> {
    List<Book> findAllBooksByUserId(Long id);
}
