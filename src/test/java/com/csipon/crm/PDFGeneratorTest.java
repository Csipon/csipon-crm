package com.csipon.crm;

import com.csipon.crm.domain.model.*;
import com.csipon.crm.domain.real.RealDiscount;
import com.csipon.crm.domain.real.RealOrder;
import com.csipon.crm.domain.real.RealProduct;
import com.csipon.crm.domain.real.RealUser;
import com.csipon.crm.pdf.PDFGenerator;
import org.junit.Test;

import java.time.LocalDateTime;

public class PDFGeneratorTest {

    @Test
    public void orderPDFGeneratorTest() throws Exception {
        User user = new RealUser();
        user.setEmail("elon@spacex.com");
        user.setFirstName("Elon");
        user.setLastName("Musk");
        user.setPhone("0935168465");

        Order order = new RealOrder();
        Product product = new RealProduct();
        Discount discount = new RealDiscount();

        discount.setId(1488l);
        discount.setPercentage(5.5);
        product.setTitle("Internet");
        product.setDescription("25 mb/s");
        product.setDefaultPrice(55.5);
        product.setDiscount(discount);

        order.setProduct(product);
        order.setId(214748l);
        order.setDate(LocalDateTime.MAX);
        order.setCustomer(user);
        order.setStatus(OrderStatus.DISABLED);

        PDFGenerator pdfGenerator = new PDFGenerator();
        pdfGenerator.generate(order, user, product, discount);

    }
}
