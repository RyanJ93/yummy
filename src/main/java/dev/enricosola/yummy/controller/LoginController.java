package dev.enricosola.yummy.controller;

import static org.springframework.security.web.WebAttributes.AUTHENTICATION_EXCEPTION;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import dev.enricosola.yummy.form.user.UserLoginForm;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginGET(HttpServletRequest httpServletRequest, Model model){
        HttpSession httpSession = httpServletRequest.getSession(false);
        String generalErrorMessage = null;
        if ( httpSession != null ){
            AuthenticationException ex = (AuthenticationException)httpSession.getAttribute(AUTHENTICATION_EXCEPTION);
            generalErrorMessage = ex == null ? null : ex.getMessage();
        }
        model.addAttribute("generalErrorMessage", generalErrorMessage);
        model.addAttribute("form", new UserLoginForm());
        return "user/login";
    }
}
