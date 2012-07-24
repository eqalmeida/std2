/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.List;
import repo.exceptions.IllegalOrphanException;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public interface IDBService<T> {

    public void edit(T t) throws IllegalOrphanException, NonexistentEntityException, Exception;

    public void create(T t);

    public List<T> findEntities(int maxResults, int firstResult);

    public T find(Integer id);

    public void setSortedField(String sortedField, String order);
    
    public int getCount();
}
