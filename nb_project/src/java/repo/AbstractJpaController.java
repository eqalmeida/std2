package repo;

import controller.ControllerBase;
import java.io.Serializable;
import javax.persistence.EntityManager;

public abstract class AbstractJpaController implements Serializable {
    
    protected EntityManager getEntityManager(){
        return ControllerBase.getEntityManager();
    } 
    
}
