package com.csipon.crm.dao;

import com.csipon.crm.domain.model.History;
import com.csipon.crm.dto.GraphDto;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author YARUS
 */
public interface HistoryDao extends CrudDao<History> {
    List<History> findAllByDate(LocalDate date);

    List<History> findAllByOrderId(Long id);
    
    List<History> findAllByComplaintId(Long id);
    
    List<History> findAllByProductId(Long id);

    GraphDto findComplaintHistoryBetweenDateChangeByProductIds(LocalDate fromDate, LocalDate toDate, GraphDto graphDto);
    GraphDto findOrderHistoryBetweenDateChangeByProductIds(LocalDate fromDate, LocalDate toDate, GraphDto graphDto);

}
