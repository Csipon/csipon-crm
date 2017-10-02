package com.csipon.crm.service.entity;

import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.request.DiscountRowRequest;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.bulk.DiscountBulkDto;
import com.csipon.crm.dto.DiscountDto;

import java.util.List;
import java.util.Map;

/**
 * Created by Pasha on 01.05.2017.
 */
public interface DiscountService {

    Discount create(DiscountDto discountDto);

    boolean update(DiscountDto discountDto);
    boolean update(Discount discount);
    
    Discount getDiscountById(Long id);

    List<AutocompleteDto> getAutocompleteDto(String pattern);

    Map<String, Object> getDiscountRows(DiscountRowRequest rowRequest);

    boolean bulkUpdate(DiscountBulkDto bulkDto);
}
