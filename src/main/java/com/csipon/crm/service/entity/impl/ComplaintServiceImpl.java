package com.csipon.crm.service.entity.impl;

import com.csipon.crm.dao.ComplaintDao;
import com.csipon.crm.dao.HistoryDao;
import com.csipon.crm.domain.model.*;
import com.csipon.crm.domain.real.RealComplaint;
import com.csipon.crm.domain.request.ComplaintRowRequest;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ComplaintDto;
import com.csipon.crm.dto.GraphDto;
import com.csipon.crm.dto.mapper.ModelMapper;
import com.csipon.crm.dto.mapper.impl.ComplaintMapper;
import com.csipon.crm.dto.row.ComplaintRowDto;
import com.csipon.crm.exception.UnsupportedChangingStatusException;
import com.csipon.crm.listener.event.ChangeStatusComplaintEvent;
import com.csipon.crm.listener.event.CreateComplaintEvent;
import com.csipon.crm.service.entity.ComplaintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 01.05.2017
 */

@Service
public class ComplaintServiceImpl implements ComplaintService {
    private static final Logger log = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private final ApplicationEventPublisher publisher;
    private final ComplaintDao complaintDao;
    private final HistoryDao historyDao;
    private final ComplaintMapper complaintMapper;

    @Autowired
    public ComplaintServiceImpl(ComplaintDao complaintDao, HistoryDao historyDao,
                                ApplicationEventPublisher publisher, ComplaintMapper complaintMapper) {
        this.complaintDao = complaintDao;
        this.historyDao = historyDao;
        this.publisher = publisher;
        this.complaintMapper = complaintMapper;
    }

    @Transactional
    public Complaint persist(ComplaintDto dto) {
        Complaint complaint = ModelMapper.map(complaintMapper.dtoToModel(), dto, RealComplaint.class);
        complaint.setDate(LocalDateTime.now());
        complaint.setStatus(ComplaintStatus.OPEN);
        Long id = complaintDao.create(complaint);
        publisher.publishEvent(new CreateComplaintEvent(this, complaint));
        return complaint;
    }

    @Transactional(readOnly = true)
    public List<Complaint> findByTitle(String title) {
        return complaintDao.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public List<Complaint> findByDate(LocalDate date) {
        return complaintDao.findAllByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Complaint> findByCustomerId(Long id) {
        return complaintDao.findAllByCustomerId(id);
    }

    @Transactional(readOnly = true)
    public Complaint findById(Long id) {
        return complaintDao.findById(id);
    }

    @Transactional(readOnly = true)
    List<AutocompleteDto> getTitlesByPmg(String likeTitle, User pmg) {
        List<String> titles = complaintDao.findComplaintsTitleByPmgId(likeTitle, pmg.getId());
        return ModelMapper.mapList(complaintMapper.modelToAutocomplete(), titles, AutocompleteDto.class);
    }

    @Transactional(readOnly = true)
    List<AutocompleteDto> getAllTitles(String likeTitle) {
        List<String> titles = complaintDao.findComplaintsTitleLikeTitle(likeTitle);
        return ModelMapper.mapList(complaintMapper.modelToAutocomplete(), titles, AutocompleteDto.class);
    }

    @Transactional(readOnly = true)
    List<AutocompleteDto> getTitlesForContactPerson(String likeTitle, Long custId) {
        List<String> titles = complaintDao.findComplaintsTitleForContactPerson(likeTitle, custId);
        return ModelMapper.mapList(complaintMapper.modelToAutocomplete(), titles, AutocompleteDto.class);
    }

    @Transactional(readOnly = true)
    List<AutocompleteDto> getTitlesForNotContactPerson(String likeTitle, Long custId) {
        List<String> titles = complaintDao.findComplaintsTitleByCustId(likeTitle, custId);
        return ModelMapper.mapList(complaintMapper.modelToAutocomplete(), titles, AutocompleteDto.class);
    }

    @Transactional
    @Override
    public boolean changeStatusComplaint(Long complaintId, String type, User pmg) {
        if ("ACCEPT".equals(type)) {
            return acceptComplaint(complaintId, pmg);
        } else if ("CLOSE".equals(type)) {
            return closeComplaint(complaintId, pmg);
        }
        throw new UnsupportedChangingStatusException("Complaint with id "
                + complaintId + " hasn't changed status");
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> getComplaintRow(ComplaintRowRequest complaintRowRequest, User user, boolean individual) {
        UserRole role = user.getUserRole();
        if (role.equals(UserRole.ROLE_CUSTOMER)) {
            complaintRowRequest.setCustId(user.getId());
            if (user.isContactPerson()) {
                complaintRowRequest.setContactPerson(true);
            }
        } else if ((role.equals(UserRole.ROLE_PMG) || role.equals(UserRole.ROLE_ADMIN)) && (individual)) {
            complaintRowRequest.setPmgId(user.getId());
        }
        Map<String, Object> response = new HashMap<>();
        Long length = complaintDao.getComplaintRowsCount(complaintRowRequest);
        response.put("length", length);
        List<Complaint> complaints = complaintDao.findComplaintRows(complaintRowRequest);
        List<ComplaintRowDto> dtos = ModelMapper.mapList(complaintMapper.modelToRowDto(), complaints, ComplaintRowDto.class);
        response.put("rows", dtos);
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AutocompleteDto> getAutocompleteDto(String pattern, User user, boolean individual) {
        UserRole role = user.getUserRole();
        if (role.equals(UserRole.ROLE_PMG) || role.equals(UserRole.ROLE_ADMIN)) {
            if (individual) {
                return getTitlesByPmg(pattern, user);
            } else {
                return getAllTitles(pattern);
            }
        } else if (role.equals(UserRole.ROLE_CUSTOMER)) {
            if (user.isContactPerson()) {
                return getTitlesForContactPerson(pattern, user.getId());
            } else {
                return getTitlesForNotContactPerson(pattern, user.getId());
            }
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public GraphDto getStatisticalGraph(GraphDto graphDto) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(graphDto.getDatePattern());
        LocalDate fromDate = LocalDate.parse(graphDto.getFromDate(), dtf);
        LocalDate toDate = LocalDate.parse(graphDto.getToDate(), dtf);
        return historyDao.findComplaintHistoryBetweenDateChangeByProductIds(fromDate, toDate, graphDto);
    }

    @Transactional
    boolean acceptComplaint(Long complaintId, User user) {
        Complaint complaint = complaintDao.findById(complaintId);

        if (complaint.getPmg() != null) {
            throw new UnsupportedChangingStatusException("Complaint with id "
                    + complaint.getId() + " hasn't changed status");
        }
        complaint.setPmg(user);
        ChangeStatusComplaintEvent event = new ChangeStatusComplaintEvent(this, complaint, ComplaintStatus.SOLVING);
        publisher.publishEvent(event);
        if (!event.isDone()) {
            throw new UnsupportedChangingStatusException("Complaint with id "
                    + complaint.getId() + " hasn't changed status");
        } else {
            return event.isDone();
        }
    }

    @Transactional
    boolean closeComplaint(Long complaintId, User user) {
        Boolean isRoleAdmin = user.getUserRole().equals(UserRole.ROLE_ADMIN);
        Complaint complaint = complaintDao.findById(complaintId);
        if (complaint.getPmg() == null || (!complaint.getPmg().getId().equals(user.getId())) && (!isRoleAdmin)) {
            throw new UnsupportedChangingStatusException("Complaint with id "
                    + complaint.getId() + " hasn't changed status");
        }
        if (isRoleAdmin) {
            complaint.setPmg(user);
        }
        ChangeStatusComplaintEvent event = new ChangeStatusComplaintEvent(this, complaint, ComplaintStatus.CLOSED);
        publisher.publishEvent(event);
        if (!event.isDone()) {
            throw new UnsupportedChangingStatusException("Complaint with id "
                    + complaint.getId() + " hasn't changed status");
        } else {
            return event.isDone();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkAccessToComplaint(User customer, Long complaintId) {
        UserRole role = customer.getUserRole();
        if (role.equals(UserRole.ROLE_ADMIN) || role.equals(UserRole.ROLE_PMG)) {
            return true;
        } else if (role.equals(UserRole.ROLE_CSR)) {
            return false;
        } else if (role.equals(UserRole.ROLE_CUSTOMER)) {
            Long count;
            if (customer.isContactPerson()) {
                count = complaintDao.checkOwnershipOfContactPerson(complaintId, customer.getId());
            } else {
                count = complaintDao.checkOwnershipOfCustomer(complaintId, customer.getId());
            }
            return count > 0;
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public List<History> getHistory(Long complaintId) {
        return historyDao.findAllByComplaintId(complaintId);
    }
}
