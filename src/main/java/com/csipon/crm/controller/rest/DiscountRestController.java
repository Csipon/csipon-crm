package com.csipon.crm.controller.rest;

import com.csipon.crm.controller.message.MessageHeader;
import com.csipon.crm.domain.request.DiscountRowRequest;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.bulk.DiscountBulkDto;
import com.csipon.crm.validation.BindingResultHandler;
import com.csipon.crm.validation.impl.BulkDiscountValidator;
import com.csipon.crm.controller.message.ResponseGenerator;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.dto.DiscountDto;
import com.csipon.crm.service.entity.DiscountService;
import com.csipon.crm.validation.impl.DiscountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.csipon.crm.controller.message.MessageProperty.*;

/**
 * Created by Pasha on 01.05.2017.
 */
@RestController
@RequestMapping(value = "/discounts")
public class DiscountRestController {
    private final DiscountService discountService;
    private final DiscountValidator discountValidator;
    private final BulkDiscountValidator bulkDiscountValidator;
    private final ResponseGenerator<Discount> generator;
    private final BindingResultHandler bindingResultHandler;

    @Autowired
    public DiscountRestController(DiscountService discountService, DiscountValidator discountValidator,
                                  BulkDiscountValidator bulkDiscountValidator, ResponseGenerator<Discount> generator, BindingResultHandler bindingResultHandler) {
        this.discountService = discountService;
        this.discountValidator = discountValidator;
        this.bulkDiscountValidator = bulkDiscountValidator;
        this.generator = generator;
        this.bindingResultHandler = bindingResultHandler;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<?> create(@Valid DiscountDto discountDto, BindingResult bindingResult) {
        discountValidator.validate(discountDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }
        Discount disc = discountService.create(discountDto);
        if (disc.getId() > 0) {
            return generator.getHttpResponse(disc.getId(), MessageHeader.SUCCESS_MESSAGE, SUCCESS_DISCOUNT_CREATED, HttpStatus.CREATED);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<?> update(@Valid DiscountDto discountDto, BindingResult bindingResult) {
        discountValidator.validate(discountDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }
        if (discountService.update(discountDto)) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_DISCOUNT_UPDATED, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/autocomplete")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN', 'ROLE_PMG')")
    public ResponseEntity<List<AutocompleteDto>> getAutocompleteDto(String pattern) {
        return new ResponseEntity<>(discountService.getAutocompleteDto(pattern), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN', 'ROLE_PMG')")
    public ResponseEntity<Map<String, Object>> getDiscountRows(DiscountRowRequest rowRequest) {
        return new ResponseEntity<>(discountService.getDiscountRows(rowRequest), HttpStatus.OK);
    }

    @PutMapping("/bulk")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR')")
    public ResponseEntity discountBulkUpdate(@Valid DiscountBulkDto bulkDto, BindingResult bindingResult) {
        bulkDiscountValidator.validate(bulkDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }

        if (discountService.bulkUpdate(bulkDto)) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_DISCOUNT_BULK_UPDATED, HttpStatus.OK);
        }

        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
