package com.emi.onlineshop.services;

import com.emi.onlineshop.dtos.ProductRequest;
import com.emi.onlineshop.dtos.ProductResponse;
import com.emi.onlineshop.models.Product;
import com.emi.onlineshop.models.TechnicalDetails;
import com.emi.onlineshop.repositories.ProductRepository;
import com.emi.onlineshop.utils.Filter;
import com.emi.onlineshop.utils.Mapper;
import com.emi.onlineshop.utils.Utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.emi.onlineshop.utils.Specifications.createSpecification;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    public ProductService(ProductRepository productRepository, Mapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(mapper::mapProductResponse)
                .toList();
    }

    public List<ProductResponse> getAllProductsBetter() {
        return productRepository.getAllProductsBetter()
                .stream()
                .map(mapper::mapProductResponse)
                .toList();
    }

    public List<ProductResponse> getAllProductsByPage(int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        pageNo = Math.max(pageNo, 1);

        return productRepository.findAll(PageRequest.of(pageNo - 1, pageSize, sort))
                .stream()
                .map(mapper::mapProductResponse)
                .toList();
    }

    public String addProduct(ProductRequest productRequest) {
        Product product = mapper.mapProductRequest(productRequest);

        Product savedProduct = productRepository.save(product);

        return "Product with id: " + savedProduct.getId() + " saved successfully.";
    }

    public List<ProductResponse> getAllByFilter(String filter) {
        List<Filter> filters = Utils.getFilters(filter);

        return productRepository.findAll(getSpecificationFromFilters(filters))
                .stream()
                .map(mapper::mapProductResponse)
                .toList();
    }

    private Specification<Product> getSpecificationFromFilters(List<Filter> filters) {
        Specification<Product> specification = createSpecification(filters.remove(0));
        for (Filter filter : filters) {
            specification = specification.and(createSpecification(filter));
        }

        return specification;
    }

    @Transactional
    public String deleteTechnicalDetailFromProduct(String productCode, String tdName) {
        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        List<TechnicalDetails> technicalDetails = product.getTechnicalDetails()
                .stream()
                .filter(p -> p.getName().equals(tdName))
                .toList();

        if (technicalDetails.isEmpty()) {
            throw new IllegalArgumentException("The product with code " + productCode + " does not have a technical detail " + tdName);
        }

        product.removeTechnicalDetail(technicalDetails.get(0));
        return "Technical detail " + tdName + " was removed from product with code " + productCode;
    }

    @Transactional
    public String deleteProductByCode(String productCode) {
        long deletedProducts = productRepository.deleteByCode(productCode);
        return deletedProducts > 0 ?
                "Deleted Product with code " + productCode :
                "Product with code " + productCode + " does not exist";
    }
}
