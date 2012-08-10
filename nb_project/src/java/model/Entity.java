/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author eqalmeida
 */
@SuppressWarnings("serial")
public abstract class Entity implements Serializable{
    public abstract Long getId();

    @Override
    public boolean equals(Object obj){
        if(this == obj)return true;
        
        if(obj == null)return false;
        
        if(getClass() != obj.getClass()) return false;
        
        Entity other = (Entity) obj;
        if(getId() != other.getId()) return false;
        
        return true;
    }
    
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + Long.signum(getId() ^ (getId() >>> 32));
        return result;
    }
    
}
