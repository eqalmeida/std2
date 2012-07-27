/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author eqalmeida
 */
@ManagedBean
@ApplicationScoped
public class App {
    
    private List<SelectItem> meses = null;

    /**
     * Creates a new instance of App
     */
    public App() {
    }

    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    public List<SelectItem> getMeses() {
        
        if(meses == null){
            
            meses = new ArrayList<SelectItem>();
            
            SelectItem it;
            
            it = new SelectItem(0, "Janeiro");
            meses.add(it);
            
            it = new SelectItem(1, "Fevereiro");
            meses.add(it);
            
            it = new SelectItem(2, "Março");
            meses.add(it);
            
            it = new SelectItem(3, "Abril");
            meses.add(it);
            
            it = new SelectItem(4, "Maio");
            meses.add(it);
            
            it = new SelectItem(5, "Junho");
            meses.add(it);
            
            it = new SelectItem(6, "Julho");
            meses.add(it);
            
            it = new SelectItem(7, "Agosto");
            meses.add(it);
            
            it = new SelectItem(8, "Setembro");
            meses.add(it);
            
            it = new SelectItem(9, "Outubro");
            meses.add(it);
            
            it = new SelectItem(10, "Novembro");
            meses.add(it);
            
            it = new SelectItem(11, "Dezembro");
            meses.add(it);
            
        }
        
        return meses;
    }
    
}
