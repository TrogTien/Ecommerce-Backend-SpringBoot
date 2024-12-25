package com.example.shopapp.controllers;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.models.User;
import com.example.shopapp.responses.OrderListResponse;
import com.example.shopapp.responses.OrderResponse;
import com.example.shopapp.services.interfaces.IOrderService;
import com.example.shopapp.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/orders")
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;

    // POST: localhost:8080/api/v1/orders
    @PostMapping("")
    public ResponseEntity<?> addOrder(
            @RequestBody @Valid OrderDTO orderDTO,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(String.valueOf(errors));
            }

            OrderResponse orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: localhost:8080/api/v1/orders/user/4
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrdersByUserId(
            @PathVariable("user_id") Long userId,
            Principal principal
    ) {
        try {
            User currentUser = userService.findUserByPhoneNumber(principal.getName());
            if (!currentUser.getRole().getName().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            List<OrderResponse> orderResponseList = orderService.findByUserId(userId);
            return ResponseEntity.ok(orderResponseList);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: localhost:8080/api/v1/orders/4
    @GetMapping("/{order_id}")
    public ResponseEntity<?> getOneOrder(
            @PathVariable("order_id") Long orderId,
            Principal principal
    ) {
        try {
            User currentUser = userService.findUserByPhoneNumber(principal.getName());

            OrderResponse orderResponse = orderService.getOrderById(orderId);

                if (!orderResponse.getUserId().equals(currentUser.getId()) && !currentUser.getRole().getName().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(orderResponse);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT :localhost:8080/api/v1/orders/4
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOneOrder(
            @PathVariable("id") Long id,
            @RequestBody @Valid OrderDTO orderDTO
    ) {
        try {
            OrderResponse orderResponse = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(orderResponse);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa mềm , update isActive
    // DELETE :localhost:8080/api/v1/orders/4
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOneOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }

    @GetMapping("/get-orders-by-keyword")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getOrdersByKeyword(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String keyword
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());

        Page<OrderResponse> orderResponsePage = orderService.getOrdersByKeyword(keyword, pageRequest).map(OrderResponse::fromOrder);
        int totalPages = orderResponsePage.getTotalPages();

        return ResponseEntity.ok(
                OrderListResponse.builder()
                        .totalPages(totalPages)
                        .orders(orderResponsePage.getContent())
                        .build()
        );
    }

}
