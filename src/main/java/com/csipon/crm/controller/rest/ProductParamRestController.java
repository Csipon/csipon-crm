package com.csipon.crm.controller.rest;

import com.csipon.crm.controller.message.MessageHeader;
import com.csipon.crm.controller.message.MessageProperty;
import com.csipon.crm.controller.message.ResponseGenerator;
import com.csipon.crm.domain.model.ProductParam;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ProductParamDto;
import com.csipon.crm.service.entity.ProductParamService;
import com.csipon.crm.validation.BindingResultHandler;
import com.csipon.crm.validation.impl.ProductParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author YARUS
 */
@RestController
@RequestMapping(value = "/productParams")
public class ProductParamRestController {
    private final ProductParamService productParamService;
    private final ProductParamValidator productParamValidator;
    private final ResponseGenerator<ProductParam> generator;
    private final BindingResultHandler bindingResultHandler;

    @Autowired
    public ProductParamRestController(ProductParamService productParamService, ProductParamValidator productParamValidator, ResponseGenerator<ProductParam> generator, BindingResultHandler bindingResultHandler) {
        this.productParamService = productParamService;
        this.productParamValidator = productParamValidator;
        this.generator = generator;
        this.bindingResultHandler = bindingResultHandler;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<?> create(@Valid ProductParamDto productParamDto, BindingResult bindingResult) {
        productParamValidator.validate(productParamDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }
        ProductParam pParam = productParamService.create(productParamDto);
        if (pParam.getId() > 0) {
            return generator.getHttpResponse(pParam.getId(), MessageHeader.SUCCESS_MESSAGE, MessageProperty.SUCCESS_PRODUCT_PARAM_CREATED, HttpStatus.CREATED);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, MessageProperty.ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<?> update(@Valid ProductParamDto productParamDto, BindingResult bindingResult) {
        productParamValidator.validateOnEdit(productParamDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }
        if (productParamService.update(productParamDto)) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, MessageProperty.SUCCESS_PRODUCT_PARAM_UPDATED, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, MessageProperty.ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping
    @RequestMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (productParamService.delete(id)) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, MessageProperty.SUCCESS_PRODUCT_PARAM_DELETED, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, MessageProperty.ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/autocomplete")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN', 'ROLE_PMG')")
    public ResponseEntity<List<AutocompleteDto>> getAutocompleteDto(String pattern) {
        return new ResponseEntity<>(productParamService.getAutocompleteDto(pattern), HttpStatus.OK);
    }
}
