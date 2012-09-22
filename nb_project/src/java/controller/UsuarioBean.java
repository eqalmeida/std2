/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import facade.UsuarioFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Usuario;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "usuarioMB")
@ViewScoped
public class UsuarioBean extends ControllerBase implements Serializable {

    private List<Usuario> lista = null;
    private Usuario selected = null;
    private UsuarioFacade facade = null;


    @PostConstruct
    private void init() {
        facade = new UsuarioFacade();
    }

    public List<Usuario> getLista() {

        lista = facade.findAll();
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
            selected = facade.find(id);
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
                selected = facade.find(id);
                facade.remove(selected);
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
                    facade.edit(selected);
                } else {
                    //Inserindo
                    if (selected.getSenha() == null) {
                        selected.setSenha("123456");
                    }
                    facade.create(selected);
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
