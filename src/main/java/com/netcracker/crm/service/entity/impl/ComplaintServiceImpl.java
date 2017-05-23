package com.netcracker.crm.service.entity.impl;

import com.netcracker.crm.dao.ComplaintDao;
import com.netcracker.crm.dao.HistoryDao;
import com.netcracker.crm.domain.model.*;
import com.netcracker.crm.domain.real.RealComplaint;
import com.netcracker.crm.domain.request.ComplaintRowRequest;
import com.netcracker.crm.dto.AutocompleteDto;
import com.netcracker.crm.dto.ComplaintDto;
import com.netcracker.crm.dto.GraphDto;
import com.netcracker.crm.dto.mapper.ModelMapper;
import com.netcracker.crm.dto.mapper.impl.ComplaintMapper;
import com.netcracker.crm.dto.row.ComplaintRowDto;
import com.netcracker.crm.listener.event.ChangeStatusComplaintEvent;
import com.netcracker.crm.listener.event.CreateComplaintEvent;
import com.netcracker.crm.service.entity.ComplaintService;
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
        complaint.setId(id);
        publisher.publishEvent(new CreateComplaintEvent(this, complaint));
        return complaint;
    }

    @Transactional
    public List<Complaint> findByTitle(String title) {
        return complaintDao.findByTitle(title);
    }

    @Transactional
    public List<Complaint> findByDate(LocalDate date) {
        return complaintDao.findAllByDate(date);
    }

    @Transactional
    public List<Complaint> findByCustomerId(Long id) {
        return complaintDao.findAllByCustomerId(id);
    }

    @Transactional
    public Complaint findById(Long id) {
        return complaintDao.findById(id);
    }

    @Transactional
    private List<AutocompleteDto> getTitlesByPmg(String likeTitle, User pmg) {
        List<String> titles = complaintDao.findComplaintsTitleByPmgId(likeTitle, pmg.getId());
        return ModelMapper.mapList(complaintMapper.modelToAutocomplete(), titles, AutocompleteDto.class);
    }

    @Transactional
    private List<AutocompleteDto> getAllTitles(String likeTitle) {
        List<String> titles = complaintDao.findComplaintsTitleLikeTitle(likeTitle);
        return ModelMapper.mapList(complaintMapper.modelToAutocomplete(), titles, AutocompleteDto.class);
    }

    @Transactional
    private List<AutocompleteDto> getTitlesForContactPerson(String likeTitle, Long custId) {
        List<String> titles = complaintDao.findComplaintsTitleForContactPerson(likeTitle, custId);
        return ModelMapper.mapList(complaintMapper.modelToAutocomplete(), titles, AutocompleteDto.class);
    }

    @Transactional
    private List<AutocompleteDto> getTitlesForNotContactPerson(String likeTitle, Long custId) {
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
        return false;
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
    public GraphDto getStatisticalGraph(GraphDto graphDto) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(graphDto.getDatePattern());
        LocalDate fromDate = LocalDate.parse(graphDto.getFromDate(), dtf);
        LocalDate toDate = LocalDate.parse(graphDto.getToDate(), dtf);
        return historyDao.findComplaintHistoryBetweenDateChangeByProductIds(fromDate, toDate, graphDto);
    }

    @Transactional
    private boolean acceptComplaint(Long complaintId, User user) {
        Complaint complaint = complaintDao.findById(complaintId);

        if (complaint.getPmg() != null) {
            return false;
        }
        complaint.setPmg(user);
        ChangeStatusComplaintEvent event = new ChangeStatusComplaintEvent(this, complaint, ComplaintStatus.SOLVING);
        publisher.publishEvent(event);
        return event.isDone();
    }

    @Transactional
    private boolean closeComplaint(Long complaintId, User user) {
        Boolean isRoleAdmin = user.getUserRole().equals(UserRole.ROLE_ADMIN);
        Complaint complaint = complaintDao.findById(complaintId);
        if (complaint.getPmg() == null || (!complaint.getPmg().getId().equals(user.getId())) && (!isRoleAdmin)) {
            return false;
        }
        if (isRoleAdmin) {
            complaint.setPmg(user);
        }
        ChangeStatusComplaintEvent event = new ChangeStatusComplaintEvent(this, complaint, ComplaintStatus.CLOSED);
        publisher.publishEvent(event);
        return event.isDone();
    }

    @Transactional
    @Override
    public boolean checkAccessToComplaint(User customer, Long complaintId) {
        UserRole role = customer.getUserRole();
        if (role.equals(UserRole.ROLE_ADMIN) || role.equals(UserRole.ROLE_PMG)) {
            return true;
        } else if (role.equals(UserRole.ROLE_CSR)) {
            return false;
        } else if (role.equals(UserRole.ROLE_CUSTOMER)) {
            Long count = null;
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
