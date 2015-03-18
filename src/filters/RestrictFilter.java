package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logovanje.LoginUser;

@WebFilter("/userpages/*")
public class RestrictFilter implements Filter {
    private FilterConfig fc;

    public RestrictFilter() {

    }

    public void destroy() {

    }


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        LoginUser logUser=(LoginUser) request.getSession().getAttribute("loginUser");

        String loginURL = request.getContextPath() + "/loginUser.xhtml";
        if(logUser != null && logUser.isLoggedIn()){
            chain.doFilter(req, res);
        }
        else{
            response.sendRedirect(loginURL);
        }
    }


    public void init(FilterConfig fConfig) throws ServletException {
        this.fc = fConfig;
    }
}
