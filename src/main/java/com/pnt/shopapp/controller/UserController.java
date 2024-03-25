package com.pnt.shopapp.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pnt.shopapp.dtos.UserDTO;
import com.pnt.shopapp.dtos.UserLoginDTO;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result){
       try{
            if(result.hasErrors()){
                List<String> errorMessages=result.getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password and RetypePassword not match");
            }
           return ResponseEntity.ok("Register Successfully");
       }catch(Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        try{
            //kiểm tra thông tin đăng nhập
            // trả về token trong response
            return ResponseEntity.ok("Some token");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
