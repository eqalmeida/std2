/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.Config;
import repo.ConfigJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "configMB")
@RequestScoped
public class ConfigBean extends ControllerBase implements Serializable {

    private Config config = null;
    private ConfigJpaController service ;
    
   
    
    public ConfigBean() {
    }
    
    @PostConstruct
    private void init(){
        service = new ConfigJpaController();
    }
    
    public Config getConfig() {
        if(config == null){
            config = service.findConfig();
        }
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    
    public void gravar(){
        try{
            service.edit(config);
            config = null;
            addMessage("Gravado com sucesso!");
            
        }
        catch(Exception ex){
            addErrorMessage(ex.getLocalizedMessage());
        }
    }
    
}
