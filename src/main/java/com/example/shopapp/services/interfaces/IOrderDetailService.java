package com.example.shopapp.services.interfaces;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetail getOrderDetail(Long orderDetailId) throws DataNotFoundException;

    OrderDetail updateOrderDetail(Long orderDetailId, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    void deleteOrderDetail(Long orderDetailId);

    List<OrderDetail> findByOrderId(Long orderId);
}
