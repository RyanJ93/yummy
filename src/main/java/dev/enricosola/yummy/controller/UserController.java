package dev.enricosola.yummy.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import dev.enricosola.yummy.form.user.UserPasswordChangeForm;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import dev.enricosola.yummy.support.FlashMessagesInjector;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import dev.enricosola.yummy.form.user.UserCreateForm;
import org.springframework.validation.BindingResult;
import dev.enricosola.yummy.form.user.UserEditForm;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import dev.enricosola.yummy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import dev.enricosola.yummy.entity.User;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final int PAGE_SIZE = 20;

    private final UserService userService;

    private void injectFlashMessages(HttpServletRequest httpServletRequest, Model model){
        HashMap<String, String> lastActionMessagesMapping = new HashMap<>();
        lastActionMessagesMapping.put("passwordChanged", "User password has successfully been changed!");
        lastActionMessagesMapping.put("userCreated", "User has successfully been created!");
        lastActionMessagesMapping.put("userUpdated", "User has successfully been updated!");
        lastActionMessagesMapping.put("userDeleted", "User has successfully been removed!");
        FlashMessagesInjector flashMessagesInjector = new FlashMessagesInjector(lastActionMessagesMapping);
        flashMessagesInjector.injectFlashMessages(httpServletRequest, model);
    }

    private boolean isAdminUser(Authentication authentication){
        List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return authorities.contains("ROLE_ADMIN");
    }

    private boolean adminUserCheck(Authentication authentication, RedirectAttributes redirectAttributes){
        if ( !this.isAdminUser(authentication) ){
            String message = "Regular users cannot create or edit users, you must authenticate as an administrator.";
            redirectAttributes.addFlashAttribute("generalErrorMessage", message);
            return false;
        }
        return true;
    }

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public String list(
        @RequestParam(required = false, name = "page") String page,
        HttpServletRequest httpServletRequest,
        Authentication authentication,
        Model model
    ){
        int pageNumber = page == null || page.isEmpty() ? 1 : Integer.parseInt(page);
        int skip = ( pageNumber - 1 ) * UserController.PAGE_SIZE;
        List<User> userList = this.userService.getAll();
        int numPages = userList.size() / UserController.PAGE_SIZE;
        userList = userList.stream().skip(skip).limit(UserController.PAGE_SIZE).toList();
        model.addAttribute("isAdminUser", this.isAdminUser(authentication));
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("userList", userList);
        model.addAttribute("numPages", numPages);
        this.injectFlashMessages(httpServletRequest, model);
        return "user/list";
    }

    @GetMapping("/create")
    public String createGET(Authentication authentication, RedirectAttributes redirectAttributes, Model model){
        if ( !this.adminUserCheck(authentication, redirectAttributes) ){
            return "redirect:/user";
        }
        model.addAttribute("form", new UserCreateForm());
        return "user/create";
    }

    @PostMapping("/create")
    public String createPOST(
        @Valid @ModelAttribute("form") UserCreateForm userCreateForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Authentication authentication
    ){
        if ( !this.adminUserCheck(authentication, redirectAttributes) ){
            return "redirect:/user";
        }
        if ( bindingResult.hasErrors() ){
            return "user/create";
        }
        this.userService.createFromForm(userCreateForm);
        redirectAttributes.addFlashAttribute("lastAction", "userCreated");
        return "redirect:/user";
    }

    @GetMapping("/{userId}/edit")
    public String editGET(
        @PathVariable("userId") String userId,
        RedirectAttributes redirectAttributes,
        Authentication authentication,
        Model model
    ){
        if ( !this.adminUserCheck(authentication, redirectAttributes) ){
            return "redirect:/user";
        }
        User user = this.userService.getById(Integer.parseInt(userId));
        if ( user == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such user found.");
        }
        model.addAttribute("form", new UserEditForm(user));
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/{userId}/edit")
    public String editPOST(
        @PathVariable("userId") String userId,
        @Valid @ModelAttribute("form") UserEditForm userEditForm,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes,
        Authentication authentication
    ){
        if ( !this.adminUserCheck(authentication, redirectAttributes) ){
            return "redirect:/user";
        }
        User user = this.userService.getById(Integer.parseInt(userId));
        if ( user == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such user found.");
        }
        if ( bindingResult.hasErrors() ){
            model.addAttribute("user", user);
            return "user/edit";
        }
        this.userService.updateFromForm(userEditForm);
        redirectAttributes.addFlashAttribute("lastAction", "userUpdated");
        return "redirect:/user";
    }

    @GetMapping("/{userId}/change-password")
    public String changePasswordGET(
        @PathVariable("userId") String userId,
        RedirectAttributes redirectAttributes,
        Authentication authentication,
        Model model
    ){
        if ( !this.adminUserCheck(authentication, redirectAttributes) ){
            return "redirect:/user";
        }
        User user = this.userService.getById(Integer.parseInt(userId));
        if ( user == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such user found.");
        }
        model.addAttribute("form", new UserPasswordChangeForm());
        model.addAttribute("user", user);
        return "user/change_password";
    }

    @PostMapping("/{userId}/change-password")
    public String changePasswordPOST(
        @PathVariable("userId") String userId,
        @Valid @ModelAttribute("form") UserPasswordChangeForm userPasswordChangeForm,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes,
        Authentication authentication
    ){
        if ( !this.adminUserCheck(authentication, redirectAttributes) ){
            return "redirect:/user";
        }
        User user = this.userService.getById(Integer.parseInt(userId));
        if ( user == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such user found.");
        }
        if ( bindingResult.hasErrors() ){
            model.addAttribute("user", user);
            return "user/change_password";
        }
        this.userService.changePasswordFromForm(userPasswordChangeForm);
        redirectAttributes.addFlashAttribute("lastAction", "passwordChanged");
        return "redirect:/user";
    }

    @GetMapping("/{userId}/delete")
    public String delete(
        @PathVariable("userId") String userId,
        RedirectAttributes redirectAttributes,
        Authentication authentication
    ){
        if ( !this.adminUserCheck(authentication, redirectAttributes) ){
            return "redirect:/user";
        }
        if ( this.userService.getById(Integer.parseInt(userId)) != null ){
            this.userService.delete();
            redirectAttributes.addFlashAttribute("lastAction", "userDeleted");
        }
        return "redirect:/user";
    }
}
