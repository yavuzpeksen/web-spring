package com.ypeksen.web.spring.controllers;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter{

  private FilterConfig config = null;

  public void init(FilterConfig config) throws ServletException {
      this.config = config;
      config.getServletContext().log("Initializing SessionCheckerFilter");
  }
  
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		//
		// Check to see if user's session attribute contains an attribute
		// named AUTHENTICATED. If the attribute is not exists redirect
		// user to the login page.
		//
		
		HttpSession session = request.getSession(false);
		
		System.out.println(request.getRequestURI() + " " + session);
		
		
/*		if (!request.getRequestURI().endsWith("login.jsp") && session.getAttribute("AUTHENTICATED") == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
		}*/
		
		if(!request.getRequestURI().endsWith("register.html") && session == null) {
			//response.sendRedirect(request.getContextPath() + "/register.html");
			//return;
		}
		chain.doFilter(req, res);
		
  }
  public void destroy() {
    config.getServletContext().log("Destroying SessionCheckerFilter");
  }

}