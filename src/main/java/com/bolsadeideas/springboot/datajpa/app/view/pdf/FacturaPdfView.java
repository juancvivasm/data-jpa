package com.bolsadeideas.springboot.datajpa.app.view.pdf;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Factura;
import com.bolsadeideas.springboot.datajpa.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.awt.*;
import java.util.Locale;
import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Factura factura = (Factura) model.get("factura");
        Locale locale = localeResolver.resolveLocale(request);

        //02 Forma para obtener los mensajes desde properties
        MessageSourceAccessor mensajes = getMessageSourceAccessor();

        PdfPCell cell = null;

        PdfPTable table01 = new PdfPTable(1);
        table01.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale)));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        table01.addCell(cell);
        table01.addCell(factura.getCliente().getNombre().concat(" ").concat(factura.getCliente().getApellido()));
        table01.addCell(factura.getCliente().getEmail());

        PdfPTable table02 = new PdfPTable(1);
        table02.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.factura", null, locale)));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        table02.addCell(cell);
        table02.addCell(mensajes.getMessage("text.cliente.factura.folio") + ": ".concat(factura.getId().toString()));
        table02.addCell(mensajes.getMessage("text.cliente.factura.descripcion") + ": ".concat(factura.getDescripcion()));
        table02.addCell(mensajes.getMessage("text.cliente.factura.fecha") + ": ".concat(factura.getCreateAt().toString()));

        document.add(table01);
        document.add(table02);

        PdfPTable table03 = new PdfPTable(4);
        table03.setWidths(new float[] {3.3f, 1, 1, 1});
        table03.setSpacingAfter(20);
        table03.addCell(mensajes.getMessage("text.factura.form.item.nombre"));
        table03.addCell(mensajes.getMessage("text.factura.form.item.precio"));
        table03.addCell(mensajes.getMessage("text.factura.form.item.cantidad"));
        table03.addCell(mensajes.getMessage("text.factura.form.item.total"));
        for (ItemFactura item: factura.getItems()) {
            table03.addCell(item.getProducto().getNombre());
            table03.addCell(item.getProducto().getPrecio().toString());

            cell = new PdfPCell(new Phrase(item.getCantidad().toString().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table03.addCell(cell);
            table03.addCell(item.calcularImporte().toString());
        }

        cell = new PdfPCell(new Phrase(mensajes.getMessage("text.factura.form.total")));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table03.addCell(cell);
        table03.addCell(factura.getTotal().toString());

        document.add(table03);
    }
}
