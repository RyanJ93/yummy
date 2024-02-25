package dev.enricosola.yummy.controller;

import dev.enricosola.yummy.form.restauranttable.RestaurantTableCreateForm;
import dev.enricosola.yummy.form.restauranttable.RestaurantTableEditForm;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import dev.enricosola.yummy.service.RestaurantTableService;
import dev.enricosola.yummy.support.FlashMessagesInjector;
import org.springframework.validation.BindingResult;
import dev.enricosola.yummy.entity.RestaurantTable;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/table")
public class RestaurantTableController {
    private static final int PAGE_SIZE = 20;

    private final RestaurantTableService restaurantTableService;

    private void injectFlashMessages(HttpServletRequest httpServletRequest, Model model){
        HashMap<String, String> lastActionMessagesMapping = new HashMap<>();
        lastActionMessagesMapping.put("restaurantTableCreated", "Table has successfully been created!");
        lastActionMessagesMapping.put("restaurantTableUpdated", "Table has successfully been updated!");
        lastActionMessagesMapping.put("restaurantTableDeleted", "Table has successfully been removed!");
        FlashMessagesInjector flashMessagesInjector = new FlashMessagesInjector(lastActionMessagesMapping);
        flashMessagesInjector.injectFlashMessages(httpServletRequest, model);
    }

    public RestaurantTableController(RestaurantTableService restaurantTableService){
        this.restaurantTableService = restaurantTableService;
    }

    @GetMapping()
    public String list(
        @RequestParam(required = false, name = "page") String page,
        HttpServletRequest httpServletRequest,
        Model model
    ){
        int pageNumber = page == null || page.isEmpty() ? 1 : Integer.parseInt(page);
        int skip = ( pageNumber - 1 ) * RestaurantTableController.PAGE_SIZE;
        List<RestaurantTable> restaurantTableList = this.restaurantTableService.getAll();
        int numPages = restaurantTableList.size() / RestaurantTableController.PAGE_SIZE;
        restaurantTableList = restaurantTableList.stream().skip(skip).limit(RestaurantTableController.PAGE_SIZE).toList();
        model.addAttribute("restaurantTableList", restaurantTableList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("numPages", numPages);
        this.injectFlashMessages(httpServletRequest, model);
        return "restaurant_table/list";
    }

    @GetMapping("/create")
    public String createGET(Model model){
        model.addAttribute("form", new RestaurantTableCreateForm());
        return "restaurant_table/create";
    }

    @PostMapping("/create")
    public String createPOST(
        @Valid @ModelAttribute("form") RestaurantTableCreateForm restaurantTableCreateForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ){
        if ( bindingResult.hasErrors() ){
            return "restaurant_table/create";
        }
        this.restaurantTableService.createFromForm(restaurantTableCreateForm);
        redirectAttributes.addFlashAttribute("lastAction", "restaurantTableCreated");
        return "redirect:/table";
    }

    @GetMapping("/{tableId}/edit")
    public String editGET(@PathVariable("tableId") String tableId, Model model){
        RestaurantTable restaurantTable = this.restaurantTableService.getById(Integer.parseInt(tableId));
        if ( restaurantTable == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such table found.");
        }
        model.addAttribute("form", new RestaurantTableEditForm(restaurantTable));
        model.addAttribute("restaurantTable", restaurantTable);
        return "restaurant_table/edit";
    }

    @PostMapping("/{tableId}/edit")
    public String editPOST(
        @PathVariable("tableId") String tableId,
        @Valid @ModelAttribute("form") RestaurantTableEditForm restaurantTableEditForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ){
        if ( this.restaurantTableService.getById(Integer.parseInt(tableId)) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such table found.");
        }
        if ( bindingResult.hasErrors() ){
            return "restaurant_table/edit";
        }
        this.restaurantTableService.updateFromForm(restaurantTableEditForm);
        redirectAttributes.addFlashAttribute("lastAction", "restaurantTableUpdated");
        return "redirect:/table";
    }

    @GetMapping("/{tableId}/delete")
    public String delete(@PathVariable("tableId") String tableId, RedirectAttributes redirectAttributes){
        if ( this.restaurantTableService.getById(Integer.parseInt(tableId)) != null ){
            this.restaurantTableService.delete();
            redirectAttributes.addFlashAttribute("lastAction", "restaurantTableDeleted");
        }
        return "redirect:/table";
    }
}
