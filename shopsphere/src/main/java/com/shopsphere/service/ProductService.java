package com.shopsphere.service;
import com.shopsphere.model.Product;
import com.shopsphere.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository) {

        this.productRepository = productRepository;
    }


    // =========================================================
    // CREATE PRODUCT
    // =========================================================

    public Product createProduct(Product product) {

        validateProduct(product);

        product.setId(null);

        return productRepository.save(product);
    }


    // =========================================================
    // GET ALL PRODUCTS
    // =========================================================

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }


    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"
                        ));
    }


    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    public Product updateProduct(
            Long id,
            Product product) {

        Product existingProduct =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        validateProduct(product);

        existingProduct.setName(
                product.getName()
        );

        existingProduct.setDescription(
                product.getDescription()
        );

        existingProduct.setPrice(
                product.getPrice()
        );

        existingProduct.setCategory(
                product.getCategory()
        );

        existingProduct.setImageUrl(
                product.getImageUrl()
        );

        return productRepository.save(
                existingProduct
        );
    }


    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {

            throw new RuntimeException(
                    "Product not found"
            );
        }

        productRepository.deleteById(id);
    }


    // =========================================================
    // PRODUCT VALIDATION
    // =========================================================

    private void validateProduct(
            Product product) {

        if (product.getName() == null ||
                product.getName().isBlank()) {

            throw new RuntimeException(
                    "Product name is required"
            );
        }

        if (product.getPrice() < 0) {

            throw new RuntimeException(
                    "Product price cannot be negative"
            );
        }

        if (product.getCategory() == null ||
                product.getCategory().isBlank()) {

            throw new RuntimeException(
                    "Product category is required"
            );
        }
    }
}
