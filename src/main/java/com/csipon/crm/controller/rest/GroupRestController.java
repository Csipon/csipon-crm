package com.csipon.crm.controller.rest;

import com.csipon.crm.controller.message.MessageHeader;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Group;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.GroupDto;
import com.csipon.crm.dto.bulk.GroupBulkDto;
import com.csipon.crm.service.entity.GroupService;
import com.csipon.crm.validation.BindingResultHandler;
import com.csipon.crm.validation.impl.GroupValidator;
import com.csipon.crm.controller.message.ResponseGenerator;
import com.csipon.crm.domain.request.GroupRowRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.csipon.crm.controller.message.MessageProperty.ERROR_SERVER_ERROR;
import static com.csipon.crm.controller.message.MessageProperty.SUCCESS_DISCOUNT_UPDATED;
import static com.csipon.crm.controller.message.MessageProperty.SUCCESS_GROUP_CREATED;
import static com.csipon.crm.controller.message.MessageProperty.SUCCESS_GROUP_UPDATE;

import com.csipon.crm.service.entity.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.csipon.crm.controller.message.MessageProperty.*;

/**
 * Created by Pasha on 01.05.2017.
 */
@RestController
@RequestMapping(value = "/groups")
public class GroupRestController {
    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

    private final DiscountService discountService;
    private final GroupService groupService;
    private final ResponseGenerator<Group> generator;
    private final GroupValidator groupValidator;
    private final BindingResultHandler bindingResultHandler;

    @Autowired
    public GroupRestController(DiscountService discountService, GroupService groupService, GroupValidator groupValidator,
                                  ResponseGenerator<Group> generator, BindingResultHandler bindingResultHandler) {
        this.discountService = discountService;
        this.groupService = groupService;
        this.groupValidator = groupValidator;
        this.generator = generator;
        this.bindingResultHandler = bindingResultHandler;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<?> createGroup(@Valid GroupDto groupDto, BindingResult bindingResult) {
        groupValidator.validate(groupDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }
        Group group = groupService.create(groupDto);
        if (group.getId() > 0) {
            return generator.getHttpResponse(group.getId(), MessageHeader.SUCCESS_MESSAGE, SUCCESS_GROUP_CREATED, HttpStatus.CREATED);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/autocomplete")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN', 'ROLE_PMG')")
    public ResponseEntity<List<AutocompleteDto>> getAutocompleteDto(String pattern) {
        return new ResponseEntity<>(groupService.getAutocompleteGroup(pattern), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN', 'ROLE_PMG')")
    public ResponseEntity<Map<String, Object>> getGroupRows(GroupRowRequest request) {
        return new ResponseEntity<>(groupService.getGroupPage(request), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<?> update(@Valid GroupDto groupDto, BindingResult bindingResult) {
        groupValidator.validate(groupDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }
        if (groupService.update(groupDto)) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_GROUP_UPDATE, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    @RequestMapping(value = "/{id}")
    public ResponseEntity<?> updateDiscount(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        Discount changedDisc = group.getDiscount();
        changedDisc.setActive(!changedDisc.isActive());
        discountService.update(changedDisc);

        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_DISCOUNT_UPDATED, HttpStatus.OK);

    }

    @PutMapping("/bulk")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR')")
    public ResponseEntity groupBulkUpdate(GroupBulkDto bulkDto) {
        if (groupService.bulkUpdate(bulkDto)) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_GROUP_BULK_UPDATED, HttpStatus.OK);
        }

        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
