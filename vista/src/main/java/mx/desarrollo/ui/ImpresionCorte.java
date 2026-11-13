package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;
import javax.swing.JOptionPane;


public final class ImpresionCorte {

    private static final double ANCHO_TICKET = 226;

    private ImpresionCorte() {
    }

    public static void imprimir(Component padre, List<String> lineas, boolean ticket, String trabajo) {
        if (lineas == null || lineas.isEmpty()) {
            return;
        }
        final int altoLinea = ticket ? 11 : 13;
        final Font fuente = new Font(Font.MONOSPACED, Font.PLAIN, ticket ? 8 : 10);

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName(trabajo);
        PageFormat formato = job.defaultPage();
        if (ticket) {
            double alto = Math.max(200, lineas.size() * altoLinea + 40);
            Paper papel = new Paper();
            papel.setSize(ANCHO_TICKET, alto);
            papel.setImageableArea(6, 6, ANCHO_TICKET - 12, alto - 12);
            formato.setPaper(papel);
            formato.setOrientation(PageFormat.PORTRAIT);
        }

        job.setPrintable((Graphics g, PageFormat pf, int pagina) -> {
            int porPagina = Math.max(1, (int) (pf.getImageableHeight() / altoLinea));
            int paginas = (int) Math.ceil(lineas.size() / (double) porPagina);
            if (pagina >= paginas) {
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.translate(pf.getImageableX(), pf.getImageableY());
            g2.setFont(fuente);
            g2.setColor(Color.BLACK);
            int inicio = pagina * porPagina;
            int fin = Math.min(lineas.size(), inicio + porPagina);
            for (int i = inicio; i < fin; i++) {
                g2.drawString(lineas.get(i), 0, (i - inicio + 1) * altoLinea - 2);
            }
            return Printable.PAGE_EXISTS;
        }, formato);

        try {
            if (job.printDialog()) {
                job.print();
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(padre,
                    "No se pudo imprimir el corte: " + ex.getMessage(),
                    "Impresión", JOptionPane.ERROR_MESSAGE);
        }
    }
}
