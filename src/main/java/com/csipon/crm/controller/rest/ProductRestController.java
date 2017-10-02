package com.csipon.crm.controller.rest;

import com.csipon.crm.controller.message.MessageHeader;
import com.csipon.crm.controller.message.MessageProperty;
import com.csipon.crm.controller.message.ResponseGenerator;
import com.csipon.crm.domain.request.ProductRowRequest;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ProductDto;
import com.csipon.crm.dto.bulk.ProductBulkDto;
import com.csipon.crm.security.UserDetailsImpl;
import com.csipon.crm.service.entity.ProductService;
import com.csipon.crm.validation.BindingResultHandler;
import com.csipon.crm.validation.impl.BulkProductValidator;
import com.csipon.crm.validation.impl.ProductValidator;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductStatus;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.service.entity.DiscountService;

/**
 * Created by Pasha on 29.04.2017.
 */
@RequestMapping(value = "/products")
@RestController
public class ProductRestController {
    private final DiscountService discountService;
    private final ProductService productService;
    private final BindingResultHandler bindingResultHandler;
    private final ProductValidator productValidator;
    private final BulkProductValidator bulkProductValidator;
    private final ResponseGenerator<Product> generator;

    @Autowired
    public ProductRestController(DiscountService discountService, ProductService productService,
                                 BindingResultHandler bindingResultHandler, ProductValidator productValidator,
                                 BulkProductValidator bulkProductValidator, ResponseGenerator<Product> generator) {
        this.discountService = discountService;
        this.productService = productService;
        this.bindingResultHandler = bindingResultHandler;
        this.productValidator = productValidator;
        this.bulkProductValidator = bulkProductValidator;
        this.generator = generator;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR')")
    public ResponseEntity<?> create(@Valid ProductDto productDto, BindingResult bindingResult,
                                    Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user = (UserDetailsImpl) principal;
        productValidator.validate(productDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }

        Product product = productService.create(productDto, user);
        if (product.getId() > 0) {
            return generator.getHttpResponse(product.getId(), MessageHeader.SUCCESS_MESSAGE, MessageProperty.SUCCESS_PRODUCT_CREATED, HttpStatus.CREATED);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, MessageProperty.ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR')")
    public ResponseEntity<?> update(@Valid ProductDto productDto, BindingResult bindingResult,
                                    Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user = (UserDetailsImpl) principal;
        productValidator.validate(productDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }
        if (productService.update(productDto, user)) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, MessageProperty.SUCCESS_PRODUCT_UPDATE, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, MessageProperty.ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @PutMapping    
    @RequestMapping(value = "/{id}")
    public ResponseEntity<?> updateDiscount(@PathVariable Long id) {
        Product product = productService.getProductsById(id);
        Discount changedDisc = product.getDiscount();
        changedDisc.setActive(!changedDisc.isActive());
        discountService.update(changedDisc);
        
        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, MessageProperty.SUCCESS_DISCOUNT_UPDATED, HttpStatus.OK);
        
    }

    @PutMapping(value = "/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR')")
    public ResponseEntity<?> changeStatus(@RequestParam Long productId, @RequestParam Long statusId,
                                          Authentication authentication) {
        User user = (UserDetailsImpl) authentication.getPrincipal();
        boolean result = productService.changeStatus(productId, statusId, user);
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/autocomplete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR', 'ROLE_CUSTOMER', 'ROLE_PMG')")
    public ResponseEntity<List<AutocompleteDto>> productNames(String pattern, String type, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user = (UserDetailsImpl) principal;
        List<AutocompleteDto> result = null;
        if (user.getUserRole() == UserRole.ROLE_CUSTOMER) {
            switch (type) {
                case "actual":
                    result = productService.getActualProductsAutocompleteDtoByCustomer(pattern, user);
                    break;
                case "possible":
                    result = productService.getPossibleProductsAutocompleteDtoByCustomer(pattern, user);
                    break;
            }
        } else {
            switch (type) {
                case "withoutGroup":
                    result = productService.getAutocompleteDtoWithoutGroup(pattern);
                    break;
                case "all":
                    result = productService.getAutocompleteDto(pattern);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR', 'ROLE_CUSTOMER', 'ROLE_PMG')")
    public ResponseEntity<Map<String, Object>> productRows(ProductRowRequest productRowRequest, String type, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user = (UserDetailsImpl) principal;
        Map<String, Object> result = null;
        if (user.getUserRole() == UserRole.ROLE_CUSTOMER) {
            productRowRequest.setCustomerId(user.getId());
            productRowRequest.setStatusId(ProductStatus.ACTUAL.getId());
            switch (type) {
                case "possible":
                    productRowRequest.setAddress(user.getAddress());
                    result = productService.getProductsRow(productRowRequest);
                    break;
                case "actual":
                    result = productService.getProductsRow(productRowRequest);
            }
        } else {
            result = productService.getProductsRow(productRowRequest);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/bulk")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR')")
    public ResponseEntity productBulkUpdate(@Valid ProductBulkDto bulkDto, BindingResult bindingResult, Authentication authentication) {
        bulkProductValidator.validate(bulkDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }

        User user = (UserDetailsImpl) authentication.getPrincipal();
        if (productService.bulkUpdate(bulkDto, user)) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, MessageProperty.SUCCESS_PRODUCT_BULK_UPDATED, HttpStatus.OK);
        }

        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, MessageProperty.ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
