package com.edu.ulab.app.repositories.utils;

public interface IdGenerator<Target extends Entity<T>, T> {
    T genNextValue();
    Class<Target> getTargetClass();
}
