package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.models.Product;
import com.example.shopapp.repositories.OrderDetailRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.services.interfaces.IOrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        OrderDetail newOrderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();

        return orderDetailRepository.save(newOrderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long orderDetailId) throws DataNotFoundException {

        return orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find OrderDetail with id = " + orderDetailId));
    }

    @Override
    @Transactional
    public OrderDetail updateOrderDetail(Long orderDetailId, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {

        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail"));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order"));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product"));

        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(Long orderDetailId) {
        orderDetailRepository.deleteById(orderDetailId);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
