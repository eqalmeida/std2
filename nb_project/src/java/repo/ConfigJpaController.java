/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Config;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class ConfigJpaController extends AbstractJpaController {

    public void create(Config config) {
        EntityManager em = null;

        em = getEntityManager();
        em.getTransaction().begin();
        em.persist(config);
        em.getTransaction().commit();
    }

    public void edit(Config config) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            config = em.merge(config);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = config.getId();
            }
            throw ex;
        }
    }

    public void destroy(Short id) throws NonexistentEntityException {
        EntityManager em = null;

        em = getEntityManager();
        em.getTransaction().begin();
        Config config;
        try {
            config = em.getReference(Config.class, id);
            config.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The config with id " + id + " no longer exists.", enfe);
        }
        em.remove(config);
        em.getTransaction().commit();
    }

    public List<Config> findConfigEntities() {
        return findConfigEntities(true, -1, -1);
    }

    public List<Config> findConfigEntities(int maxResults, int firstResult) {
        return findConfigEntities(false, maxResults, firstResult);
    }

    private List<Config> findConfigEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Config.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Config findConfig() {
        EntityManager em = getEntityManager();

        Config c;
        c = em.find(Config.class, (short) 1);
        if (c == null) {
            c = new Config((short) 1);
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        }
        return c;
    }

    public int getConfigCount() {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Config> rt = cq.from(Config.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
