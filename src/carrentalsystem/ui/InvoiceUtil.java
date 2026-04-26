package carrentalsystem.ui;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.util.Date;

public class InvoiceUtil {
    // Professional Branding Colors for PDF
    private static final BaseColor NAVY = new BaseColor(11, 61, 145);
    private static final BaseColor LIGHT_GREY = new BaseColor(245, 245, 245);

    public static void createSimpleInvoice(String filename, String invoiceNo, String customer, String car, String rentDate, String returnDate, double total) throws Exception {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(filename));
        doc.open();

        // 1. Header & Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, NAVY);
        Paragraph title = new Paragraph("CAR RENTAL SYSTEM", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);

        Paragraph subtitle = new Paragraph("Official Payment Receipt", FontFactory.getFont(FontFactory.HELVETICA, 12));
        subtitle.setAlignment(Element.ALIGN_CENTER);
        doc.add(subtitle);
        
        doc.add(new Chunk(new LineSeparator()));
        doc.add(new Paragraph(" ")); // Spacer

        // 2. Invoice Meta Info (In two columns or simple lines)
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        doc.add(new Paragraph("Invoice No: " + invoiceNo, boldFont));
        doc.add(new Paragraph("Date Generated: " + new Date().toString()));
        doc.add(new Paragraph(" "));

        // 3. Billing & Details Table
        PdfPTable detailsTable = new PdfPTable(2);
        detailsTable.setWidthPercentage(100);
        detailsTable.setSpacingBefore(10f);

        addCellWithStyle(detailsTable, "CUSTOMER DETAILS", NAVY, true);
        addCellWithStyle(detailsTable, "VEHICLE DETAILS", NAVY, true);

        addCellWithStyle(detailsTable, customer, BaseColor.BLACK, false);
        addCellWithStyle(detailsTable, car, BaseColor.BLACK, false);
        
        doc.add(detailsTable);
        doc.add(new Paragraph(" "));

        // 4. Financial Breakdown Table
        PdfPTable financialTable = new PdfPTable(2);
        financialTable.setWidthPercentage(100);
        financialTable.setWidths(new float[]{3f, 1f});

        // Table Headers
        addHeaderCell(financialTable, "Description");
        addHeaderCell(financialTable, "Amount");

        // Table Data
        financialTable.addCell("Rental Period: " + rentDate + " to " + returnDate);
        financialTable.addCell("Rs" + total);

        // Total Row
        PdfPCell totalLabel = new PdfPCell(new Phrase("GRAND TOTAL", boldFont));
        totalLabel.setBackgroundColor(LIGHT_GREY);
        totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        financialTable.addCell(totalLabel);

        PdfPCell totalVal = new PdfPCell(new Phrase("Rs" + total, boldFont));
        totalVal.setBackgroundColor(LIGHT_GREY);
        financialTable.addCell(totalVal);

        doc.add(financialTable);

        // 5. Footer
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Thank you for choosing our service!\nPlease drive safely.", 
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        doc.add(footer);

        doc.close();
    }

    private static void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE)));
        cell.setBackgroundColor(NAVY);
        cell.setPadding(8f);
        table.addCell(cell);
    }

    private static void addCellWithStyle(PdfPTable table, String text, BaseColor color, boolean isHeader) {
        Font font = isHeader ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, color) : FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        table.addCell(cell);
    }
}