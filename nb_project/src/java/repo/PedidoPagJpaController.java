/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import controller.ControllerBase;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import model.PedidoPag;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class PedidoPagJpaController implements Serializable {

    public PedidoPagJpaController() {
        this.emf = ControllerBase.getEmf();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PedidoPag pedidoPag) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(pedidoPag);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PedidoPag pedidoPag) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pedidoPag = em.merge(pedidoPag);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedidoPag.getId();
                if (findPedidoPag(id) == null) {
                    throw new NonexistentEntityException("The pedidoPag with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PedidoPag pedidoPag;
            try {
                pedidoPag = em.getReference(PedidoPag.class, id);
                pedidoPag.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedidoPag with id " + id + " no longer exists.", enfe);
            }
            em.remove(pedidoPag);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PedidoPag> findPedidoPagEntities() {
        return findPedidoPagEntities(true, -1, -1);
    }

    public List<PedidoPag> findPedidoPagEntities(int maxResults, int firstResult) {
        return findPedidoPagEntities(false, maxResults, firstResult);
    }

    public List<PedidoPag> findPedidoPagEntities(Integer pedidoId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT p FROM PedidoPag p WHERE p.pedido.id = :pedidoId");
            q.setParameter("pedidoId", pedidoId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    private List<PedidoPag> findPedidoPagEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PedidoPag.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PedidoPag findPedidoPag(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PedidoPag.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoPagCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PedidoPag> rt = cq.from(PedidoPag.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
