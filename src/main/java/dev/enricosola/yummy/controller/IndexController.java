package dev.enricosola.yummy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Authentication authentication){
        if ( authentication != null ){
            return "redirect:/dashboard";
        }
        return "index";
    }
}
