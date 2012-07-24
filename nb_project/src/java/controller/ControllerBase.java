/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Usuario;

/**
 *
 * @author eqalmeida
 */
public abstract class ControllerBase {

    public ControllerBase() {
    }
    private static EntityManagerFactory emf = null;
    
    static public Usuario getUsuarioLogado(){
        FacesContext context = FacesContext.getCurrentInstance();

        Usuario u = context.getApplication().evaluateExpressionGet(context, "#{loginMB}", LoginBean.class).getUsuario();

        return u;
    }
    

    static public boolean verificaLogin(String req) {


        Usuario u = getUsuarioLogado();

        if (u == null) {
            return false;
        }

        return true;

    }

    static public EntityManagerFactory getEmf() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("std_lojaPU");
        }
        return (emf);
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addErrorMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
