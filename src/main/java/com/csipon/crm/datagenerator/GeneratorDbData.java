package com.csipon.crm.datagenerator;

import com.csipon.crm.datagenerator.impl.*;
import com.csipon.crm.datagenerator.impl.user.AdminSetter;
import com.csipon.crm.datagenerator.impl.user.CsrSetter;
import com.csipon.crm.datagenerator.impl.user.CustomerSetter;
import com.csipon.crm.datagenerator.impl.user.PmgSetter;
import com.csipon.crm.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Pasha on 05.05.2017.
 */
@Service
public class GeneratorDbData {
    private static final int INDEX_DISCOUNT = 100;
    private static final int INDEX_GROUP = 10;
    private static final int INDEX_PRODUCT = 100;
    private static final int INDEX_ORGANIZATION = 500;
    private static final int INDEX_ADMIN = 5;
    private static final int INDEX_CSR = 20;
    private static final int INDEX_PMG = 20;
    private static final int INDEX_CUSTOMER = 5000;
    private static final int INDEX_ORDER = 10_000;
    private static final int INDEX_COMPLAINT = 500;
    private static final int INDEX_ADDRESSES = 5000;
    private static final int INDEX_REGION = 20;
    private static final int INDEX_PRODUCT_PARAM = 100;

    @Autowired
    private DiscountSetter discountSetter;
    @Autowired
    private GroupSetter groupSetter;
    @Autowired
    private ProductSetter productSetter;
    @Autowired
    private OrganizationSetter organizationSetter;
    @Autowired
    private CsrSetter csrSetter;
    @Autowired
    private AdminSetter adminSetter;
    @Autowired
    private PmgSetter pmgSetter;
    @Autowired
    private CustomerSetter customerSetter;
    @Autowired
    private OrderSetter orderSetter;
    @Autowired
    private ComplaintSetter complaintSetter;
    @Autowired
    private RegionSetter regionSetter;
    @Autowired
    private AddressSetter addressSetter;
    @Autowired
    private RegionGroupsSetter regionGroupsSetter;
    @Autowired
    private HistorySetter historySetter;
    @Autowired
    private ProductParamSetter productParamSetter;

    public void generateDataForDB(int number){
        List<Discount> discounts = discountSetter.generate(number * INDEX_DISCOUNT);
        groupSetter.setDiscounts(discounts);
        List<Group> groups = groupSetter.generate(number * INDEX_GROUP);

        productSetter.setDiscounts(discounts);
        productSetter.setGroups(groups);

        List<Product> products = productSetter.generate(number * INDEX_PRODUCT);
        
        productParamSetter.setProducts(products);
        List<ProductParam> productParams = productParamSetter.generate(number * INDEX_PRODUCT);

        List<Region> regions = regionSetter.generate(number * INDEX_REGION);

        regionGroupsSetter.setRegions(regions);
        regionGroupsSetter.setGroups(groups);
//        Relationship many to many
        List emptyList = regionGroupsSetter.generate(0);

        addressSetter.setRegions(regions);

        List<Address> addresses = addressSetter.generate(number * INDEX_ADDRESSES);

        List<Organization> organizations = organizationSetter.generate(number * INDEX_ORGANIZATION);
        customerSetter.setOrganizationList(organizations);
        customerSetter.setAddresses(addresses);
        List<User> customers = customerSetter.generate(number * INDEX_CUSTOMER);
        List<User> pmgs = pmgSetter.generate(number * INDEX_PMG);
        List<User> csrs = csrSetter.generate(number * INDEX_CSR);
        List<User> admins = adminSetter.generate(number * INDEX_ADMIN);


        orderSetter.setCsrs(csrs);
        orderSetter.setCustomers(customers);
        orderSetter.setProducts(regionGroupsSetter.getProductInRegion());

        List<Order> orders = orderSetter.generate(number * INDEX_ORDER);


        complaintSetter.setOrders(orders);
        complaintSetter.setPmgs(pmgs);

        List<Complaint> complaints = complaintSetter.generate(number * INDEX_COMPLAINT);

        historySetter.setOrders(orders);
        historySetter.setProducts(products);
        historySetter.setComplaints(complaints);
        historySetter.generate(number);
    }
}
