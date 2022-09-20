package com.edu.ulab.app.repositories.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.repositories.UserRepo;
import com.edu.ulab.app.storage.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepoImpl extends CrudRepoImpl<User, Long> implements UserRepo {
    public UserRepoImpl(DataSource dataSource) {
        super(dataSource, User.class);
    }
}
