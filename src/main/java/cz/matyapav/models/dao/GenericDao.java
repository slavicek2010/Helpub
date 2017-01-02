package cz.matyapav.models.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pavel on 21.12.2016.
 */
public interface GenericDao<T, PK extends Serializable> {

    /**
     * Persist the newInstance object into database
     */
    T create(T newInstance);

    /**
     * Retrieve an object that was previously persisted to the database using
     * the indicated id as primary key
     */
    T read(PK id);

    /**
     * Save changes made to a persistent object.
     */
    void update(T transientObject);

    /**
     * Remove an object from persistent storage in the database
     */
    boolean delete(PK id);

    List<T> list();
}