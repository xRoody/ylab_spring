package com.edu.ulab.app.repositories.utils;

public interface Entity<T> {
    T getId();
    void setId(T id);
}
