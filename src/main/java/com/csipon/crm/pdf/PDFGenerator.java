package com.csipon.crm.pdf;

import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PDFGenerator {

    private final static Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private final static Font SIMPLE_FONT = new Font(Font.FontFamily.HELVETICA, 12);

    public byte[] generate(Order order, User customer, Product product, Discount discount) throws DocumentException, IOException, MessagingException {
        String title = product.getTitle();
        String description = product.getDescription();
        Double price = product.getDefaultPrice();
        Long orderId = order.getId();
        LocalDateTime orderDate = order.getDate();
        String email = customer.getEmail();
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        String phone = "";
        if (customer.getPhone() != null) phone = customer.getPhone();
        Double percentage = 0D;
        if (discount != null) percentage = discount.getPercentage();
        return createPdf(title, description, price, percentage, orderId,
                orderDate, firstName, lastName, phone, email);
    }

    private byte[] createPdf(String title, String description, double price, double discount, Long orderNum,
                             LocalDateTime date, String fName, String lName, String phoneNumber, String email)
            throws DocumentException, IOException {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {

            Document document = new Document();

            try {
                PdfWriter.getInstance(document, stream);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            document.open();

            //insert order details
            document.add(createHeader(orderNum));
            document.add(createOrderDate(date));

            //insert table
            document.add(createTable(title, description, String.valueOf(price)));

            //insert customer's order details
            document.add(createOrderInfoTable(fName, lName, price, discount, phoneNumber, email));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    private Paragraph createHeader(Long orderNum) {

        Paragraph header = new Paragraph(null, BOLD_FONT);
        header.add("Details for Order #" + orderNum);
        header.setFont(BOLD_FONT);
        header.setAlignment(Element.ALIGN_CENTER);
        return header;

    }

    private Paragraph createOrderDate(LocalDateTime date) {

        Paragraph orderDate = new Paragraph(null, SIMPLE_FONT);
        orderDate.add("Order placed: " + date);
        orderDate.setSpacingAfter(15);
        return orderDate;

    }


    private PdfPTable createOrderInfoTable(String fName, String lName, double price, double discount, String phone, String email) {
        PdfPTable infoTable = new PdfPTable(3);

        infoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        infoTable.addCell(new Paragraph("Customer information:", BOLD_FONT));
        infoTable.addCell("");
        infoTable.addCell(new Paragraph("Subtotal: " + price + " $", SIMPLE_FONT));
        infoTable.addCell(new Paragraph(fName + " " + lName, SIMPLE_FONT));
        infoTable.addCell("");
        infoTable.addCell(new Paragraph("Discount: " + (price * discount / 100) + " $", SIMPLE_FONT));
        infoTable.addCell(new Paragraph(phone));
        infoTable.addCell("");
        infoTable.addCell(new Paragraph("Total order: " + (price * (100 - discount) / 100) + " $", BOLD_FONT));
        infoTable.addCell(email);
        infoTable.addCell("");
        infoTable.addCell("");

        return infoTable;

    }

    private PdfPTable createTable(String name, String description, String price) {

        PdfPTable table = new PdfPTable(3);
        // the cell object
        PdfPCell cell = new PdfPCell();

        cell.setRowspan(3);

        cell.setMinimumHeight(50);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Name");
        table.addCell("Description");
        table.addCell("Price, $");

        cell.setRowspan(3);
        table.addCell(name);
        table.addCell(description);
        table.addCell(price);
        table.setSpacingAfter(15);

        return table;

    }

    public String generateFileName() {
        UUID fileName = UUID.randomUUID();

        return fileName.toString() + ".pdf";
    }
}