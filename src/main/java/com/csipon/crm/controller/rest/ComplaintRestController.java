package com.csipon.crm.controller.rest;

import com.csipon.crm.controller.message.MessageHeader;
import com.csipon.crm.controller.message.MessageProperty;
import com.csipon.crm.controller.message.ResponseGenerator;
import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.request.ComplaintRowRequest;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ComplaintDto;
import com.csipon.crm.dto.GraphDto;
import com.csipon.crm.security.UserDetailsImpl;
import com.csipon.crm.service.entity.ComplaintService;
import com.csipon.crm.validation.BindingResultHandler;
import com.csipon.crm.validation.impl.ComplaintValidator;
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

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 01.05.2017
 */

@RestController
@RequestMapping(value = "/complaints")
public class ComplaintRestController {


    private ComplaintService complaintService;
    private ResponseGenerator<Complaint> generator;
    private BindingResultHandler bindingResultHandler;
    private ComplaintValidator complaintValidator;

    @Autowired
    public ComplaintRestController(ComplaintService complaintService, ResponseGenerator<Complaint> generator,
                                   BindingResultHandler bindingResultHandler, ComplaintValidator complaintValidator) {
        this.complaintService = complaintService;
        this.generator = generator;
        this.bindingResultHandler = bindingResultHandler;
        this.complaintValidator = complaintValidator;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> createComplaint(@Valid ComplaintDto complaintDto, BindingResult bindingResult, Authentication authentication) {
        complaintValidator.validate(complaintDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }
        User user = (UserDetailsImpl) authentication.getPrincipal();
        complaintDto.setCustomerId(user.getId());

        Complaint complaint = complaintService.persist(complaintDto);
        if (complaint.getId() > 0) {
            return generator.getHttpResponse(complaint.getId(), MessageHeader.SUCCESS_MESSAGE, MessageProperty.SUCCESS_COMPLAINT_CREATED, HttpStatus.CREATED);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, MessageProperty.ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN', 'ROLE_PMG')")
    public ResponseEntity<Map<String, Object>> complaints(ComplaintRowRequest complaintRowRequest, Authentication authentication,
                                                          @RequestParam(required = false) boolean individual) {
        User user = (UserDetailsImpl) authentication.getPrincipal();
        return new ResponseEntity<>(complaintService.getComplaintRow(complaintRowRequest, user, individual), HttpStatus.OK);
    }

    @GetMapping("/autocomplete")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN', 'ROLE_PMG')")
    public ResponseEntity<List<AutocompleteDto>> complaintsTitles(String pattern, Authentication authentication,
                                                                  @RequestParam(required = false) boolean individual) {
        User user = (UserDetailsImpl) authentication.getPrincipal();
        return new ResponseEntity<>(complaintService.getAutocompleteDto(pattern, user, individual), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_PMG', 'ROLE_ADMIN')")
    public ResponseEntity<Boolean> acceptOrCloseComplaint(Authentication authentication,
                                                          @RequestParam(value = "type") String type,
                                                          @PathVariable Long id) {
        User pmg = (UserDetailsImpl) authentication.getPrincipal();
        Boolean result = complaintService.changeStatusComplaint(id, type, pmg);
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/graph")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PMG')")
    public GraphDto getGraph(GraphDto graphDto) {
        return complaintService.getStatisticalGraph(graphDto);
    }
}
