package com.imperial.utilidad;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.chart.Chart;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

public class GeneracionPDF {

    private GeneracionPDF() {
        throw new UnsupportedOperationException(Constantes.ERROR_CLASE_UTILERIA);
    }

    public static void generarReporte(File archivo, String tituloReporte, List<String> datosTexto, Chart grafica) throws DocumentException, IOException {
        Document documento = new Document();
        PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(archivo));
        
        Membrete eventoMembrete = new Membrete();
        writer.setPageEvent(eventoMembrete);

        documento.open();

        crearPortada(documento, tituloReporte);
        
        documento.newPage();

        documento.add(new Paragraph("Gráfica Representativa", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        documento.add(new Paragraph(" "));

        if (grafica != null) {
            WritableImage imagenFx = grafica.snapshot(new javafx.scene.SnapshotParameters(), null);
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(imagenFx, null), "png", byteOutput);
            Image imagenPdf = Image.getInstance(byteOutput.toByteArray());
            imagenPdf.scaleToFit(500, 300);
            imagenPdf.setAlignment(Element.ALIGN_CENTER);
            documento.add(imagenPdf);
        }

        documento.add(new Paragraph(" "));
        documento.add(new Paragraph("Detalle de Información:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        documento.add(new Paragraph(" "));
        
        Font fuenteDatos = FontFactory.getFont(FontFactory.COURIER, 10);
        for (String linea : datosTexto) {
            documento.add(new Paragraph(linea, fuenteDatos));
        }

        documento.close();
    }

    private static void crearPortada(Document documento, String titulo) throws DocumentException {
        Paragraph espacio = new Paragraph("\n\n\n\n\n");
        documento.add(espacio);

        Paragraph empresa = new Paragraph("IMPERIAL MOTORS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 30, BaseColor.DARK_GRAY));
        empresa.setAlignment(Element.ALIGN_CENTER);
        documento.add(empresa);

        Paragraph subtitulo = new Paragraph("Sistema de Gestión Vehicular", FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.GRAY));
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(subtitulo);

        documento.add(new Paragraph("\n\n"));

        Paragraph tituloRep = new Paragraph(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22));
        tituloRep.setAlignment(Element.ALIGN_CENTER);
        documento.add(tituloRep);

        documento.add(new Paragraph("\n\n\n"));

        Paragraph fecha = new Paragraph("Fecha de generación: " + LocalDate.now(), FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12));
        fecha.setAlignment(Element.ALIGN_RIGHT);
        documento.add(fecha);
    }

    static class Membrete extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("Imperial Motors - Reporte Oficial", FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY)),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + 10, 0);

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("Página %d", writer.getPageNumber()), FontFactory.getFont(FontFactory.HELVETICA, 8)),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);
        }
    }
    
    public static void generarReporteTabla(File archivo, String tituloReporte, List<String> encabezados, List<List<String>> datos) throws DocumentException, IOException {
        Document documento = new Document();
        PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(archivo));
        
        Membrete eventoMembrete = new Membrete();
        writer.setPageEvent(eventoMembrete);

        documento.open();

        crearPortada(documento, tituloReporte);
        
        documento.newPage();

        Paragraph tituloSeccion = new Paragraph("Detalle de Inventario", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY));
        tituloSeccion.setSpacingAfter(20);
        documento.add(tituloSeccion);

        PdfPTable tabla = new PdfPTable(encabezados.size());
        tabla.setWidthPercentage(100); // Ancho completo
        tabla.setSpacingBefore(10f);
        tabla.setSpacingAfter(10f);

        Font fuenteCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        Font fuenteDatos = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

        for (String encabezado : encabezados) {
            PdfPCell celda = new PdfPCell(new Phrase(encabezado, fuenteCabecera));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.DARK_GRAY);
            celda.setPadding(8);
            tabla.addCell(celda);
        }

        for (List<String> fila : datos) {
            for (String dato : fila) {
                PdfPCell celda = new PdfPCell(new Phrase(dato, fuenteDatos));
                celda.setPadding(5);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                
                if (dato.startsWith("$") || dato.matches("\\d+")) {
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                } else {
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                }
                
                tabla.addCell(celda);
            }
        }

        documento.add(tabla);
        documento.close();
    }
}