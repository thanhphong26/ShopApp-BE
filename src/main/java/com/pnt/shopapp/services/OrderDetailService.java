package com.pnt.shopapp.services;

import com.pnt.shopapp.dtos.OrderDetailDTO;
import com.pnt.shopapp.exceptions.DataNotFoundException;
import com.pnt.shopapp.models.Order;
import com.pnt.shopapp.models.OrderDetail;
import com.pnt.shopapp.models.Product;
import com.pnt.shopapp.repositories.OrderDetailRepository;
import com.pnt.shopapp.repositories.OrderRepository;
import com.pnt.shopapp.repositories.ProductRepository;
import com.pnt.shopapp.responses.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order=orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find order with id: "+orderDetailDTO.getOrderId()));
        Product product=productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find Product with id: "+orderDetailDTO.getProductId()));
        OrderDetail orderDetail=OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(orderDetailDTO.getQuantity())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getQuantity() * product.getPrice())
                .color(orderDetailDTO.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find OrderDetail with id: "+id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id: "+id));
        Order order=orderRepository.findById(newOrderDetailData.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find order with id: "+newOrderDetailData.getOrderId()));
        Product product=productRepository.findById(newOrderDetailData.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find Product with id: "+newOrderDetailData.getProductId()));
        existingOrderDetail.setPrice(newOrderDetailData.getPrice());
        existingOrderDetail.setQuantity(newOrderDetailData.getQuantity());
        existingOrderDetail.setProduct(product);
        existingOrderDetail.setOrder(order);
        existingOrderDetail.setColor(newOrderDetailData.getColor());
        existingOrderDetail.setTotalMoney(newOrderDetailData.getTotalMoney());
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
