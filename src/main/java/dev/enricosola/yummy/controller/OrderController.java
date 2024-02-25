package dev.enricosola.yummy.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import dev.enricosola.yummy.support.FlashMessagesInjector;
import dev.enricosola.yummy.form.order.OrderCreateForm;
import org.springframework.validation.BindingResult;
import dev.enricosola.yummy.service.CustomerService;
import dev.enricosola.yummy.service.MenuItemService;
import dev.enricosola.yummy.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import dev.enricosola.yummy.entity.Customer;
import dev.enricosola.yummy.entity.MenuItem;
import dev.enricosola.yummy.entity.Order;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/customer/{customerId}/order")
public class OrderController {
    private static final int PAGE_SIZE = 20;

    private final MenuItemService menuItemService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private Customer customer;

    private void fetchDependencyEntities(String customerId){
        this.customer = this.customerService.getById(Integer.parseInt(customerId));
        if ( this.customer == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such customer found.");
        }
    }

    private void injectFormPageDependencies(Model model){
        List<MenuItem> menuItemList = this.menuItemService.getAll(this.customer.getMenu());
        model.addAttribute("menuItemList", menuItemList);
        model.addAttribute("customer", this.customer);
    }

    private void injectFlashMessages(HttpServletRequest httpServletRequest, Model model){
        HashMap<String, String> lastActionMessagesMapping = new HashMap<>();
        lastActionMessagesMapping.put("orderMarkedAsNotReceived", "Order has successfully been marked as not received!");
        lastActionMessagesMapping.put("orderMarkedAsNotReady", "Order has successfully been marked as not ready!");
        lastActionMessagesMapping.put("orderMarkedAsReceived", "Order has successfully been marked as received!");
        lastActionMessagesMapping.put("orderMarkedAsReady", "Order has successfully been marked as ready!");
        lastActionMessagesMapping.put("orderCreated", "Order has successfully been created!");
        lastActionMessagesMapping.put("orderUpdated", "Order has successfully been updated!");
        lastActionMessagesMapping.put("orderDeleted", "Order has successfully been removed!");
        FlashMessagesInjector flashMessagesInjector = new FlashMessagesInjector(lastActionMessagesMapping);
        flashMessagesInjector.injectFlashMessages(httpServletRequest, model);
    }

    public OrderController(MenuItemService menuItemService, CustomerService customerService, OrderService orderService){
        this.menuItemService = menuItemService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping()
    public String list(
        @PathVariable("customerId") String customerId,
        @RequestParam(name = "withoutReceived", required = false) String withoutReceived,
        @RequestParam(name = "withoutReady", required = false) String withoutReady,
        @RequestParam(required = false, name = "page") String page,
        HttpServletRequest httpServletRequest,
        Model model
    ){
        this.fetchDependencyEntities(customerId);
        boolean includeReceived = withoutReceived == null || !withoutReceived.equals("on");
        boolean includeReady = withoutReady == null || !withoutReady.equals("on");
        int pageNumber = page == null || page.isEmpty() ? 1 : Integer.parseInt(page);
        List<Order> orderList = this.orderService.getAllByCustomer(this.customer, includeReceived, includeReady);
        int skip = ( pageNumber - 1 ) * OrderController.PAGE_SIZE;
        int numPages = orderList.size() / OrderController.PAGE_SIZE;
        orderList = orderList.stream().skip(skip).limit(OrderController.PAGE_SIZE).toList();
        model.addAttribute("includeReceived", includeReceived);
        model.addAttribute("includeReady", includeReady);
        model.addAttribute("customer", this.customer);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("orderList", orderList);
        model.addAttribute("numPages", numPages);
        this.injectFlashMessages(httpServletRequest, model);
        return "order/list";
    }

    @GetMapping("/create")
    public String createGET(@PathVariable("customerId") String customerId, Model model){
        this.fetchDependencyEntities(customerId);
        model.addAttribute("form", new OrderCreateForm());
        this.injectFormPageDependencies(model);
        return "order/create";
    }

    @PostMapping("/create")
    public String createPOST(
        @PathVariable("customerId") String customerId,
        @Valid @ModelAttribute("form") OrderCreateForm orderCreateForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        this.fetchDependencyEntities(customerId);
        if ( bindingResult.hasErrors() ){
            this.injectFormPageDependencies(model);
            return "order/create";
        }
        this.orderService.createFromForm(this.customer, orderCreateForm);
        redirectAttributes.addFlashAttribute("lastAction", "orderCreated");
        return "redirect:/customer/{customerId}/order";
    }

    @GetMapping("/{orderId}/toggle-received")
    public String toggleReceived(
        @PathVariable("customerId") String customerId,
        @PathVariable("orderId") String orderId,
        RedirectAttributes redirectAttributes
    ){
        this.fetchDependencyEntities(customerId);
        if ( this.orderService.getById(Integer.parseInt(orderId)) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such order found.");
        }
        boolean received = this.orderService.getOrder().getReceived();
        this.orderService.updateReceived(!received);
        String attribute = received ? "orderMarkedAsNotReceived" : "orderMarkedAsReceived";
        redirectAttributes.addFlashAttribute("lastAction", attribute);
        return "redirect:/customer/{customerId}/order";
    }

    @GetMapping("/{orderId}/toggle-ready")
    public String toggleReady(
        @PathVariable("customerId") String customerId,
        @PathVariable("orderId") String orderId,
        RedirectAttributes redirectAttributes
    ){
        this.fetchDependencyEntities(customerId);
        if ( this.orderService.getById(Integer.parseInt(orderId)) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such order found.");
        }
        boolean ready = this.orderService.getOrder().getReady();
        this.orderService.updateReady(!ready);
        String attribute = ready ? "orderMarkedAsNotReady" : "orderMarkedAsReady";
        redirectAttributes.addFlashAttribute("lastAction", attribute);
        return "redirect:/customer/{customerId}/order";
    }

    @GetMapping("/{orderId}/delete")
    public String delete(
        @PathVariable("customerId") String customerId,
        @PathVariable("orderId") String orderId,
        RedirectAttributes redirectAttributes
    ){
        this.fetchDependencyEntities(customerId);
        if ( this.orderService.getById(Integer.parseInt(orderId)) != null ){
            this.orderService.delete();
            redirectAttributes.addFlashAttribute("lastAction", "orderDeleted");
        }
        return "redirect:/customer/{customerId}/order";
    }
}
