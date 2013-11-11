package filter;


import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(servletNames = {"Faces Servlet"})
public class JPAFilter implements Filter {

    private EntityManagerFactory factory;

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        this.factory.close();

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String requestUrl = req.getRequestURL().toString();
        
        EntityManager entityManager = null;
        
        // Verifica se é necessário criar o EntityManager
        if (isJSFView(requestUrl)) {
            // TODO Auto-generated method stub
            entityManager = this.factory.createEntityManager();
            request.setAttribute("entityManager", entityManager);
            //System.out.println(requestUrl);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            if(entityManager!=null){
            entityManager.close();
            }
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        this.factory = Persistence.createEntityManagerFactory("std_lojaPU");
    }
    
    private boolean isJSFView(String url){
        if (url.contains("javax.faces.resource")){
            return false;
        } 
        return true;
    }
}
