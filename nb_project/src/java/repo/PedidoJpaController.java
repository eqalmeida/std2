/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import controller.ControllerBase;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Pedido;
import model.Usuario;
import org.primefaces.model.SortOrder;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController() {
        this.emf = ControllerBase.getEmf();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = pedido.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                pedido.setUsuario(usuario);
            }
/*
            em.persist(pedido);
            if (usuario != null) {
                usuario.getPedidoCollection().add(pedido);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
            */ 
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getId());
            Usuario usuarioOld = persistentPedido.getUsuario();
            Usuario usuarioNew = pedido.getUsuario();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                pedido.setUsuario(usuarioNew);
            }
            pedido = em.merge(pedido);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedido.getId();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
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
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = pedido.getUsuario();
            em.remove(pedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResults, Calendar data, SortOrder sortOrder, String sortField){
        EntityManager em = getEntityManager();
        try{
            Date data1, data2;
            
            String query = "SELECT p FROM Pedido p WHERE p.data BETWEEN :data1 AND :data2";
            query += " ORDER BY ";
            query += (" p." + sortField);
            if(sortOrder == SortOrder.ASCENDING){
                query += " ASC";
            }
            else if(sortOrder == SortOrder.DESCENDING){
                query += " DESC";
            }
            
            Query q = em.createQuery(query);

            data.set(Calendar.DATE, 1);
            data1 = data.getTime();
            data.add(Calendar.MONTH, 1);
            data.add(Calendar.DATE, -1);
            data2 = data.getTime();
            
            q.setParameter("data1", data1, TemporalType.DATE);
            q.setParameter("data2", data2, TemporalType.DATE);
            
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResults);
            return q.getResultList();
        }finally{
            em.close();
        }
    }
            
    
    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
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

    public Pedido findPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    

    public int getPedidoCount(Calendar data){
        EntityManager em = getEntityManager();
        try{
            Date data1, data2;
            
            String query = "SELECT COUNT(p) FROM Pedido p WHERE p.data BETWEEN :data1 AND :data2";
            
            Query q = em.createQuery(query);

            data.set(Calendar.DATE, 1);
            data1 = data.getTime();
            data.add(Calendar.MONTH, 1);
            data.add(Calendar.DATE, -1);
            data2 = data.getTime();
            
            q.setParameter("data1", data1, TemporalType.DATE);
            q.setParameter("data2", data2, TemporalType.DATE);
            
            return ((Long)q.getSingleResult()).intValue();
        }finally{
            em.close();
        }
    }
            
    
    
    
}
