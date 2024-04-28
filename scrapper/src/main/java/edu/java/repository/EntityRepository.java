package edu.java.repository;

import java.util.Collection;

public interface EntityRepository<T> {
    T add(T entity);

    T remove(T entity);

    Collection<T> findAll();
}
