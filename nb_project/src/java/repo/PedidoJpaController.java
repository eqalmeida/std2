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
import javax.persistence.TemporalType;
import model.Boleto;
import model.Pedido;
import model.PedidoPag;
import model.PedidoProduto;
import model.PedidoStatus;
import model.Produto;
import model.Usuario;
import org.primefaces.model.SortOrder;

/**
 *
 * @author eqalmeida
 */
public class PedidoJpaController extends AbstractJpaController<Pedido> {

    public PedidoJpaController() {
        super(Pedido.class);
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
