// package com.heroku.java.service;

// import com.heroku.java.model.Payroll;
// import com.itextpdf.text.Document;
// import com.itextpdf.text.DocumentException;
// import com.itextpdf.text.Paragraph;
// import com.itextpdf.text.pdf.PdfWriter;
// import org.springframework.stereotype.Service;

// import java.io.ByteArrayOutputStream;
// import java.util.List;

// @Service
// public class PdfService {

//     public byte[] generatePayrollPdf(List<Payroll> payrolls) {
//         ByteArrayOutputStream baos = new ByteArrayOutputStream();
//         Document document = new Document();
//         try {
//             PdfWriter.getInstance(document, baos);
//             document.open();

//             document.add(new Paragraph("Payroll Records"));

//             for (Payroll payroll : payrolls) {
//                 document.add(new Paragraph("Staff Name: " + payroll.getStaffName()));
//                 document.add(new Paragraph("IC: " + payroll.getIc()));
//                 document.add(new Paragraph("Address: " + payroll.getAddress()));
//                 document.add(new Paragraph("Month: " + payroll.getMonth()));
//                 document.add(new Paragraph("Hours Worked: " + payroll.getHoursWorked()));
//                 document.add(new Paragraph("Hourly Rate: " + payroll.getHourlyRate()));
//                 document.add(new Paragraph("Total Pay: " + payroll.getTotalPay()));
//                 document.add(new Paragraph(" "));
//             }
//             document.close();
//         } catch (DocumentException e) {
//             e.printStackTrace(); // Handle this exception properly in your real code
//         }

//         return baos.toByteArray();
//     }
// }
