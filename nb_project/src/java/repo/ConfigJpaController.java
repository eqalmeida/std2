/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Config;

/**
 *
 * @author eqalmeida
 */
public class ConfigJpaController extends AbstractJpaController<Config> {

    public ConfigJpaController() {
        super(Config.class);
    }
    
    

    public Config findConfig() {

        Config c = super.find((short)1);

        if (c == null) {
            c = new Config((short) 1);
            try {
                super.create(c);
            } catch (Exception ex) {
                Logger.getLogger(ConfigJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return c;
    }
}
