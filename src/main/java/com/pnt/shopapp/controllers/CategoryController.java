package com.pnt.shopapp.controllers;

import com.pnt.shopapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("${api.prefix}/categories")
public class CategoryController {
    @GetMapping("")
    public ResponseEntity<String> getAllCategories (@RequestParam("page") int page, @RequestParam("limit") int limit){
        return ResponseEntity.ok(String.format("getAllCategories, page=%d, limit=%d", page,limit));
    }
    @PostMapping("")
    //Nếu tham số truỳen vào là 1 object thì sao ==> Data Transfer Object =Request Object
    public ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessage=result.getFieldErrors()
                    .stream().map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("This is insert Category"+categoryDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id){
        return ResponseEntity.ok("update category with id= "+id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok("delete category with id= "+id);
    }
}
