package com.edu.ulab.app.repositories.utils.gens;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.repositories.utils.IdGenerator;
import org.springframework.stereotype.Component;

@Component
public class UserIdGenerator implements IdGenerator<User, Long> {
    private long id=1;
    @Override
    public Long genNextValue() {
        return id++;
    }

    @Override
    public Class<User> getTargetClass() {
        return User.class;
    }
}
