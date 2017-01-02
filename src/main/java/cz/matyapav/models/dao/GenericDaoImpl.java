package cz.matyapav.models.dao;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Pavel on 21.12.2016.
 */

@Transactional(readOnly = false)
public class GenericDaoImpl<T, PK extends Serializable>
        implements GenericDao<T, PK> {

    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    public GenericDaoImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T create(T t) {
        this.entityManager.persist(t);
        return t;
    }

    @Override
    public T read(PK id) {
        return this.entityManager.find(entityClass, id);
    }

    @Override
    public void update(T t) {
        this.entityManager.merge(t);
    }

    @Override
    public boolean delete(PK id) {
        T obj = this.entityManager.find(entityClass, id);
        if (obj != null) {
            this.entityManager.remove(obj);
            return true;
        }
        return false;
    }

    @Override
    public List<T> list() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }
}
