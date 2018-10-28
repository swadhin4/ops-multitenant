package com.pms.web.service.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
   @Override
   @Autowired
   public void setAuthenticationManager(AuthenticationManager authenticationManager) {
      super.setAuthenticationManager(authenticationManager);
   }
   @Override
   public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
      String extraParam = request.getParameter("extraParam");
      String username = request.getParameter("j_username");
      String passw = request.getParameter("j_password");

      CustomAuthenticationToken authRequest = new CustomAuthenticationToken(username, passw, extraParam);
      authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

      return this.getAuthenticationManager().authenticate(authRequest);
   }
}