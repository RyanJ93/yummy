package dev.enricosola.yummy.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import dev.enricosola.yummy.form.menusection.MenuSectionCreateForm;
import dev.enricosola.yummy.form.menusection.MenuSectionEditForm;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import dev.enricosola.yummy.support.FlashMessagesInjector;
import dev.enricosola.yummy.service.MenuSectionService;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import dev.enricosola.yummy.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import dev.enricosola.yummy.entity.MenuSection;
import dev.enricosola.yummy.entity.Menu;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/menu/{menuId}/menu-section")
public class MenuSectionController {
    private static final int PAGE_SIZE = 20;

    private final MenuSectionService menuSectionService;
    private final MenuService menuService;
    private Menu menu;

    private void fetchDependencyEntities(String menuId){
        if ( ( this.menu = this.menuService.getById(Integer.parseInt(menuId)) ) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such menu found.");
        }
    }

    private void injectFormPageDependencies(Model model){
        model.addAttribute("menu", this.menu);
    }

    private void injectFlashMessages(HttpServletRequest httpServletRequest, Model model){
        HashMap<String, String> lastActionMessagesMapping = new HashMap<>();
        lastActionMessagesMapping.put("menuSectionCreated", "Menu section has successfully been created!");
        lastActionMessagesMapping.put("menuSectionUpdated", "Menu section has successfully been updated!");
        lastActionMessagesMapping.put("menuSectionDeleted", "Menu section has successfully been removed!");
        FlashMessagesInjector flashMessagesInjector = new FlashMessagesInjector(lastActionMessagesMapping);
        flashMessagesInjector.injectFlashMessages(httpServletRequest, model);
    }

    public MenuSectionController(MenuSectionService menuSectionService, MenuService menuService){
        this.menuSectionService = menuSectionService;
        this.menuService = menuService;
    }

    @GetMapping()
    public String list(
        @PathVariable("menuId") String menuId,
        @RequestParam(required = false, name = "page") String page,
        HttpServletRequest httpServletRequest,
        Model model
    ){
        int pageNumber = page == null || page.isEmpty() ? 1 : Integer.parseInt(page);
        int skip = ( pageNumber - 1 ) * MenuSectionController.PAGE_SIZE;
        this.fetchDependencyEntities(menuId);
        List<MenuSection> menuSectionList = this.menuSectionService.getAll(this.menu);
        int numPages = menuSectionList.size() / MenuSectionController.PAGE_SIZE;
        menuSectionList = menuSectionList.stream().skip(skip).limit(MenuSectionController.PAGE_SIZE).toList();
        model.addAttribute("menuSectionList", menuSectionList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("numPages", numPages);
        model.addAttribute("menu", this.menu);
        this.injectFlashMessages(httpServletRequest, model);
        return "menu_sections/list";
    }

    @GetMapping("/create")
    public String createGET(@PathVariable("menuId") String menuId, Model model){
        this.fetchDependencyEntities(menuId);
        model.addAttribute("form", new MenuSectionCreateForm());
        this.injectFormPageDependencies(model);
        return "menu_sections/create";
    }

    @PostMapping("/create")
    public String createPOST(
        @PathVariable("menuId") String menuId,
        @Valid @ModelAttribute("form") MenuSectionCreateForm menuSectionCreateForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        this.fetchDependencyEntities(menuId);
        if ( bindingResult.hasErrors() ){
            this.injectFormPageDependencies(model);
            return "menu_sections/create";
        }
        this.menuSectionService.createFromForm(this.menu, menuSectionCreateForm);
        redirectAttributes.addFlashAttribute("lastAction", "menuSectionCreated");
        return "redirect:/menu/{menuId}/menu-section";
    }

    @GetMapping("/{menuSectionId}/edit")
    public String editGET(@PathVariable("menuId") String menuId, @PathVariable("menuSectionId") String menuSectionId, Model model){
        if ( this.menuSectionService.getById(Integer.parseInt(menuSectionId)) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such menu section found.");
        }
        this.fetchDependencyEntities(menuId);
        model.addAttribute("form", new MenuSectionEditForm(this.menuSectionService.getMenuSection()));
        this.injectFormPageDependencies(model);
        return "menu_sections/edit";
    }

    @PostMapping("/{menuSectionId}/edit")
    public String editPOST(
        @PathVariable("menuId") String menuId,
        @PathVariable("menuSectionId") String menuSectionId,
        @Valid @ModelAttribute("form") MenuSectionEditForm menuSectionEditForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        this.fetchDependencyEntities(menuId);
        if ( this.menuSectionService.getById(Integer.parseInt(menuSectionId)) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such menu section found.");
        }
        if ( bindingResult.hasErrors() ){
            this.injectFormPageDependencies(model);
            return "menu_sections/edit";
        }
        this.menuSectionService.updateFromForm(menuSectionEditForm);
        redirectAttributes.addFlashAttribute("lastAction", "menuSectionUpdated");
        return "redirect:/menu/{menuId}/menu-section";
    }

    @GetMapping("/{menuSectionId}/delete")
    public String delete(
        @PathVariable("menuId") String menuId,
        @PathVariable("menuSectionId") String menuSectionId,
        RedirectAttributes redirectAttributes
    ){
        this.fetchDependencyEntities(menuId);
        if ( this.menuSectionService.getById(Integer.parseInt(menuSectionId)) != null ){
            this.menuSectionService.delete();
            redirectAttributes.addFlashAttribute("lastAction", "menuSectionDeleted");
        }
        return "redirect:/menu/{menuId}/menu-section";
    }
}
