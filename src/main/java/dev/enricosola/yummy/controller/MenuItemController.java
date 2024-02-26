package dev.enricosola.yummy.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import dev.enricosola.yummy.form.menuitem.MenuItemCreateForm;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import dev.enricosola.yummy.form.menuitem.MenuItemEditForm;
import dev.enricosola.yummy.support.FlashMessagesInjector;
import dev.enricosola.yummy.service.MenuSectionService;
import dev.enricosola.yummy.service.MenuItemService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import dev.enricosola.yummy.service.MenuService;
import dev.enricosola.yummy.entity.MenuSection;
import jakarta.servlet.http.HttpServletRequest;
import dev.enricosola.yummy.entity.MenuItem;
import dev.enricosola.yummy.entity.Menu;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import java.util.HashMap;
import io.sentry.Sentry;
import java.util.List;

@Controller
@RequestMapping("/menu/{menuId}/menu-item")
public class MenuItemController {
    private static final int PAGE_SIZE = 20;

    private final MenuSectionService menuSectionService;
    private final MenuItemService menuItemService;
    private final MenuService menuService;
    private Menu menu;

    private void fetchDependencyEntities(String menuId){
        if ( ( this.menu = this.menuService.getById(Integer.parseInt(menuId)) ) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such menu found.");
        }
    }

    private void injectFormPageDependencies(Model model){
        List<MenuSection> menuSectionList = this.menuSectionService.getAll(this.menu);
        model.addAttribute("menuSectionList", menuSectionList);
        model.addAttribute("menu", this.menu);
    }

    private void injectFlashMessages(HttpServletRequest httpServletRequest, Model model){
        HashMap<String, String> lastActionMessagesMapping = new HashMap<>();
        lastActionMessagesMapping.put("menuItemCreated", "Menu item has successfully been created!");
        lastActionMessagesMapping.put("menuItemUpdated", "Menu item has successfully been updated!");
        lastActionMessagesMapping.put("menuItemDeleted", "Menu item has successfully been removed!");
        FlashMessagesInjector flashMessagesInjector = new FlashMessagesInjector(lastActionMessagesMapping);
        flashMessagesInjector.injectFlashMessages(httpServletRequest, model);
    }

    public MenuItemController(MenuSectionService menuSectionService, MenuItemService menuItemService, MenuService menuService){
        this.menuSectionService = menuSectionService;
        this.menuItemService = menuItemService;
        this.menuService = menuService;
    }

    @GetMapping()
    public String list(
        @PathVariable("menuId") String menuId,
        @RequestParam(required = false, name = "page") String page,
        HttpServletRequest httpServletRequest,
        Model model
    ){
        this.fetchDependencyEntities(menuId);
        int pageNumber = page == null || page.isEmpty() ? 1 : Integer.parseInt(page);
        int skip = ( pageNumber - 1 ) * MenuItemController.PAGE_SIZE;
        List<MenuItem> menuItemList = this.menuItemService.getAll(this.menu);
        int numPages = menuItemList.size() / MenuItemController.PAGE_SIZE;
        menuItemList = menuItemList.stream().skip(skip).limit(MenuItemController.PAGE_SIZE).toList();
        model.addAttribute("menuItemList", menuItemList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("numPages", numPages);
        model.addAttribute("menu", this.menu);
        this.injectFlashMessages(httpServletRequest, model);
        return "menu_item/list";
    }

    @GetMapping("/create")
    public String createGET(@PathVariable("menuId") String menuId, Model model){
        this.fetchDependencyEntities(menuId);
        model.addAttribute("form", new MenuItemCreateForm());
        this.injectFormPageDependencies(model);
        return "menu_item/create";
    }

    @PostMapping("/create")
    public String createPOST(
        @PathVariable("menuId") String menuId,
        @Valid @ModelAttribute("form") MenuItemCreateForm menuItemCreateForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        this.fetchDependencyEntities(menuId);
        if ( bindingResult.hasErrors() ){
            this.injectFormPageDependencies(model);
            return "menu_item/create";
        }
        try{
            this.menuItemService.createFromForm(this.menu, menuItemCreateForm);
            redirectAttributes.addFlashAttribute("lastAction", "menuItemCreated");
        }catch(Exception ex){
            redirectAttributes.addFlashAttribute("generalErrorMessage", ex.getMessage());
            Sentry.captureException(ex);
        }
        return "redirect:/menu/{menuId}/menu-item";
    }

    @GetMapping("/{menuItemId}/edit")
    public String editGET(@PathVariable("menuId") String menuId, @PathVariable("menuItemId") String menuItemId, Model model){
        this.fetchDependencyEntities(menuId);
        if ( ( this.menuItemService.getById(Integer.parseInt(menuItemId)) ) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such menu item found.");
        }
        MenuItemEditForm menuItemEditForm = new MenuItemEditForm(this.menuItemService.getMenuItem());
        model.addAttribute("menuItem", this.menuItemService.getMenuItem());
        model.addAttribute("form", menuItemEditForm);
        this.injectFormPageDependencies(model);
        return "menu_item/edit";
    }

    @PostMapping("/{menuItemId}/edit")
    public String editPOST(
        @PathVariable("menuId") String menuId,
        @PathVariable("menuItemId") String menuItemId,
        @Valid @ModelAttribute("form") MenuItemEditForm menuItemEditForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        this.fetchDependencyEntities(menuId);
        if ( ( this.menuItemService.getById(Integer.parseInt(menuItemId)) ) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such menu item found.");
        }
        try{
            if ( bindingResult.hasErrors() ){
                model.addAttribute("menuItem", this.menuItemService.getMenuItem());
                this.injectFormPageDependencies(model);
                return "menu_item/edit";
            }
            this.menuItemService.updateFromForm(menuItemEditForm);
            redirectAttributes.addFlashAttribute("lastAction", "menuItemUpdated");
        }catch(Exception ex){
            redirectAttributes.addFlashAttribute("generalErrorMessage", ex.getMessage());
            Sentry.captureException(ex);
        }
        return "redirect:/menu/{menuId}/menu-item";
    }

    @GetMapping("/{menuItemId}/delete")
    public String delete(
        @PathVariable("menuId") String menuId,
        @PathVariable("menuItemId") String menuItemId,
        RedirectAttributes redirectAttributes
    ){
        this.fetchDependencyEntities(menuId);
        if ( this.menuItemService.getById(Integer.parseInt(menuItemId)) != null ){
            this.menuItemService.delete();
            redirectAttributes.addFlashAttribute("lastAction", "menuItemDeleted");
        }
        return "redirect:/menu/{menuId}/menu-item";
    }
}
