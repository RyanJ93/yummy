package dev.enricosola.yummy.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import dev.enricosola.yummy.form.customer.CustomerCreateForm;
import dev.enricosola.yummy.service.RestaurantTableService;
import dev.enricosola.yummy.support.FlashMessagesInjector;
import org.springframework.validation.BindingResult;
import dev.enricosola.yummy.service.CustomerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import dev.enricosola.yummy.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import dev.enricosola.yummy.entity.Customer;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private static final int PAGE_SIZE = 20;

    private final RestaurantTableService restaurantTableService;
    private final CustomerService customerService;
    private final MenuService menuService;

    private void injectFormPageDependencies(Model model){
        model.addAttribute("restaurantTableList", this.restaurantTableService.getAvailableList());
        model.addAttribute("menuList", this.menuService.getAll());
    }

    private void injectFlashMessages(HttpServletRequest httpServletRequest, Model model){
        HashMap<String, String> lastActionMessagesMapping = new HashMap<>();
        lastActionMessagesMapping.put("customerCreated", "Customer has successfully been created!");
        lastActionMessagesMapping.put("customerDeleted", "Customer has successfully been removed!");
        FlashMessagesInjector flashMessagesInjector = new FlashMessagesInjector(lastActionMessagesMapping);
        flashMessagesInjector.injectFlashMessages(httpServletRequest, model);
    }

    public CustomerController(CustomerService customerService, MenuService menuService, RestaurantTableService restaurantTableService){
        this.restaurantTableService = restaurantTableService;
        this.customerService = customerService;
        this.menuService = menuService;
    }

    @GetMapping()
    public String list(
        @RequestParam(required = false, name = "page") String page,
        @RequestParam(name = "withoutFinalized", required = false) String withoutFinalized,
        HttpServletRequest httpServletRequest,
        Model model
    ){
        boolean includeFinalized = withoutFinalized == null || !withoutFinalized.equals("on");
        int pageNumber = page == null || page.isEmpty() ? 1 : Integer.parseInt(page);
        List<Customer> customerList = this.customerService.getAll(includeFinalized);
        int skip = ( pageNumber - 1 ) * CustomerController.PAGE_SIZE;
        int numPages = customerList.size() / CustomerController.PAGE_SIZE;
        customerList = customerList.stream().skip(skip).limit(CustomerController.PAGE_SIZE).toList();
        model.addAttribute("includeFinalized", includeFinalized);
        model.addAttribute("customerList", customerList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("numPages", numPages);
        this.injectFlashMessages(httpServletRequest, model);
        return "customer/list";
    }

    @GetMapping("/create")
    public String createGET(Model model){
        model.addAttribute("form", new CustomerCreateForm());
        this.injectFormPageDependencies(model);
        return "customer/create";
    }

    @PostMapping("/create")
    public String createPOST(
        @Valid @ModelAttribute("form") CustomerCreateForm customerCreateForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        if ( bindingResult.hasErrors() ){
            this.injectFormPageDependencies(model);
            return "customer/create";
        }
        this.customerService.createFromForm(customerCreateForm);
        redirectAttributes.addFlashAttribute("lastAction", "customerCreated");
        return "redirect:/customer";
    }

    @GetMapping("/{customerId}/toggle-finalized")
    public String toggleFinalized(@PathVariable("customerId") String customerId){
        this.customerService.getById(Integer.parseInt(customerId));
        this.customerService.toggleFinalized();
        return "redirect:/customer";
    }

    @GetMapping("/{customerId}/delete")
    public String delete(@PathVariable("customerId") String customerId, RedirectAttributes redirectAttributes){
        if ( this.customerService.getById(Integer.parseInt(customerId)) != null ){
            this.customerService.delete();
            redirectAttributes.addFlashAttribute("lastAction", "customerDeleted");
        }
        return "redirect:/customer";
    }
}
