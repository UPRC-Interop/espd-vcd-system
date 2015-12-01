package eu.esens.espdvcd.model.dao;

import java.io.Serializable;
import java.util.List;

/**
 * TODO: Add description.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public interface DAOInterface<T, Id extends Serializable> {

    void persist(T entity);

    void update(T entity);

    T findById(Class<T> clazz, Id id);

    void delete(T entity);

    List<T> findAll(Class<T> clazz);

    void deleteAll(Class<T> clazz);

    Long getCountOf(Class<T> clazz);
}
