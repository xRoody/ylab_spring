package com.edu.ulab.app.entity;


import lombok.*;

import java.util.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class User implements com.edu.ulab.app.repositories.utils.Entity<Long> {
    private Long user_id;
    private String fullName;
    private String title;
    private int age;
    private List<Book> bookSet=new ArrayList<>();

    public Long getId() {
        return user_id;
    }

    public void setId(Long id) {
        this.user_id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(user_id, user.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id);
    }
}
