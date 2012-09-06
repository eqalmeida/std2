/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Usuario;
import org.primefaces.context.RequestContext;
import repo.UsuarioJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "usuarioMB")
@ViewScoped
public class UsuarioBean extends ControllerBase implements Serializable {

    private List<Usuario> lista = null;
    private Usuario selected = null;
    private UsuarioJpaController service = null;

    /**
     * Creates a new instance of UsuarioBean
     */
    public UsuarioBean() {
    }

    @PostConstruct
    private void init() {
        service = new UsuarioJpaController();
    }

    public List<Usuario> getLista() {

        lista = service.findUsuarioEntities();
        return lista;
    }

    public void setLista(List<Usuario> lista) {
        this.lista = lista;
    }

    public Usuario getSelected() {
        return selected;
    }

    public void setSelected(Usuario usuario) {
        this.selected = usuario;
    }

    public void editar(Long id) {
        if (getUsuarioLogado().isAdmin()) {
            selected = service.findUsuario(id);
            RequestContext.getCurrentInstance().execute("uDlg.show()");
        } else {
            addErrorMessage("Acesso negado!");
        }
    }

    public void novo() {
        selected = new Usuario();
        selected.setAdmin(false);
        selected.setAtivo(true);
        RequestContext.getCurrentInstance().execute("uDlg.show()");
    }

    public void editarSenha() {
        RequestContext.getCurrentInstance().execute("usDlg.show()");
    }

    public void excluir(Long id) {
        if (getUsuarioLogado().isAdmin()) {
            try {
                service.destroy(id);
            } catch (Exception ex) {
                addErrorMessage("Não foi possível excluir!");
            }
        } else {
            addErrorMessage("Acesso Negado!");
        }
    }

    public void gravar() {

        if (getUsuarioLogado().isAdmin()) {
            try {

                if (selected.getId() > 0) {
                    //Editando
                    service.edit(selected);
                } else {
                    //Inserindo
                    if (selected.getSenha() == null) {
                        selected.setSenha("123456");
                    }
                    service.create(selected);
                }

                RequestContext.getCurrentInstance().execute("uDlg.hide()");
                RequestContext.getCurrentInstance().execute("usDlg.hide()");

                addMessage("Gravado com sucesso");


            } catch (Exception ex) {
                addErrorMessage(ex.getMessage());
            }
        }
        else{
            addErrorMessage("Acesso Negado!");
        }
    }
}
