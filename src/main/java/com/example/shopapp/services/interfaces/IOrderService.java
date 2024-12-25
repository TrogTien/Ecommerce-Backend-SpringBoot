package com.example.shopapp.services.interfaces;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;

    OrderResponse getOrderById(Long id) throws Exception;

    OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id);

    List<OrderResponse> findByUserId(Long userId);

    Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);
}
