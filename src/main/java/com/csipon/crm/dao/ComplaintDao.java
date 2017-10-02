package com.csipon.crm.dao;

import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.domain.request.ComplaintRowRequest;

import java.time.LocalDate;
import java.util.List;

public interface ComplaintDao extends CrudDao<Complaint> {

    List<Complaint> findByTitle(String title);

    List<Complaint> findAllByDate(LocalDate date);

    List<Complaint> findAllByCustomerId(Long id);

    List<Complaint> findComplaintRows(ComplaintRowRequest complaintRowRequest);

    Long getComplaintRowsCount(ComplaintRowRequest complaintRowRequest);

    List<String> findComplaintsTitleLikeTitle(String likeTitle);

    List<Complaint> findAllByProductIds(List<Long> id, LocalDate from, LocalDate to, int orderByIndex);

    List<String> findComplaintsTitleByPmgId(String likeTitle, Long pmgId);

    List<String> findComplaintsTitleByCustId(String likeTitle, Long custId);

    Long checkOwnershipOfContactPerson(Long complaintId, Long custId);

    Long checkOwnershipOfCustomer(Long complaintId, Long custId);

    List<String> findComplaintsTitleForContactPerson(String likeTitle, Long custId);
}
