package de.shimunmatic.informationhub.service.definition;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T, U> {

    Optional<T> getById(U id);

    List<T> getAll();

    T save(T entity);

    List<T> save(Iterable<T> entities);

    T update(T entity);

    void delete(T entity);

    void deleteById(U id);
}