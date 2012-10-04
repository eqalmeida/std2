/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Usuario;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class UsuarioJpaController extends AbstractJpaController<Usuario> {

    public UsuarioJpaController() {
        super(Usuario.class);
    }

    private void verificaAdmin(Usuario usuario) throws Exception {
        if (usuario.isAdmin() && usuario.getAtivo()) {
            return;
        }

        List<Usuario> lista = super.findAll();

        for (Usuario u : lista) {
            if (u.isAdmin() && u.getAtivo() && (!u.getId().equals(usuario.getId()))) {
                return;
            }
        }

        throw new Exception("É necessário manter pelo menos um usuário como ADMIN");
    }

    private void verificaExcluir(Long usuarioId) throws Exception {

        Usuario usuario = super.find(usuarioId);

        List<Usuario> lista = super.findAll();

        for (Usuario u : lista) {
            if (u.isAdmin() && u.getAtivo() && (!u.getId().equals(usuario.getId()))) {
                return;
            }
        }

        throw new Exception("É necessário manter pelo menos um usuário como ADMIN");
    }

    @Override
    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        verificaAdmin(usuario);
        super.edit(usuario);
    }

    
    /**
     *
     * @param usuario
     * @throws Exception
     */
    @Override
    public void remove(Usuario usuario){
        try {
            verificaExcluir(usuario.getId());
            super.remove(usuario);
        } catch (Exception ex) {
            
        }
    }

}
