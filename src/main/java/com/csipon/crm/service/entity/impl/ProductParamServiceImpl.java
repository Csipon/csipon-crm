package com.csipon.crm.service.entity.impl;

import com.csipon.crm.dao.ProductParamDao;
import com.csipon.crm.domain.model.ProductParam;
import com.csipon.crm.domain.real.RealProductParam;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ProductParamDto;
import com.csipon.crm.dto.mapper.ModelMapper;
import com.csipon.crm.dto.mapper.impl.ProductParamMapper;
import com.csipon.crm.service.entity.ProductParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author YARUS
 */
@Service
public class ProductParamServiceImpl implements ProductParamService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductParamDao productParamDao;
    private final ProductParamMapper productParamMapper;

    @Autowired
    public ProductParamServiceImpl(ProductParamDao productParamDao, ProductParamMapper productParamMapper) {
        this.productParamDao = productParamDao;
        this.productParamMapper = productParamMapper;
    }

    @Override
    @Transactional
    public ProductParam create(ProductParamDto productParamDto) {
        ProductParam productParam = ModelMapper.map(productParamMapper.dtoToModel(), productParamDto, RealProductParam.class);
        productParamDao.create(productParam);
        return productParam;
    }

    @Override
    @Transactional
    public boolean update(ProductParamDto productParamDto) {
        ProductParam productParam = ModelMapper.map(productParamMapper.dtoToModel(), productParamDto, RealProductParam.class);
        return productParamDao.update(productParam) > 0;
    }


    @Override
    @Transactional
    public boolean delete(Long id) {
        return productParamDao.delete(id) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductParam> getAllByProductId(Long id) {
        return productParamDao.findAllByProductId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductParam> getAllByParamName(String paramName) {
        return productParamDao.findAllByParamName(paramName);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductParam getById(Long id) {
        return productParamDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutocompleteDto> getAutocompleteDto(String pattern) {
        List<ProductParam> params = productParamDao.findByIdOrTitle(pattern);
        return ModelMapper.mapList(productParamMapper.modelToAutocomplete(), params, AutocompleteDto.class);
    }

}
