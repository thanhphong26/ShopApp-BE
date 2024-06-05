package com.pnt.shopapp.services;

import com.pnt.shopapp.dtos.OrderDetailDTO;
import com.pnt.shopapp.exceptions.DataNotFoundException;
import com.pnt.shopapp.models.OrderDetail;
import com.pnt.shopapp.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData)
            throws DataNotFoundException;
    void deleteById(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
}
