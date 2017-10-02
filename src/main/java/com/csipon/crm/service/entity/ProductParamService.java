package com.csipon.crm.service.entity;

import com.csipon.crm.domain.model.ProductParam;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ProductParamDto;
import java.util.List;

/**
 *
 * @author YARUS
 */
public interface ProductParamService {
    
    ProductParam create(ProductParamDto productParamDto);
        
    boolean update(ProductParamDto productParamDto);
    
    boolean delete(Long id);
    
    List<ProductParam> getAllByProductId(Long id);

    List<AutocompleteDto> getAutocompleteDto(String pattern);
    
    List<ProductParam> getAllByParamName(String paramName);
    
    ProductParam getById(Long id);
}
