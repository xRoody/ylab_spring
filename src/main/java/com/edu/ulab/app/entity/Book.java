package com.edu.ulab.app.entity;


import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
public class Book implements com.edu.ulab.app.repositories.utils.Entity<Long> {
    private Long id;
    private User user;
    private String title;
    private String author;
    private long pageCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id!=null && id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
