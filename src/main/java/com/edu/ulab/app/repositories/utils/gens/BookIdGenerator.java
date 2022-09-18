package com.edu.ulab.app.repositories.utils.gens;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.repositories.utils.IdGenerator;
import org.springframework.stereotype.Component;

@Component
public class BookIdGenerator implements IdGenerator<Book, Long> {
    private long id=1;

    @Override
    public Long genNextValue() {
        return id++;
    }

    @Override
    public Class<Book> getTargetClass() {
        return Book.class;
    }
}
