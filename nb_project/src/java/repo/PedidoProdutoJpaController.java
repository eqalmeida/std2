/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.PedidoProduto;
import model.PedidoProdutoPK;
import model.Produto;
import repo.exceptions.NonexistentEntityException;
import repo.exceptions.PreexistingEntityException;

/**
 *
 * @author eqalmeida
 */
public class PedidoProdutoJpaController extends AbstractJpaController<PedidoProduto> {

    public PedidoProdutoJpaController() {
        super(PedidoProduto.class);
    }

    @Override
    public void create(PedidoProduto pedidoProduto) throws PreexistingEntityException, Exception {
        if (pedidoProduto.getPedidoProdutoPK() == null) {
            pedidoProduto.setPedidoProdutoPK(new PedidoProdutoPK());
        }
        pedidoProduto.getPedidoProdutoPK().setPedido(pedidoProduto.getPedido().getId());
        pedidoProduto.getPedidoProdutoPK().setProduto(pedidoProduto.getProduto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto produto = pedidoProduto.getProduto();
            if (produto != null) {
                produto = em.getReference(produto.getClass(), produto.getId());
                pedidoProduto.setProduto(produto);
            }
            em.persist(pedidoProduto);
            if (produto != null) {
                produto.getPedidoProdutoCollection().add(pedidoProduto);
                produto = em.merge(produto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (super.find(pedidoProduto.getPedidoProdutoPK()) != null) {
                throw new PreexistingEntityException("PedidoProduto " + pedidoProduto + " already exists.", ex);
            }
            throw ex;
        }
    }

    @Override
    public void edit(PedidoProduto pedidoProduto) throws NonexistentEntityException, Exception {
        pedidoProduto.getPedidoProdutoPK().setPedido(pedidoProduto.getPedido().getId());
        pedidoProduto.getPedidoProdutoPK().setProduto(pedidoProduto.getProduto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PedidoProduto persistentPedidoProduto = em.find(PedidoProduto.class, pedidoProduto.getPedidoProdutoPK());
            Produto produtoOld = persistentPedidoProduto.getProduto();
            Produto produtoNew = pedidoProduto.getProduto();
            if (produtoNew != null) {
                produtoNew = em.getReference(produtoNew.getClass(), produtoNew.getId());
                pedidoProduto.setProduto(produtoNew);
            }
            pedidoProduto = em.merge(pedidoProduto);
            if (produtoOld != null && !produtoOld.equals(produtoNew)) {
                produtoOld.getPedidoProdutoCollection().remove(pedidoProduto);
                produtoOld = em.merge(produtoOld);
            }
            if (produtoNew != null && !produtoNew.equals(produtoOld)) {
                produtoNew.getPedidoProdutoCollection().add(pedidoProduto);
                produtoNew = em.merge(produtoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PedidoProdutoPK id = pedidoProduto.getPedidoProdutoPK();
                if (super.find(id) == null) {
                    throw new NonexistentEntityException("The pedidoProduto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public List<PedidoProduto> findPedidoProdutoEntities(Integer pedidoId) {
        EntityManager em = getEntityManager();

        Query query = em.createQuery("SELECT p FROM PedidoProduto p WHERE p.pedido.id = :pedidoId");
        query.setParameter("pedidoId", pedidoId);
        return query.getResultList();
    }
}
