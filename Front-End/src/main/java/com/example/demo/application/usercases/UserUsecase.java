package com.example.demo.application.usercases;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.adapter.in.auth.CustomAuthenticationProvider.EmailDoesNotExist;
import com.example.demo.adapter.in.auth.CustomAuthenticationProvider.PasswordDoesNotReset;
import com.example.demo.adapter.in.auth.CustomAuthenticationProvider.UserExistsException;
import com.example.demo.application.ports.in.UserPortIn;
import com.example.demo.application.ports.out.EmailPortOut;
import com.example.demo.application.ports.out.UserPortOut;
import com.example.demo.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class UserUsecase.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserUsecase implements UserPortIn {
	
	/** The Constant EMAIL_NOT_EXISTS. */
    private static final String EMAIL_NOT_EXISTS = "No existe ninguna cuenta con ese correo.";
    
    /** The Constant PASSWORD_NOT_RESET. */
    private static final String PASSWORD_NOT_RESET = "La contraseña no se ha podido cambiar, intentálo de nuevo más tarde.";
    
    /** The Constant USER_EXISTS. */
	private static final String USER_EXISTS = "El usuario ya existe";
	
	/** The user port out. */
	private final UserPortOut userPortOut;
	
	/** The email port out. */
	private final EmailPortOut emailPortOut;

	/**
	 * Load.
	 *
	 * @param auth the auth
	 * @return the string
	 * @throws BadCredentialsException the bad credentials exception
	 */
	@Override
	public String load(Authentication auth) throws BadCredentialsException {
		try {
	        // Solo redirige a /private/home si está autenticado
	        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
	        	// Verificamos si tiene el rol de ADMIN
	            boolean isAdmin = auth.getAuthorities().stream()
	                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

	            if (isAdmin) {
	                return "redirect:/private/admin";
	            } else {
	                return "redirect:/private/home";
	            }
	        }
	        
	        return "auth";
		} catch(Exception e) {
			log.error("Error al cargar la ruta de autenticación: {}", e.getMessage(), e);
			
			return "error";
		}
	}
	
	/**
	 * Forgot password.
	 *
	 * @param email the email
	 * @return the string
	 */
	@Override
	public String forgotPassword(String email) {
        String token = userPortOut.forgotPassword(email);

        if (token != null) {
            emailPortOut.sendResetPasswordEmail(email, token);
            
            return "redirect:/forgot-password?msg=sent";
        }

        throw new EmailDoesNotExist(EMAIL_NOT_EXISTS);
	}
	
	/**
	 * Reset password.
	 *
	 * @param password the password
	 * @param token the token
	 * @return the string
	 */
	@Override
	public String resetPassword(String password, String token) {
		Integer status = userPortOut.resetPassword(
                password,
                token
        );

        if (status != null && status == 1) {
        	return "redirect:/reset-password?msg=resetpass";
        }

        throw new PasswordDoesNotReset(PASSWORD_NOT_RESET);
	}
	
	/**
	 * Register.
	 *
	 * @param user the user
	 * @return the string
	 */
	@Override
	public String register(User user) {
		HttpServletRequest request =
	            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
	                .getRequest();

        HttpSession session = request.getSession(true);
        
        if (userPortOut.register(user, session.getId()) != null) {
        	
        	return "redirect:/auth?registered=true";
        }
        throw new UserExistsException(USER_EXISTS);
        
	}
}
