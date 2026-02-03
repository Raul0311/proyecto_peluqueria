package com.peluqueria.front_end.adapter.in.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.peluqueria.front_end.application.ports.out.UserPortOut;
import com.peluqueria.front_end.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * The Class CustomAuthenticationProvider.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    /** The Constant USER_NOT_EXISTS. */
    private static final String USER_NOT_EXISTS = "Credenciales inválidas";
    
    /** The user port out. */
    private final UserPortOut userPortOut;
    
    /**
     * Authenticate.
     *
     * @param authentication the authentication
     * @return the authentication
     * @throws AuthenticationException the authentication exception
     */
    @Override
    public Authentication authenticate(Authentication authentication) {

        HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        HttpSession session = request.getSession(true);

        User user = new User();
        user.setUsername(authentication.getName());
        user.setPassw(authentication.getCredentials().toString());

        // LOGIN
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (attemptLogin(user, session, authorities)) {
            return new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassw(),
                    authorities
            );
        }

        throw new BadCredentialsException(USER_NOT_EXISTS);
    }
    
    /**
     * Attempt login.
     *
     * @param user the user
     * @param session the session
     * @param reg the reg
     * @param log the log
     * @param authorities the authorities
     * @return true, if successful
     */
    private boolean attemptLogin(User user, HttpSession session, List<GrantedAuthority> authorities) {
        String userJson = userPortOut.login(user, session.getId());
        if (userJson == null) return false;

        try {
            ObjectMapper mapper = new ObjectMapper();
            User data = mapper.readValue(userJson, User.class);
            
            // Actualizar el objeto user con los datos del JSON
            user.setId(data.getId());
            user.setUserToken(data.getUserToken());
            
            session.setAttribute("userToken", data.getUserToken());

            if (data.getRolesStr() != null && !data.getRolesStr().isEmpty()) {
                Arrays.stream(data.getRolesStr().split("\\|"))
                    .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toUpperCase())));
            }
            return true;
        } catch (Exception e) {
            // Excepción específica (Aviso SonarQube)
            throw new DatabaseOperationException("Error procesando JSON de usuario", e);
        }
    }

    /**
     * Supports.
     *
     * @param authentication the authentication
     * @return true, if successful
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    /**
     * The Class UserExistsException.
     */
    @SuppressWarnings("serial")
    public static class UserExistsException extends AuthenticationException {
        
        /**
         * Instantiates a new user exists exception.
         *
         * @param msg the msg
         */
        public UserExistsException(String msg) {
            super(msg);
        }
    }
    
    /**
     * The Class EmailDoesNotExist.
     */
    @SuppressWarnings("serial")
    public static class EmailDoesNotExist extends AuthenticationException {
        
        /**
         * Instantiates a new email does not exist.
         *
         * @param msg the msg
         */
        public EmailDoesNotExist(String msg) {
            super(msg);
        }
    }
    
    /**
     * The Class PasswordDoesNotReset.
     */
    @SuppressWarnings("serial")
    public static class PasswordDoesNotReset extends AuthenticationException {
        
        /**
         * Instantiates a new password does not reset.
         *
         * @param msg the msg
         */
        public PasswordDoesNotReset(String msg) {
            super(msg);
        }
    }
    
    /**
     * The Class DatabaseOperationException.
     */
    @SuppressWarnings("serial")
	public class DatabaseOperationException extends RuntimeException {
	    
    	/**
    	 * Instantiates a new database operation exception.
    	 *
    	 * @param message the message
    	 * @param cause the cause
    	 */
    	public DatabaseOperationException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}
}
