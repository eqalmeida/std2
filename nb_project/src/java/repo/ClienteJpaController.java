/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import model.Cliente;
import model.Pedido;

/**
 *
 * @author eqalmeida
 */
public class ClienteJpaController extends AbstractJpaController<Cliente> {

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
        super(Cliente.class);
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

    public int getClienteCount() {
        EntityManager em = getEntityManager();

        String query = ("SELECT COUNT(c) FROM Cliente c" + filterNome);
        Query q = em.createQuery(query);
        return ((Long) q.getSingleResult()).intValue();
    }
}
