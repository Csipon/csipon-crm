/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csipon.crm.dao;

import com.csipon.crm.domain.model.ProductParam;

import java.util.List;

/**
 * @author YARUS
 */
public interface ProductParamDao extends CrudDao<ProductParam> {
    List<ProductParam> findAllByParamName(String paramName);

    List<ProductParam> findAllByProductId(Long id);

    List<ProductParam> findByIdOrTitle(String pattern);

}
