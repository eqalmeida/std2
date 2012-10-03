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

        // TODO Auto-generated method stub
        EntityManager entityManager = this.factory.createEntityManager();
        request.setAttribute("entityManager", entityManager);

        try {
            chain.doFilter(request, response);
        } finally {
            entityManager.close();
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        this.factory = Persistence.createEntityManagerFactory("std_lojaPU");
    }
}
