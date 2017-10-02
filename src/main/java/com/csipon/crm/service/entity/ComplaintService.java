package com.csipon.crm.service.entity;

import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ComplaintDto;
import com.csipon.crm.dto.GraphDto;
import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.request.ComplaintRowRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 03.05.2017
 */
public interface ComplaintService {

    Complaint persist(ComplaintDto dto);

    List<Complaint> findByTitle(String title);

    List<Complaint> findByDate(LocalDate date);

    List<Complaint> findByCustomerId(Long id);

    Complaint findById(Long id);

    boolean checkAccessToComplaint(User customer, Long id);

    boolean changeStatusComplaint(Long id, String type, User pmg);

    Map<String, Object> getComplaintRow(ComplaintRowRequest complaintRowRequest, User user, boolean individual);

    List<AutocompleteDto> getAutocompleteDto(String pattern, User user, boolean individual);

    GraphDto getStatisticalGraph(GraphDto graphDto);

    List<History> getHistory(Long complaintId);
}
