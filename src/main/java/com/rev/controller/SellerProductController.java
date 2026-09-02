package com.rev.controller;


import com.rev.Exceptions.ProductException;
import com.rev.Exceptions.SellerException;
import com.rev.modal.Product;
import com.rev.modal.Seller;
import com.rev.request.CreateProductRequest;
import com.rev.service.ProductService;
import com.rev.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers/products")
public class SellerProductController {
    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySellerId(
            @RequestHeader("Authorization") String jwt) throws Exception,
            SellerException {
        Seller seller = sellerService.getSellerProfile(jwt);

        List<Product> products = productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(
            @RequestBody CreateProductRequest request,
            @RequestHeader("Authorization")String jwt)
            throws Exception {

        System.out.println("error --------"+jwt);

        Seller seller=sellerService.getSellerProfile(jwt);
        Product product=productService.createProduct(request,seller);
//        Product product = new Product();
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody Product product) throws ProductException{

            Product updatedProduct = productService.updateProduct(productId, product);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
