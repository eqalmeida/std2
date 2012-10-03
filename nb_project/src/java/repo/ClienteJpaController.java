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
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Cliente;
import model.Pedido;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class ClienteJpaController extends AbstractJpaController {

    private String sortedField = null;
    private String order = null;
    private String filterNome = "";

    public void setFilterNome(String filterNome) {
        if (filterNome != null && filterNome.length() > 0) {
            this.filterNome = (" WHERE c.nome LIKE '" + filterNome + "%'");
        } else {
            this.filterNome = "";
        }

    }

    public void setSortedField(String sortedField, String order) {
        this.sortedField = sortedField;
        this.order = order;
    }

    public ClienteJpaController() {
    }

    public void create(Cliente cliente) {
        EntityManager em = null;

        em = getEntityManager();
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cliente = em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;

        em = getEntityManager();
        em.getTransaction().begin();
        Cliente cliente;
        try {
            cliente = em.getReference(Cliente.class, id);
            cliente.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
        }
        em.remove(cliente);
        em.getTransaction().commit();
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

        String query = ("SELECT c FROM Cliente c" + filterNome);

        if (sortedField != null) {
            query += (" ORDER BY c." + sortedField + " " + order);
        }


        Query q = em.createQuery(query);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public List<Pedido> findPedidos(Cliente cliente) {
        EntityManager em = getEntityManager();

        Query query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id = :cid");
        query.setParameter("cid", cliente.getId());
        return query.getResultList();
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Cliente.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();

        return em.find(Cliente.class, id);
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();

        String query = ("SELECT COUNT(c) FROM Cliente c" + filterNome);
        Query q = em.createQuery(query);
        return ((Long) q.getSingleResult()).intValue();
    }
}
