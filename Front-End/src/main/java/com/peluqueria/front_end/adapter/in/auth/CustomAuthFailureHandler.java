package com.peluqueria.front_end.adapter.in.auth;

import java.io.IOException;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.peluqueria.front_end.adapter.in.auth.CustomAuthenticationProvider.EmailDoesNotExist;
import com.peluqueria.front_end.adapter.in.auth.CustomAuthenticationProvider.PasswordDoesNotReset;
import com.peluqueria.front_end.adapter.in.auth.CustomAuthenticationProvider.UserExistsException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom Auth Failure Handler que envía Status de Error y redirige mediante Refresh Header.
 */
@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    /**
     * On authentication failure.
     *
     * @param request the request
     * @param response the response
     * @param exception the exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            org.springframework.security.core.AuthenticationException exception) throws ServletException, IOException {
        
    	String redirectUrl = null;
        int status;

        if (exception instanceof UserExistsException) {
            status = HttpServletResponse.SC_CONFLICT; // 409
            redirectUrl = "/auth/register?error=userexists";
        } else if (exception instanceof EmailDoesNotExist) {
            status = HttpServletResponse.SC_NOT_FOUND; // 404
            redirectUrl = "/auth/forgot-password?error=notfound";
        } else if (exception instanceof PasswordDoesNotReset) {
            status = HttpServletResponse.SC_BAD_REQUEST; // 400
            redirectUrl = "/auth/reset-password?error=true";
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED; // 401 por defecto
            redirectUrl = "/auth?error=true";
        }
            	
    	response.setStatus(status);

        response.setHeader("Refresh", "0; URL=" + redirectUrl);

        response.getWriter().write("Status: " + status + ". Redirecting to " + redirectUrl);
    }
}