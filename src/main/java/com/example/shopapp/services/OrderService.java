package com.example.shopapp.services;

import com.example.shopapp.dtos.CartItemDTO;
import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.*;
import com.example.shopapp.repositories.OrderDetailRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.responses.OrderResponse;
import com.example.shopapp.services.interfaces.IOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        // check existing user
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        // convert userDTO => User
        // * use Model Mapper
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        order.setActive(true);
        // Check Shipping Date
        LocalDate shippingDate = orderDTO.getShippingDate() == null
                ? LocalDate.now()
                : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Shipping Date must be at least today");
        }
        order.setShippingDate(shippingDate);

        orderRepository.save(order);

        // Create OrderDetail from List CartItemDTO
        List<OrderDetail> orderDetails  = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            Product product = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Product not found"));

            orderDetail.setProduct(product);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setNumberOfProducts(quantity);
            // Add to List
            orderDetails.add(orderDetail);
        }

        orderDetailRepository.saveAll(orderDetails);

        modelMapper.typeMap(Order.class, OrderResponse.class);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrderById(Long id) throws DataNotFoundException {
        return orderRepository.findById(id).map(OrderResponse::fromOrder)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order with id = " +  id));
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order with that id"));
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);

        orderRepository.save(order);
        return OrderResponse.fromOrder(order);
    }

    // Soft-Delete
    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);

        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(OrderResponse::fromOrder)
                .toList();
    }

    @Override
    public Page<Order> getOrdersByKeyword(String keyword, Pageable pageable) {
        return orderRepository.findByKeyword(keyword, pageable);
    }
}
