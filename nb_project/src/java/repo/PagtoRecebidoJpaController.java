/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import javax.persistence.EntityManager;
import model.PagtoRecebido;
import model.Usuario;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class PagtoRecebidoJpaController extends AbstractJpaController<PagtoRecebido> {

    public PagtoRecebidoJpaController() {
        super(PagtoRecebido.class);
    }

    @Override
    public void create(PagtoRecebido pagtoRecebido) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Usuario recebUsuario = pagtoRecebido.getRecebUsuario();
        if (recebUsuario != null) {
            recebUsuario = em.getReference(recebUsuario.getClass(), recebUsuario.getId());
            pagtoRecebido.setRecebUsuario(recebUsuario);
        }
        em.persist(pagtoRecebido);
        em.getTransaction().commit();
    }

    @Override
    public void edit(PagtoRecebido pagtoRecebido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PagtoRecebido persistentPagtoRecebido = em.find(PagtoRecebido.class, pagtoRecebido.getId());
            Usuario recebUsuarioOld = persistentPagtoRecebido.getRecebUsuario();
            Usuario recebUsuarioNew = pagtoRecebido.getRecebUsuario();
            if (recebUsuarioNew != null) {
                recebUsuarioNew = em.getReference(recebUsuarioNew.getClass(), recebUsuarioNew.getId());
                pagtoRecebido.setRecebUsuario(recebUsuarioNew);
            }
            pagtoRecebido = em.merge(pagtoRecebido);
//            if (recebUsuarioOld != null && !recebUsuarioOld.equals(recebUsuarioNew)) {
//                recebUsuarioOld.getPagtoRecebidoCollection().remove(pagtoRecebido);
//                recebUsuarioOld = em.merge(recebUsuarioOld);
//            }
//            if (recebUsuarioNew != null && !recebUsuarioNew.equals(recebUsuarioOld)) {
//                recebUsuarioNew.getPagtoRecebidoCollection().add(pagtoRecebido);
//                recebUsuarioNew = em.merge(recebUsuarioNew);
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pagtoRecebido.getId();
                if (super.find(id) == null) {
                    throw new NonexistentEntityException("The pagtoRecebido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }
}
