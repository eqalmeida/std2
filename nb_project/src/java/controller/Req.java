/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.Config;
import repo.ConfigJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean
@RequestScoped
public class Req {
    
    private Config config = null;

    /**
     * Creates a new instance of Req
     */
    public Req() {
    }
    
    public Config getConfig() {
        if(config == null){
            config = new ConfigJpaController().findConfig();
        }
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
