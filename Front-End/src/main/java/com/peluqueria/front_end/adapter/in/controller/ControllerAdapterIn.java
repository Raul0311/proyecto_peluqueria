package com.peluqueria.front_end.adapter.in.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.peluqueria.front_end.application.ports.in.UserPortIn;
import com.peluqueria.front_end.domain.User;

import lombok.RequiredArgsConstructor;

/**
 * The Class ControllerAdapterIn.
 */
@Controller
@RequiredArgsConstructor
public class ControllerAdapterIn {
	
	/** The user port in. */
	private final UserPortIn userPortIn;

    /**
     * Root.
     *
     * @return the string
     */
    // Redirige "/" a "/login"
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }
    
    
    /**
     * Index.
     *
     * @return the string
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * Auth.
     *
     * @param error the error
     * @param logout the logout
     * @param registered the registered
     * @param auth the auth
     * @return the model and view
     */
    // Página de login
    @GetMapping("/auth")
    public ModelAndView auth(@RequestParam(required = false) String error,
                              @RequestParam(required = false) String logout,
                              Authentication auth) {
    	
    	String view = userPortIn.load(auth);
    	
    	ModelAndView model = new ModelAndView(view);

        if ("true".equals(error)) {
        	model.addObject("error", "Usuario o contraseña incorrectos.");
        }
    	
        return model;
    }
    
    /**
     * Home.
     *
     * @return the string
     */
    // Página privada
    @GetMapping("/private/home")
    public String home() {
        return "private/home"; 
    }
    
    /**
     * Admin.
     *
     * @return the string
     */
    // Página privada
    @GetMapping("/private/admin")
    public String admin() {
        return "private/admin"; 
    }
    
    /**
     * Register.
     *
     * @param error the error
     * @param auth the auth
     * @return the model and view
     */
    @PostMapping("/register")
    public ModelAndView register(String error, String registered, User user) {
    	
		String view = userPortIn.register(user);
    	
    	ModelAndView model = new ModelAndView(view);
    	
    	if ("true".equals(registered)) {
    		model.addObject("msg", "La cuenta se ha creado correctamente.");
        }
    	
    	if ("userexists".equals(error)) {
        	model.addObject("error", "El nombre de usuario ya existe.");
        }
    	
    	return model;
    }
    
    @GetMapping("/register")
    public ModelAndView register(@RequestParam(required = false) String error) {
    	
    	return new ModelAndView("register");
    }
    
    /**
     * Forgot password.
     *
     * @param error the error
     * @param email the email
     * @return the model and view
     */
    @PostMapping("/forgot-password")
    public ModelAndView forgotPassword(@RequestParam(required = false) String error, @RequestParam String email) {
    	
    	return new ModelAndView(userPortIn.forgotPassword(email));
    }
    
    /**
     * Gets the forgot password.
     *
     * @param error the error
     * @return the forgot password
     */
    @GetMapping("/forgot-password")
    public ModelAndView forgotPassword(@RequestParam(required = false) String error) {
    	
    	return new ModelAndView("forgot-password");
    }
    
    /**
     * Reset password.
     *
     * @param error the error
     * @param newPass the new pass
     * @param token the token
     * @return the model and view
     */
    @PostMapping("/reset-password")
    public ModelAndView resetPassword(@RequestParam(required = false) String error, @RequestParam String newPass, 
																					@RequestParam String token) {

        return new ModelAndView(userPortIn.resetPassword(newPass, token));
    }
    
    /**
     * Reset password.
     *
     * @param error the error
     * @return the model and view
     */
    @GetMapping("/reset-password")
    public ModelAndView resetPassword(@RequestParam(required = false) String error) {

        return new ModelAndView("reset-password");
    }
}
