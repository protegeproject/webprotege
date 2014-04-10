package edu.stanford.bmir.protege.web.server.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     A Spring Data {@link Repository} that has the functionality of a {@link org.springframework.data.repository.CrudRepository}
 *     without the delete all methods.
 * </p>
 */
public interface WebProtegeRepository<T, ID extends Serializable> extends Repository<T, ID>, CrudRepository<T, ID> {

    <S extends T> S save(S s);

    <S extends T> Iterable<S> save(Iterable<S> ses);

    T findOne(ID id);

    boolean exists(ID id);

    Iterable<T> findAll();

    Iterable<T> findAll(Iterable<ID> ids);

    long count();

    void delete(ID id);

    void delete(T t);

    void delete(Iterable<? extends T> ts);
}
