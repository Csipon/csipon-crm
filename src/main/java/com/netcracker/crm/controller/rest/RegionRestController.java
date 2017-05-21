package com.netcracker.crm.controller.rest;

import com.netcracker.crm.controller.message.ResponseGenerator;
import com.netcracker.crm.domain.model.Region;
import com.netcracker.crm.dto.AutocompleteDto;
import com.netcracker.crm.dto.RegionDto;
import com.netcracker.crm.service.entity.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.netcracker.crm.controller.message.MessageHeader.ERROR_MESSAGE;
import static com.netcracker.crm.controller.message.MessageHeader.SUCCESS_MESSAGE;
import static com.netcracker.crm.controller.message.MessageProperty.ERROR_SERVER_ERROR;
import static com.netcracker.crm.controller.message.MessageProperty.SUCCESS_REGION_UPDATE;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
@RestController
@RequestMapping(value = "/regions")
public class RegionRestController {
    private final RegionService regionService;
    private final ResponseGenerator<Region> generator;

    @Autowired
    public RegionRestController(RegionService regionService, ResponseGenerator<Region> generator) {
        this.regionService = regionService;
        this.generator = generator;
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR')")
    public ResponseEntity<?> update(RegionDto productDto) {
        if (regionService.update(productDto)) {
            return generator.getHttpResponse(SUCCESS_MESSAGE, SUCCESS_REGION_UPDATE, HttpStatus.OK);
        }
        return generator.getHttpResponse(ERROR_MESSAGE, ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/autocomplete")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<List<AutocompleteDto>> getAutocompleteDto(String pattern) {
        return new ResponseEntity<>(regionService.getAutocompleteDto(pattern), HttpStatus.OK);
    }

}
