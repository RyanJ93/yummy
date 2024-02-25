package dev.enricosola.yummy.controller;

import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import dev.enricosola.yummy.support.FlashMessagesInjector;
import dev.enricosola.yummy.form.menu.MenuCreateForm;
import org.springframework.validation.BindingResult;
import dev.enricosola.yummy.form.menu.MenuEditForm;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import dev.enricosola.yummy.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import dev.enricosola.yummy.entity.Menu;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private static final int PAGE_SIZE = 20;

    private final MenuService menuService;

    private void injectFlashMessages(HttpServletRequest httpServletRequest, Model model){
        HashMap<String, String> lastActionMessagesMapping = new HashMap<>();
        lastActionMessagesMapping.put("menuCreated", "Menu has successfully been created!");
        lastActionMessagesMapping.put("menuUpdated", "Menu has successfully been updated!");
        lastActionMessagesMapping.put("menuDeleted", "Menu has successfully been removed!");
        FlashMessagesInjector flashMessagesInjector = new FlashMessagesInjector(lastActionMessagesMapping);
        flashMessagesInjector.injectFlashMessages(httpServletRequest, model);
    }

    public MenuController(MenuService menuService){
        this.menuService = menuService;
    }

    @GetMapping()
    public String list(@RequestParam(required = false, name = "page") String page, HttpServletRequest httpServletRequest, Model model){
        int pageNumber = page == null || page.isEmpty() ? 1 : Integer.parseInt(page);
        int skip = ( pageNumber - 1 ) * MenuController.PAGE_SIZE;
        List<Menu> menuList = this.menuService.getAll();
        int numPages = menuList.size() / MenuController.PAGE_SIZE;
        menuList = menuList.stream().skip(skip).limit(MenuController.PAGE_SIZE).toList();
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("menuList", menuList);
        model.addAttribute("numPages", numPages);
        this.injectFlashMessages(httpServletRequest, model);
        return "menu/list";
    }

    @GetMapping("/create")
    public String createGET(Model model){
        model.addAttribute("form", new MenuCreateForm());
        return "menu/create";
    }

    @PostMapping("/create")
    public String createPOST(
        @Valid @ModelAttribute("form") MenuCreateForm menuCreate,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ){
        if ( bindingResult.hasErrors() ){
            return "menu/create";
        }
        this.menuService.createFromForm(menuCreate);
        redirectAttributes.addFlashAttribute("lastAction", "menuCreated");
        return "redirect:/menu";
    }

    @GetMapping("/{menuId}/edit")
    public String editGET(@PathVariable("menuId") String menuId, Model model){
        Menu menu = this.menuService.getById(Integer.parseInt(menuId));
        if ( menu == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such menu found.");
        }
        model.addAttribute("form", new MenuEditForm(menu));
        model.addAttribute("menu", menu);
        return "menu/edit";
    }

    @PostMapping("/{menuId}/edit")
    public String editPOST(
        @PathVariable("menuId") String menuId,
        @Valid @ModelAttribute("form") MenuEditForm menuEditForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ){
        Menu menu = this.menuService.getById(Integer.parseInt(menuId));
        if ( menu == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such menu found.");
        }
        if ( bindingResult.hasErrors() ){
            return "menu/edit";
        }
        this.menuService.setMenu(menu);
        this.menuService.updateFromForm(menuEditForm);
        redirectAttributes.addFlashAttribute("lastAction", "menuUpdated");
        return "redirect:/menu";
    }

    @GetMapping("/{menuId}/delete")
    public String delete(@PathVariable("menuId") String menuId, RedirectAttributes redirectAttributes){
        if ( this.menuService.getById(Integer.parseInt(menuId)) != null ){
            this.menuService.delete();
            redirectAttributes.addFlashAttribute("lastAction", "menuDeleted");
        }
        return "redirect:/menu";
    }
}
