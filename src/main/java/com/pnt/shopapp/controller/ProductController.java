package com.pnt.shopapp.controller;

import com.pnt.shopapp.dtos.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertProduct(@Valid @ModelAttribute ProductDTO productDTO, BindingResult result)  {
        try{
            if(result.hasErrors()){
                List<String> errorMessage=result.getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            List<MultipartFile> files=productDTO.getFiles();
            files=files==null?new ArrayList<MultipartFile>():files;
            for(MultipartFile file:files){
                if(file.getSize()==0){
                    continue;
                }
                //Kiểm tra kích thước file và dịnh dạng
                if(file.getSize()>10*1024*1024){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File too large to upload! Maximum file is 10MB");
                }
                String contentType=file.getContentType();
                if(contentType==null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }
                //Lưu file và cập nhật thumbnail trong DTO
                String filename=storeFile(file);
            }
            return ResponseEntity.ok("This is insert Category"+productDTO);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseEntity<String> getAllProducts(@RequestParam("page") int page, @RequestParam("limit") int limit){
        return ResponseEntity.ok("All products here");
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getProductByID(@PathVariable String id){
        return ResponseEntity.ok("Product with id="+id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductByID(@PathVariable String id){
        return ResponseEntity.ok("Product delete Succesfully");
    }
    private String storeFile(MultipartFile file) throws IOException{
        String filename= StringUtils.cleanPath(file.getOriginalFilename());
        //Sử dụng UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFileName= UUID.randomUUID().toString()+"_"+filename;
        //Đường dẫn đến thư muc lưu file
        Path uploadDir= Paths.get("uploads");
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //Đường dẫn đầy đủ đến file
        Path destination=Paths.get(uploadDir.toString(), uniqueFileName);
        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
}
