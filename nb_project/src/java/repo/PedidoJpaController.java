/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Boleto;
import model.Pedido;
import model.PedidoPag;
import model.PedidoProduto;
import model.PedidoStatus;
import model.Produto;
import model.Usuario;
import org.primefaces.model.SortOrder;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class PedidoJpaController extends AbstractJpaController {

    public PedidoJpaController() {
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
        }
    }

    public void changeStatus(Pedido pedido, Short status, Usuario usuario, String just) throws IllegalAccessException, Exception {
        EntityManager em = null;
        try {
            if (status == Boleto.PAGO || status == Boleto.PAGO_PARCIAL) {
                throw new IllegalAccessException("STATUS INVÁLIDO!");
            }
            em = getEntityManager();
            em.getTransaction().begin();
            //try {
            pedido = em.find(Pedido.class, pedido.getId());

            short statusAnt = pedido.getStatus();

            pedido.setStatus(status);
            em.merge(pedido);

            for (PedidoPag pag : pedido.getPagamentos()) {
                for (Boleto b : pag.getParcelas()) {
                    if (b.getStatus() != Boleto.PAGO) {

                        int boletoId = b.getId();

                        b = em.find(Boleto.class, boletoId);

                        if (status == Boleto.ATIVO && b.getValorPago() != null && b.getValorPago().doubleValue() > 0.0) {
                            b.setStatus(Boleto.PAGO_PARCIAL);
                        } else {
                            b.setStatus(status);
                        }

                        em.merge(b);
                    }
                }
            }

            //
            // Tratamento de estoque
            //
            if (status == Boleto.CANCELADO && statusAnt == Boleto.ATIVO) {
                for (PedidoProduto p : pedido.getItens()) {

                    Produto pr = em.find(Produto.class, p.getProduto().getId());

                    Integer qtd = pr.getQtdEstoque();
                    if (qtd >= 0) {

                        pr.setQtdEstoque(qtd + 1);
                        em.merge(pr);
                    }
                }
            } else if (statusAnt == Boleto.CANCELADO && status == Boleto.ATIVO) {
                for (PedidoProduto p : pedido.getItens()) {

                    Produto pr = em.find(Produto.class, p.getProduto().getId());

                    Integer qtd = pr.getQtdEstoque();

                    if (qtd == 0) {
                        throw new IllegalAccessException("O produto " + pr.getDescricaoGeral() + " está sem estoque!");
                    } else if (qtd > 0) {

                        pr.setQtdEstoque(qtd - 1);
                        em.merge(pr);
                    }
                }
            }

            usuario = em.getReference(Usuario.class, usuario.getId());

            PedidoStatus st = new PedidoStatus();
            st.setPedido(pedido);
            st.setUsuario(usuario);
            st.setAlteracao("Status alterado para " + status);
            st.setJustificativa(just);
            em.persist(st);

            int pedidoId = pedido.getId();

            em.getTransaction().commit();

            pedido = em.find(Pedido.class, pedidoId);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception(e.getMessage());

        }

    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResults, Calendar data, SortOrder sortOrder, String sortField) {
        EntityManager em = getEntityManager();
        try {
            Date data1, data2;

            String query = "SELECT p FROM Pedido p WHERE p.data BETWEEN :data1 AND :data2";
            query += " ORDER BY ";
            query += (" p." + sortField);
            if (sortOrder == SortOrder.ASCENDING) {
                query += " ASC";
            } else if (sortOrder == SortOrder.DESCENDING) {
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
        } finally {
        }
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Pedido.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Pedido findPedido(Integer id) {
        EntityManager em = getEntityManager();
        return em.find(Pedido.class, id);
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Pedido> rt = cq.from(Pedido.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public int getPedidoCount(Calendar data) {
        EntityManager em = getEntityManager();

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

        return ((Long) q.getSingleResult()).intValue();
    }
}
