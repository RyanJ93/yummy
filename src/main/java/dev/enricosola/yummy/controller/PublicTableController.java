package dev.enricosola.yummy.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import dev.enricosola.yummy.form.order.PublicOrderCreateForm;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import dev.enricosola.yummy.service.RestaurantTableService;
import dev.enricosola.yummy.support.FlashMessagesInjector;
import dev.enricosola.yummy.service.CustomerService;
import org.springframework.validation.BindingResult;
import dev.enricosola.yummy.entity.RestaurantTable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import dev.enricosola.yummy.service.OrderService;
import dev.enricosola.yummy.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import dev.enricosola.yummy.entity.Customer;
import dev.enricosola.yummy.entity.Order;
import org.springframework.ui.Model;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/public/table/{tableId}")
public class PublicTableController {
    private final RestaurantTableService restaurantTableService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final MenuService menuService;

    private RestaurantTable restaurantTable;
    private Customer customer;

    private void fetchDependencyEntities(String tableId){
        this.restaurantTable = this.restaurantTableService.getById(Integer.parseInt(tableId));
        if ( this.restaurantTable == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such table found.");
        }
        this.customer = this.customerService.getCustomerByRestaurantTable(this.restaurantTable);
    }

    private void injectFlashMessages(HttpServletRequest httpServletRequest, Model model){
        HashMap<String, String> lastActionMessagesMapping = new HashMap<>();
        lastActionMessagesMapping.put("orderMarkedAsNotReceived", "Order has successfully been marked as not received!");
        lastActionMessagesMapping.put("orderMarkedAsReceived", "Order has successfully been marked as received!");
        lastActionMessagesMapping.put("orderCreated", "Order has successfully been sent!");
        FlashMessagesInjector flashMessagesInjector = new FlashMessagesInjector(lastActionMessagesMapping);
        flashMessagesInjector.injectFlashMessages(httpServletRequest, model);
    }

    public PublicTableController(
        RestaurantTableService restaurantTableService,
        CustomerService customerService,
        OrderService orderService,
        MenuService menuService
    ){
        this.restaurantTableService = restaurantTableService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.menuService = menuService;
    }

    @GetMapping("/not-assigned")
    public String tableNotAssigned(@PathVariable("tableId") String tableId){
        this.fetchDependencyEntities(tableId);
        return "public_table/table_not_assigned";
    }

    @GetMapping("/menu")
    public String menu(@PathVariable("tableId") String tableId, HttpServletRequest httpServletRequest, Model model){
        this.fetchDependencyEntities(tableId);
        if ( this.customer == null ){
            return "redirect:/public/table/{tableId}/not-assigned";
        }
        this.menuService.setMenu(this.customer.getMenu());
        model.addAttribute("expandedMenu", this.menuService.getExpandedMenu());
        model.addAttribute("restaurantTable", this.restaurantTable);
        model.addAttribute("menu", this.customer.getMenu());
        model.addAttribute("customer", this.customer);
        this.injectFlashMessages(httpServletRequest, model);
        return "public_table/menu";
    }

    @PostMapping("/order/create")
    public String orderCreate(
        @PathVariable("tableId") String tableId,
        @Valid @ModelAttribute PublicOrderCreateForm publicOrderCreateForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ){
        this.fetchDependencyEntities(tableId);
        if ( this.customer == null ){
            return "redirect:/public/table/{tableId}/not-assigned";
        }
        if ( bindingResult.hasErrors() ){
            Stream<String> stream = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage);
            redirectAttributes.addFlashAttribute("generalErrorMessage", stream.collect(Collectors.joining("\n")));
            return "redirect:/public/table/{tableId}/menu";
        }
        this.orderService.createFromForm(this.customer, publicOrderCreateForm);
        redirectAttributes.addFlashAttribute("lastAction", "orderCreated");
        return "redirect:/public/table/{tableId}/menu";
    }

    @GetMapping("/order")
    public String orderList(@PathVariable("tableId") String tableId, HttpServletRequest httpServletRequest, Model model){
        this.fetchDependencyEntities(tableId);
        if ( this.customer == null ){
            return "redirect:/public/table/{tableId}/not-assigned";
        }
        List<Order> orderList = this.orderService.getAllByCustomer(customer, true, true);
        float subtotal = 0;
        int itemCount = 0;
        for ( Order order : orderList ){
            subtotal += order.getMenuItem().getPrice() * order.getQuantity();
            itemCount += order.getQuantity();
        }
        model.addAttribute("restaurantTable", this.restaurantTable);
        model.addAttribute("customer", this.customer);
        model.addAttribute("itemCount", itemCount);
        model.addAttribute("orderList", orderList);
        model.addAttribute("subtotal", subtotal);
        this.injectFlashMessages(httpServletRequest, model);
        return "public_table/order_list";
    }

    @GetMapping("/order/{orderId}/toggle-received")
    public String toggleReceived(
        @PathVariable("tableId") String tableId,
        @PathVariable("orderId") String orderId,
        RedirectAttributes redirectAttributes
    ){
        this.fetchDependencyEntities(tableId);
        if ( this.customer == null ){
            return "redirect:/public/table/{tableId}/not-assigned";
        }
        Order order = this.orderService.getById(Integer.parseInt(orderId));
        if ( order == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such order found.");
        }
        if ( !order.getReady() ){
            String message = "You cannot mark an order as received if it is not ready yet.";
            redirectAttributes.addFlashAttribute("generalErrorMessage", message);
            return "redirect:/public/table/{tableId}/order";
        }
        boolean received = order.getReceived();
        this.orderService.updateReady(!received);
        String attribute = received ? "orderMarkedAsNotReceived" : "orderMarkedAsReceived";
        redirectAttributes.addFlashAttribute("lastAction", attribute);
        return "redirect:/public/table/{tableId}/order";
    }
}
