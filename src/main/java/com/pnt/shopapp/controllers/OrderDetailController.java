package com.pnt.shopapp.controllers;

import com.pnt.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id){
        return ResponseEntity.ok("Get order detail with id="+id);
    }
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessage=result.getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            return ResponseEntity.ok("Create order detail successfully");
        }catch(Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
        return ResponseEntity.ok("GetOrdersDetail with orderId= "+orderId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id, @Valid @RequestBody OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok("Update orderDetail with id="+id+", newOrderDetailData: "+orderDetailDTO+" successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id){
        return ResponseEntity.ok("Delete orderDetail with id="+ id);
    }
}
