/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author eqalmeida
 */
public class PagtoServiceFactory {
    
    private static IPagtoService pagtoService = null;
    
    public static IPagtoService getPagtoService(){
        if(pagtoService == null){
            pagtoService = new PagtoService();
        }
        return pagtoService;
    }
    
}
