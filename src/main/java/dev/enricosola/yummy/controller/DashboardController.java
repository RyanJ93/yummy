package dev.enricosola.yummy.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class DashboardController {
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model){
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        model.addAttribute("userDetails", userDetails);
        return "dashboard";
    }
}
