package com.shopflow.shopflow_api.controller;

import com.shopflow.shopflow_api.dto.ProductRequest;
import com.shopflow.shopflow_api.dto.ProductResponse;
import com.shopflow.shopflow_api.model.Product;
import com.shopflow.shopflow_api.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shopflow.shopflow_api.dto.CategoryResponse;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductResponse> getAll(Pageable pageable) {
        return productService.findAll(pageable).map(this::toResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return productService.findById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //GetMapping eredita il path da RequestMapping e gli aggiunge l'id
    //PostMapping invece non ha bisogno dell'id infatti gli basta il path ereditato
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        Product created = productService.create(toEntity(request), request.categoryIds());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(created));
    }
    
    //@RequestBosy dice a Spring: "prendi il JSON che arriva nel corpo della richiesta e convertilo in un oggetto Product
    //@PathVariable Long id — prende il numero dall'URL (in /products/1 cattura l'1) e lo mette nel parametro id. Lo usi quando l'informazione sta nel percorso.
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,@Valid @RequestBody ProductRequest request) {
        return productService.update(id, toEntity(request), request.categoryIds())
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- metodi di traduzione (mapping) ---

    private ProductResponse toResponse(Product p) {
        Set<CategoryResponse> categories = p.getCategories().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .collect(Collectors.toSet());
        return new ProductResponse(p.getId(), p.getName(), p.getDescription(),
                p.getPrice(), p.getStock(), categories);
    }

    private Product toEntity(ProductRequest req) {
        Product p = new Product();
        p.setName(req.name());
        p.setDescription(req.description());
        p.setPrice(req.price());
        p.setStock(req.stock());
        return p;
    }
}