package com.pnt.shopapp.services;

import com.pnt.shopapp.dtos.ProductDTO;
import com.pnt.shopapp.dtos.ProductImageDTO;
import com.pnt.shopapp.exceptions.DataNotFoundException;
import com.pnt.shopapp.exceptions.InvalidParamException;
import com.pnt.shopapp.models.Category;
import com.pnt.shopapp.models.Product;
import com.pnt.shopapp.models.ProductImage;
import com.pnt.shopapp.repositories.CategoryRepository;
import com.pnt.shopapp.repositories.ProductImageRepository;
import com.pnt.shopapp.repositories.ProductRepository;
import com.pnt.shopapp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws Exception {
        Category category=categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()->new DataNotFoundException(
                        "Cannot find category with id: "+productDTO.getCategoryId()));
        Product product=Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(category)
                .build();
        return productRepository.save(product);
    }
    @Override
    public Product getProductById(long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find product with id: "+id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        if (productDTO.getName() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find category with id: " + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setCategory(category);
            productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct=productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException(
                        "Cannot find product with id: "+productImageDTO.getProductId()));
        ProductImage productImage=ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // Not insert too 5 images in product
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImage.MAX_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images must be <= "+ProductImage.MAX_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(productImage);
    }


}
