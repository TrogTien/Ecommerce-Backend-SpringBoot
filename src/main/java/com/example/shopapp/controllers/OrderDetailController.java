package com.example.shopapp.controllers;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.responses.OrderDetailResponse;
import com.example.shopapp.services.interfaces.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/order_details")
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO newOrderDetailDTO,
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

            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(newOrderDetailDTO);

            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneOrderDetail(
            @PathVariable Long id
    ) {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // lay ra danh sach cac order_details cua 1 order nao do
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(
            @PathVariable Long orderId
    ) {
        List<OrderDetail> orderDetailList = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponseList = orderDetailList
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return ResponseEntity.ok(orderDetailResponseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @PathVariable Long id,
            @Valid  @RequestBody OrderDetailDTO orderDetailDTO
    ) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok(orderDetail);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable Long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("Delete successfully");
    }
}
