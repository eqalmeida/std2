package repo;

import controller.ControllerBase;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class AbstractJpaController<T> implements Serializable {
    
    private Class<T> entityClass;
    

    public AbstractJpaController(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) throws Exception {

        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            String str = e.getCause().getMessage();
            int index = str.indexOf("Duplicate entry");
            if (index > 0) {
                int ini = index + 16;
                int fin = str.indexOf(" ", ini + 2);
                throw new Exception("Valor duplicado: " + str.substring(ini, fin));
            } else {
                throw e;
            }
        }
    }

    public void edit(T entity) throws Exception {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            String str = e.getCause().getMessage();
            int index = str.indexOf("Duplicate entry");
            if (index > 0) {
                int ini = index + 16;
                int fin = str.indexOf(" ", ini + 2);
                throw new Exception("Valor duplicado: " + str.substring(ini, fin));
            } else {
                throw e;
            }
        }

    }

    public void remove(T entity) {
        EntityManager em = null;

        em = getEntityManager();
        em.getTransaction().begin();
        em.remove(em.merge(entity));
        em.getTransaction().commit();
    }

    public T find(Object id) {

        EntityManager em = null;

        em = getEntityManager();
        return em.find(entityClass, id);
    }

    public List<T> findAll() {

        EntityManager em = null;

        em = getEntityManager();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();

    }

    public List<T> findRange(int maxResults, int firstResult) {

        EntityManager em = getEntityManager();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));

        Query q = em.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public int count() {

        EntityManager em = getEntityManager();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(entityClass);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }
    
    
    
    protected EntityManager getEntityManager(){
        return ControllerBase.getEntityManager();
    } 
    
    
    
    
    
    
}
