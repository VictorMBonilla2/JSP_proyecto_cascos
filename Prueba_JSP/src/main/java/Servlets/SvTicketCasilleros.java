//package Servlets;
//
//
//import br.com.adilson.util.Extenso;
//import br.com.adilson.util.PrinterMatrix;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import javax.print.*;
//
//import javax.print.attribute.HashPrintRequestAttributeSet;
//import javax.print.attribute.PrintRequestAttributeSet;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//
//@WebServlet("/generateTicket")
//public class SvTicketCasilleros extends HttpServlet {
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        PrinterMatrix printerMatrix = new PrinterMatrix();
//
//        String numeroTicket = request.getParameter("numeroTicket");
//        String tipoTicket = request.getParameter("tipoTicket");
//        String fecha = request.getParameter("fecha");
//        String hora = request.getParameter("hora");
//        String valor = request.getParameter("valor");
//
//        Extenso e = new Extenso();
//
//        e.setNumber(101.85);
//
//        printerMatrix.setOutSize(9,32);
//
//        printerMatrix.printCharAtCol(1,1,32,"=");
//        printerMatrix.printTextWrap(1,2,8,32,"FACTURA DE VENTA");
//        printerMatrix.printTextWrap(2,3,1,32, "Numero factura");
//        printerMatrix.printTextWrap(3,3,1,32, "Tipo de ticket");
//        printerMatrix.printTextWrap(4,3,1,32, "Fecha de hora");
//        printerMatrix.printTextWrap(5,3,1,32, "Valor");
//        printerMatrix.printTextWrap(6,3,1,32, "Hora");
//        printerMatrix.printTextWrap(7,3,1,32, "Valor");
//
//        printerMatrix.toFile("impresion.txt");
//
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream("impresion.txt");
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//        }
//        if (fis != null) {
//            response.setContentType("text/plain");
//        }
//        DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
//        Doc document = new SimpleDoc(fis, docFormat, null);
//        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
//        PrintService defaultPrintservice = PrintServiceLookup.lookupDefaultPrintService();
//
//        if (defaultPrintservice != null) {
//            DocPrintJob printJob = defaultPrintservice.createPrintJob();
//            try{
//                printJob.print(document,attributeSet);
//            }catch (Exception ex){
//                ex.printStackTrace();
//            }
//        }
//}
//}
