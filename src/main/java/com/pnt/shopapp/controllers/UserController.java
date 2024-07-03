package com.pnt.shopapp.controllers;

import com.pnt.shopapp.components.LocalizationUtils;
import com.pnt.shopapp.dtos.UpdateUserDTO;
import com.pnt.shopapp.dtos.UserDTO;
import com.pnt.shopapp.dtos.UserLoginDTO;
import com.pnt.shopapp.models.User;
import com.pnt.shopapp.responses.LoginResponse;
import com.pnt.shopapp.responses.UserResponse;
import com.pnt.shopapp.services.UserService;
import com.pnt.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LocaleResolver localeResolver;
    private final LocalizationUtils localizationUtils;
    @GetMapping("/all-users")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        try{
            List<User> users = userService.getAllUsers();
            List<UserResponse> userResponses = users.stream()
                    .map(UserResponse::fromUser)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userResponses);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
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
               return ResponseEntity.badRequest().body(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
           }
           User user = userService.createUser(userDTO);
           //return ResponseEntity.o k("Register successfully");
           return ResponseEntity.ok(user);
       }catch(Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request){
        try{
            //kiểm tra thông tin đăng nhập
            // trả về token trong response
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword(), userLoginDTO.getRoleId());
            Locale locale=localeResolver.resolveLocale(request);
            // Trả về token trong response
            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY, locale))
                            .token(token)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED,e.getMessage())).build());
        }
    }
    @PostMapping("/details")
    public ResponseEntity<UserResponse> getDetailUser(@RequestHeader("Authorization") String token){
        try{
            String extractedToken = token.substring(7); // Loại bỏ "Bearer " từ chuỗi token
            User user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/update-infor")
    public ResponseEntity<UserResponse> updateUserDetail(@RequestHeader("Authorization") String authorizationToken,
                                                         @Valid @RequestBody UpdateUserDTO updateUserDTO, BindingResult result){
      try{
          String token=authorizationToken.substring(7);
          User user=userService.getUserDetailsFromToken(token);
          User updateUser=userService.updateUser(user.getId(),updateUserDTO);
          return ResponseEntity.ok(UserResponse.fromUser(updateUser));
      } catch (Exception e) {
          return ResponseEntity.badRequest().build();
      }
    }

}
