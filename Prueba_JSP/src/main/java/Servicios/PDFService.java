package Servicios;


import Modelo.Persona;
import Modelo.TbInformesUsuarios;
import Modelo.TbVehiculo;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PDFService {
    // Método para generar el PDF con una persona y su lista de vehículos
    public TbInformesUsuarios generarPDF(Persona persona, List<TbVehiculo> vehiculos) throws UnsupportedEncodingException {
        String sourceFile = URLDecoder.decode(
                PDFService.class.getClassLoader().getResource("prueba.pdf").getPath(), "UTF-8");
        String temporalId = UUID.randomUUID().toString();
        System.out.println("Codigo temporal: "+temporalId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  // Puedes ajustar el formato según tus necesidades
        Date hora = new Date();
        String fechaFormateada = sdf.format(hora);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFile), new PdfWriter(byteArrayOutputStream));  // Cambiado a ByteArrayOutputStream
            Document document = new Document(pdfDoc);

            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            Map<String, PdfFormField> fields = form.getFormFields();

            // Rellenar los campos del formulario con los datos de la persona
            setField(form, "nombre", persona.getNombre() + " " + persona.getApellido());
            setField(form, "correo", persona.getCorreo());
            setField(form, "tipoDocumento", persona.getTipoDocumento().getNombreDocumento());
            setField(form, "numeroDocumento", String.valueOf(persona.getDocumento()));
            setField(form, "EstadoUsuario", persona.getEstadoUsuario().name());
            setField(form, "fechaNacimiento", persona.getFechaNacimiento().toString());
            setField(form, "cantidadVehiculos", String.valueOf(vehiculos.size()));
            setField(form, "codigoInforme", temporalId);
            setField(form, "fechaCreacion", fechaFormateada);

            // Aplanar los campos del formulario
            form.flattenFields();

            // Crear una nueva página y agregar la tabla de vehículos
            pdfDoc.addNewPage();
            document = new Document(pdfDoc, pdfDoc.getDefaultPageSize()); // Asociar el nuevo Document a la nueva página

            // Agregar un salto de página explícito si fuera necesario
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.add(new Paragraph("Lista de Vehículos").setBold().setFontSize(14));

            float[] columnWidths = {100f, 150f, 150f, 100f, 100f};
            String[] headers = {"Placa", "Marca", "Modelo", "Color", "Estado"};
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            // Preparar los datos de los vehículos
            String[][] datosVehiculos = new String[vehiculos.size()][5];
            for (int i = 0; i < vehiculos.size(); i++) {
                TbVehiculo vehiculo = vehiculos.get(i);
                datosVehiculos[i][0] = vehiculo.getPlacaVehiculo();
                datosVehiculos[i][1] = vehiculo.getModeloVehiculo().getMarcaVehiculo().getNombreMarca();
                datosVehiculos[i][2] = vehiculo.getModeloVehiculo().getNombreModelo();
                datosVehiculos[i][3] = vehiculo.getColorVehiculo().name();
                datosVehiculos[i][4] = vehiculo.getEstadoVehiculo().name();
            }

            // Crear la tabla con los datos de los vehículos
            Table table = crearTablaConEstilos(columnWidths, headers, datosVehiculos, font);
            document.add(table);

            // Cerrar el documento
            document.close();
            pdfDoc.close();

            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            TbInformesUsuarios informe = new TbInformesUsuarios();
            informe.setPersona(persona);
            informe.setUploadDate(hora);
            informe.setFileData(pdfBytes);
            informe.setFileName("informe_usuario_" + persona.getDocumento() + ".pdf");
            informe.setFileType("application/pdf");
            informe.setCodigoInforme(temporalId);
            System.out.println("Formulario PDF completado exitosamente.");
            return  informe;

        } catch (IOException e) {
            System.out.println("Error al crear el archivo PDF: " + e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Método para establecer los valores de los campos
    private void setField(PdfAcroForm form, String fieldName, String value) {
        PdfFormField field = form.getField(fieldName);
        if (field != null) {
            field.setValue(value);
            System.out.println("Valor aplicado a " + fieldName + ": " + value);
        } else {
            System.out.println("El campo '" + fieldName + "' no fue encontrado.");
        }
    }
    // Método para personalizar las celdas del encabezado
    private Cell personalizarEncabezado(Cell celda, PdfFont font) {
        celda.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        celda.setFont(font);
        celda.setFontSize(12);
        celda.setFontColor(ColorConstants.WHITE);
        celda.setTextAlignment(TextAlignment.CENTER);
        celda.setPadding(5);  // Espaciado dentro de la celda
        return celda;
    }

    // Método para personalizar las celdas de datos
    private Cell personalizarCeldaDato(Cell celda, PdfFont font) {
        celda.setFont(font);
        celda.setFontSize(10);
        celda.setTextAlignment(TextAlignment.LEFT);
        celda.setPadding(5);  // Espaciado dentro de la celda
        return celda;
    }
    public Table crearTablaConEstilos(float[] columnWidths, String[] headers, String[][] datos, PdfFont font) {
        Table table = new Table(columnWidths);

        // Personalizar y agregar encabezados
        for (String header : headers) {
            Cell headerCell = personalizarEncabezado(new Cell(), font);
            headerCell.add(new Paragraph(header));
            table.addHeaderCell(headerCell);
        }

        // Personalizar y agregar los datos
        for (String[] fila : datos) {
            for (String dato : fila) {
                Cell dataCell = personalizarCeldaDato(new Cell(), font);
                dataCell.add(new Paragraph(dato));
                table.addCell(dataCell);
            }
        }

        return table;
    }
}

