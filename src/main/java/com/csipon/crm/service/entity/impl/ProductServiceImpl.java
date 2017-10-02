package com.csipon.crm.service.entity.impl;

import com.csipon.crm.domain.model.*;
import com.csipon.crm.domain.real.RealDiscount;
import com.csipon.crm.listener.event.ChangeStatusProductEvent;
import com.csipon.crm.listener.event.CreateProductEvent;
import com.csipon.crm.dao.ProductDao;
import com.netcracker.crm.domain.model.*;
import com.csipon.crm.domain.real.RealGroup;
import com.csipon.crm.domain.real.RealProduct;
import com.csipon.crm.domain.request.ProductRowRequest;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ProductDto;
import com.csipon.crm.dto.bulk.ProductBulkDto;
import com.csipon.crm.dto.mapper.ModelMapper;
import com.csipon.crm.dto.mapper.impl.ProductMapper;
import com.csipon.crm.dto.row.ProductRowDto;
import com.csipon.crm.exception.UnsupportedChangingStatusException;
import com.csipon.crm.service.entity.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Pasha on 30.04.2017.
 */
@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDao productDao;
    private ApplicationEventPublisher publisher;
    private ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ApplicationEventPublisher publisher, ProductMapper productMapper) {
        this.productDao = productDao;
        this.publisher = publisher;
        this.productMapper = productMapper;
    }

    @Override
    public Product getProductsById(Long id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> getProductsByGroupId(Long id) {
        return productDao.findAllByGroupId(id);
    }

    @Override
    @Transactional
    public Product create(ProductDto productDto, User user) {
        Product product = ModelMapper.map(productMapper.dtoToModel(), productDto, RealProduct.class);
        product.setStatus(ProductStatus.PLANNED);
        productDao.create(product);
        publisher.publishEvent(new CreateProductEvent(this, product, user));
        return product;
    }

    @Override
    @Transactional
    public boolean update(ProductDto productDto, User user) {
        Product product = ModelMapper.map(productMapper.dtoToModel(), productDto, RealProduct.class);
        Product productFromDB = productDao.findById(productDto.getId());
        product.setStatus(productFromDB.getStatus());
        return productDao.update(product) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutocompleteDto> getAutocompleteDto(String pattern) {
        List<Product> products = productDao.findAllByPattern(pattern);
        return ModelMapper.mapList(productMapper.modelToAutocomplete(), products, AutocompleteDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutocompleteDto> getAutocompleteDtoWithoutGroup(String pattern) {
        List<Product> products = productDao.findWithoutGroupByPattern(pattern);
        return ModelMapper.mapList(productMapper.modelToAutocomplete(), products, AutocompleteDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutocompleteDto> getActualProductsAutocompleteDtoByCustomer(String pattern, User customer) {
        List<Product> products = productDao.findActualByPatternAndCustomerId(pattern, customer.getId());
        return ModelMapper.mapList(productMapper.modelToAutocomplete(), products, AutocompleteDto.class);
    }

    @Override
    @Transactional
    public boolean bulkUpdate(ProductBulkDto bulkDto, User user) {
        Product productTemplate = getBulkProduct(bulkDto);
        Set<Long> productIDs = new HashSet<>();
        if (bulkDto.getItemIds() != null) productIDs.addAll(bulkDto.getItemIds());
        for (Long productID : productIDs) {
            if (productTemplate.getStatus() != null) changeStatus(productID, productTemplate.getStatus().getId(), user);
        }

        return productDao.bulkUpdate(productIDs, productTemplate);
    }

    @Override
    public List<Product> getProductsByDiscountId(Long id, User user) {
        UserRole role = user.getUserRole();
        List<Product> products = new ArrayList<>();
        if (role.equals(UserRole.ROLE_ADMIN) || role.equals(UserRole.ROLE_CSR) || role.equals(UserRole.ROLE_PMG)) {
            products = productDao.findProductsByDiscountId(id);
        } else if (role.equals(UserRole.ROLE_CUSTOMER)) {
            products = productDao.findProductsByDiscountIdAndCustomerId(id, user.getId());
        }
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutocompleteDto> getPossibleProductsAutocompleteDtoByCustomer(String pattern, User customer) {
        List<Product> products = productDao.findByPatternAndCustomerIdAndRegionId(pattern, customer.getId(), customer.getAddress().getRegion().getId());
        return ModelMapper.mapList(productMapper.modelToAutocomplete(), products, AutocompleteDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getProductsRow(ProductRowRequest orderRowRequest) {
        Map<String, Object> response = new HashMap<>();
        Long length = productDao.getProductRowsCount(orderRowRequest);
        response.put("length", length);
        List<Product> products = productDao.findProductRows(orderRowRequest);

        List<ProductRowDto> productsRowDto = ModelMapper.mapList(productMapper.modelToRowDto(), products, ProductRowDto.class);
        response.put("rows", productsRowDto);
        return response;
    }

    private Product getBulkProduct(ProductBulkDto bulkDto) {
        Product productTemplate = new RealProduct();
        if (bulkDto.isDescriptionChanged()) productTemplate.setDescription(bulkDto.getDescription());
        if (bulkDto.isStatusNameChanged()) productTemplate.setStatus(ProductStatus.valueOf(bulkDto.getStatusName()));
        if (bulkDto.isDefaultPriceChanged()) productTemplate.setDefaultPrice(bulkDto.getDefaultPrice());
        if (bulkDto.isGroupIdChanged()) {
            Group group = new RealGroup();
            group.setId(bulkDto.getGroupId());
            productTemplate.setGroup(group);
        }
        if (bulkDto.isDiscountIdChanged()) {
            Discount discount = new RealDiscount();
            discount.setId(bulkDto.getDiscountId());
            productTemplate.setDiscount(discount);
        }
        return productTemplate;
    }

    @Override
    public boolean hasCustomerAccessToProduct(Long productId, Long customerId) {
        return productDao.hasCustomerAccessToProduct(productId, customerId);
    }

    @Override
    @Transactional
    public boolean changeStatus(Long productId, Long statusId, User user) {
        if (ProductStatus.ACTUAL.getId().equals(statusId)) {
            return changeStatusToActual(productId, user);
        } else if (ProductStatus.OUTDATED.getId().equals(statusId)) {
            return changeStatusToOutdated(productId, user);
        }
        throw new UnsupportedChangingStatusException("Product with id "
                + productId + " hasn't changed status");
    }

    @Transactional
    private boolean changeStatusToOutdated(Long productId, User user) {
        Product product = productDao.findById(productId);
        ChangeStatusProductEvent event = new ChangeStatusProductEvent(this, product, user, ProductStatus.OUTDATED);
        publisher.publishEvent(event);
        if (!event.isDone()) {
            throw new UnsupportedChangingStatusException("Product with id "
                    + productId + " hasn't changed status");
        } else {
            return event.isDone();
        }
    }

    @Transactional
    private boolean changeStatusToActual(Long productId, User user) {
        Product product = productDao.findById(productId);
        ChangeStatusProductEvent event = new ChangeStatusProductEvent(this, product, user, ProductStatus.ACTUAL);
        publisher.publishEvent(event);
        if (!event.isDone()) {
            throw new UnsupportedChangingStatusException("Product with id "
                    + productId + " hasn't changed status");
        } else {
            return event.isDone();
        }
    }
}
