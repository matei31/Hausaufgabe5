package repository;

import java.io.IOException;

/**
 * declare create, getAll, update, delete functions
 *
 * @param <T>
 */
public interface ICrudRepo<T> {

    T findOne(T obj) throws IOException;

    Iterable<T> findAll() throws IOException;

    T save(T obj) throws IOException;

    T update(T obj) throws IOException;

    T delete(T obj) throws IOException;
}