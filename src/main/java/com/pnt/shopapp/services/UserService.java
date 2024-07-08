package com.pnt.shopapp.services;

import com.pnt.shopapp.components.JwtTokenUtil;
import com.pnt.shopapp.components.LocalizationUtils;
import com.pnt.shopapp.dtos.UpdateUserDTO;
import com.pnt.shopapp.dtos.UserDTO;
import com.pnt.shopapp.exceptions.DataNotFoundException;
import com.pnt.shopapp.exceptions.ExpiredTokenException;
import com.pnt.shopapp.exceptions.PermissionDenyException;
import com.pnt.shopapp.models.Role;
import com.pnt.shopapp.models.User;
import com.pnt.shopapp.repositories.RoleRepository;
import com.pnt.shopapp.repositories.UserRepository;
import com.pnt.shopapp.utils.MessageKeys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtToken;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        //Kiểm tra số điện thoại đã tồn tại hay chưa
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataNotFoundException("Phone number already exists");
        }
        Role role =roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You cannot register an admin account");
        }
        //convert from userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookId())
                .googleAccountId(userDTO.getGoogleId())
                .build();
        newUser.setRole(role);
        // Kiểm tra nếu có accountId, không yêu cầu password
        if (userDTO.getFacebookId() == 0 && userDTO.getGoogleId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            //sẽ nói đến trong phần spring security
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) throws Exception{
        Optional<User> user=userRepository.findByPhoneNumber(phoneNumber);
        if(user.isEmpty()){
            throw  new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
        }
        //return optionalUser.get();//muốn trả JWT token ?
        User existingUser=user.get();
        //check password
        if (existingUser.getFacebookAccountId() == 0
                && existingUser.getGoogleAccountId() == 0) {
            if(!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
            }
        }
        Optional<Role> optionalRole=roleRepository.findById(roleId);
        if (optionalRole.isEmpty()||!roleId.equals(existingUser.getRole().getId())) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS));
        }
        if(!user.get().isActive()){
            throw new PermissionDenyException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_LOCKED));
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password,
                existingUser.getAuthorities()
        );
        //authentication with java spring
        authenticationManager.authenticate(authenticationToken);
        return jwtToken.generateToken(existingUser);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if(jwtToken.isTokenExpired(token)){
            throw new ExpiredTokenException(localizationUtils.getLocalizedMessage(MessageKeys.TOKEN_EXPIRED));
        }
        String phoneNumber = jwtToken.extractPhoneNumber(token);
        Optional<User> user;
        user=userRepository.findByPhoneNumber(phoneNumber);
        if(user.isPresent()){
            return user.get();
        }
        else {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_FOUND));
        }
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(Long id, UpdateUserDTO updateUserDTO) throws DataNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_FOUND));
        }
        // Check if the phone number is being changed and if it already exists for another user
        User user = optionalUser.get();
        String newPhoneNumber = updateUserDTO.getPhoneNumber();
        if (!user.getPhoneNumber().equals(newPhoneNumber) &&
                userRepository.existsByPhoneNumber(newPhoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        if (Objects.nonNull(updateUserDTO.getFullName())) {
            user.setFullName(updateUserDTO.getFullName());
        }
        if (Objects.nonNull(updateUserDTO.getAddress())) {
            user.setAddress(updateUserDTO.getAddress());
        }
        if (Objects.nonNull(updateUserDTO.getDateOfBirth())) {
            user.setDateOfBirth(updateUserDTO.getDateOfBirth());
        }
        if (updateUserDTO.getFacebookAccountId()>0) {
            user.setFacebookAccountId(updateUserDTO.getFacebookAccountId());
        }
        if (updateUserDTO.getGoogleAccountId()>0) {
            user.setGoogleAccountId(updateUserDTO.getGoogleAccountId());
        }
        if(updateUserDTO.getPassword()!=null && !updateUserDTO.getPassword().isEmpty()){
            if(!updateUserDTO.getPassword().equals(updateUserDTO.getRetypedPassword())){
                throw new DataIntegrityViolationException(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
            }
            String encodedPassword = passwordEncoder.encode(updateUserDTO.getPassword());
            user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }
}
