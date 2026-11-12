package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import mx.desarrollo.delegate.DelegateVenta;
import mx.desarrollo.entity.DetalleVentaProducto;
import mx.desarrollo.entity.Ticket;
import mx.desarrollo.entity.Venta;

public class TicketsPanel extends javax.swing.JPanel {


    public TicketsPanel() {
        initComponents();
        configurarConsulta();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        txtBuscar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstTickets = new javax.swing.JList<>();
        btnPdf = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        lblFecha = new javax.swing.JLabel();
        LBLIVA = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        lblFolio = new javax.swing.JLabel();
        lblRegistrados = new javax.swing.JLabel();
        lblIva = new javax.swing.JLabel();
        lblMetodoPago = new javax.swing.JLabel();
        LBLsubtotal1 = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        LBLsubtotal2 = new javax.swing.JLabel();
        LBLIVA1 = new javax.swing.JLabel();
        lblTotal1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));

        jLabel2.setBackground(new java.awt.Color(136, 180, 123));
        jLabel2.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(136, 180, 123));
        jLabel2.setText("Consultas");

        jLabel4.setFont(new java.awt.Font("Instrument Sans", 0, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(94, 138, 84));
        jLabel4.setText("Estado:");

        jLabel6.setFont(new java.awt.Font("Instrument Sans", 0, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(94, 138, 84));
        jLabel6.setText("Ordenar por");

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Producto", "Precio", "Existencias", "Descuento", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProductos.setFocusable(false);
        tablaProductos.setName(""); // NOI18N
        tablaProductos.setRowHeight(52);
        tablaProductos.setRowSelectionAllowed(false);
        tablaProductos.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(tablaProductos);

        jLabel7.setBackground(new java.awt.Color(136, 180, 123));
        jLabel7.setFont(new java.awt.Font("Instrument Sans", 0, 16)); // NOI18N
        jLabel7.setText("Filtrar por:");

        jComboBox1.setBackground(new java.awt.Color(217, 217, 217));
        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre (A-Z)", "Nombre (Z-A)", "Precio (Menor a mayor)", "Precio (Mayor a menor)", "Existencias( Menor a mayor)", "Existencias (Mayor a menor)" }));
        jComboBox1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jComboBox1.setName("listaorden"); // NOI18N
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setBackground(new java.awt.Color(217, 217, 217));
        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Disponible", "Bajas Existencias", "Sin Existencias" }));
        jComboBox2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        jTextField1.setName("txtBuscar"); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(255, 255, 190));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(165, 75, 29));
        jLabel1.setText("1 producto con pocas existencias");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 225, 150)));
        jLabel1.setName("lblAlerta"); // NOI18N
        jLabel1.setOpaque(true);

        setMinimumSize(new java.awt.Dimension(1280, 770));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(1280, 770));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 770));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator3.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator3.setForeground(new java.awt.Color(136, 180, 123));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1010, 20));

        jLabel11.setBackground(new java.awt.Color(136, 180, 123));
        jLabel11.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(136, 180, 123));
        jLabel11.setText("Tickets");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel13.setFont(new java.awt.Font("Instrument Sans", 0, 13)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(94, 138, 84));
        jLabel13.setText("Ordenar por");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel14.setBackground(new java.awt.Color(136, 180, 123));
        jLabel14.setFont(new java.awt.Font("Instrument Sans", 0, 16)); // NOI18N
        jLabel14.setText("Filtrar por:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, 30));

        jComboBox5.setBackground(new java.awt.Color(217, 217, 217));
        jComboBox5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre (A-Z)", "Nombre (Z-A)", "Precio (Menor a mayor)", "Precio (Mayor a menor)", "Existencias( Menor a mayor)", "Existencias (Mayor a menor)" }));
        jComboBox5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jComboBox5.setName("listaorden"); // NOI18N
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 170, 30));

        txtBuscar.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtBuscar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        txtBuscar.setName("txtBuscar"); // NOI18N
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 270, 40));

        lstTickets.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(lstTickets);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 190, 460));

        btnPdf.setBackground(new java.awt.Color(136, 180, 123));
        btnPdf.setFont(new java.awt.Font("Instrument Sans", 0, 12)); // NOI18N
        btnPdf.setForeground(new java.awt.Color(255, 255, 255));
        btnPdf.setText("PDF");
        btnPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfActionPerformed(evt);
            }
        });
        jPanel1.add(btnPdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 130, 150, 30));

        btnImprimir.setBackground(new java.awt.Color(136, 180, 123));
        btnImprimir.setFont(new java.awt.Font("Instrument Sans", 0, 12)); // NOI18N
        btnImprimir.setForeground(new java.awt.Color(255, 255, 255));
        btnImprimir.setText("Imprimir");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        jPanel1.add(btnImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 130, 150, 30));

        lblFecha.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(153, 153, 153));
        lblFecha.setText("2026-02-15     14:38");
        jPanel1.add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 200, 140, 20));

        LBLIVA.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        LBLIVA.setForeground(new java.awt.Color(153, 153, 153));
        LBLIVA.setText("Metodo de pago");
        jPanel1.add(LBLIVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 530, -1, -1));

        tblProductos.setFont(new java.awt.Font("Lucida Console", 0, 10)); // NOI18N
        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "PRODUCTO", "CANT", "IMPORTE"
            }
        ));
        tblProductos.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tblProductos.setShowGrid(false);
        jScrollPane2.setViewportView(tblProductos);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 220, 490, 200));

        lblFolio.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblFolio.setText("T-123");
        jPanel1.add(lblFolio, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 190, -1, -1));

        lblRegistrados.setBackground(new java.awt.Color(232, 240, 229));
        lblRegistrados.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblRegistrados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblRegistrados.setOpaque(true);
        jPanel1.add(lblRegistrados, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 490, 50));

        lblIva.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblIva.setForeground(new java.awt.Color(153, 153, 153));
        lblIva.setText("$1234.00");
        jPanel1.add(lblIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 470, -1, -1));

        lblMetodoPago.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblMetodoPago.setForeground(new java.awt.Color(153, 153, 153));
        lblMetodoPago.setText("Tarjeta");
        jPanel1.add(lblMetodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 528, 90, 20));

        LBLsubtotal1.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        LBLsubtotal1.setForeground(new java.awt.Color(153, 153, 153));
        LBLsubtotal1.setText("Subtotal:");
        jPanel1.add(LBLsubtotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 440, -1, -1));

        lblSubtotal.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(153, 153, 153));
        lblSubtotal.setText("$1234.00");
        jPanel1.add(lblSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 440, -1, -1));
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 430, 490, 40));

        LBLsubtotal2.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        LBLsubtotal2.setText("Total:");
        jPanel1.add(LBLsubtotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 500, -1, -1));

        LBLIVA1.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        LBLIVA1.setForeground(new java.awt.Color(153, 153, 153));
        LBLIVA1.setText("IVA:");
        jPanel1.add(LBLIVA1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 470, -1, -1));

        lblTotal1.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblTotal1.setText("$1234.00");
        jPanel1.add(lblTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 503, -1, 20));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 490, 490, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel1.getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
        
    }//GEN-LAST:event_btnPdfActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
    }//GEN-LAST:event_btnImprimirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LBLIVA;
    private javax.swing.JLabel LBLIVA1;
    private javax.swing.JLabel LBLsubtotal1;
    private javax.swing.JLabel LBLsubtotal2;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnPdf;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFolio;
    private javax.swing.JLabel lblIva;
    private javax.swing.JLabel lblMetodoPago;
    private javax.swing.JLabel lblRegistrados;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JList<String> lstTickets;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables

    private static final Font FUENTE = new Font("Instrument Sans", Font.PLAIN, 14);
    private static final Color VERDE = new Color(136, 180, 123);
    private static final Color VERDE_CLARO = new Color(232, 240, 229);
    private static final Color LINEA = new Color(225, 225, 225);
    private static final Color GRIS = new Color(153, 153, 153);
    private static final Color TEXTO = new Color(35, 35, 35);
    private static final String[] MESES = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
    private static final int BORDE_DER = 975;

    private final DelegateVenta negocio = new DelegateVenta();
    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
    private final DefaultListModel<Ticket> modeloTickets = new DefaultListModel<>();
    private final JLabel lblSinSeleccion = new JLabel("Selecciona un ticket para ver su detalle", SwingConstants.CENTER);
    private final javax.swing.Timer temporizadorBusqueda = new javax.swing.Timer(250, e -> recargar());
    private List<Ticket> cargados = new ArrayList<>();
    private List<DetalleVentaProducto> detalleActual = new ArrayList<>();
    private Component[] grupoDetalle;

    private void configurarConsulta() {
        jPanel1.doLayout();   // aplica las posiciones del diseno para poder leerlas

        grupoDetalle = new Component[]{
            lblRegistrados, lblFolio, lblFecha, jScrollPane2, jSeparator4,
            LBLsubtotal1, lblSubtotal, LBLIVA1, lblIva, jSeparator2,
            LBLsubtotal2, lblTotal1, LBLIVA, lblMetodoPago, btnImprimir, btnPdf};

        configurarBuscador();
        configurarOrden();
        configurarLista();
        configurarTablaDetalle();
        alinearValores();

        // texto que se ve mientras no hay ningun ticket seleccionado
        lblSinSeleccion.setFont(FUENTE.deriveFont(16f));
        lblSinSeleccion.setForeground(GRIS);
        jPanel1.add(lblSinSeleccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 300, 490, 30));

        btnImprimir.addActionListener(e -> abrirTicket(false));
        btnPdf.addActionListener(e -> abrirTicket(true));

        // al volver a mostrar esta pantalla se actualiza la lista (por si hubo ventas nuevas)
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                recargar();
            }
        });

        ocultarDetalle();
        recargar();
    }


    private void configurarBuscador() {
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                txtBuscar.getBorder(), BorderFactory.createEmptyBorder(0, 8, 0, 8)));

        // "Buscar ticket..." en gris mientras la caja esta vacia (no estorba al hacer clic)
        JLabel pista = new JLabel("Buscar ticket...") {
            @Override
            public boolean contains(int x, int y) {
                return false;
            }
        };
        pista.setFont(txtBuscar.getFont());
        pista.setForeground(GRIS);
        jPanel1.add(pista, new org.netbeans.lib.awtextra.AbsoluteConstraints(
                txtBuscar.getX() + 10, txtBuscar.getY(), txtBuscar.getWidth() - 20, txtBuscar.getHeight()), 0);

        temporizadorBusqueda.setRepeats(false);
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            private void cambio() {
                pista.setVisible(txtBuscar.getText().isEmpty());
                temporizadorBusqueda.restart();   // busca 0.25 s despues de dejar de escribir
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                cambio();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                cambio();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                cambio();
            }
        });
    }

    private void configurarOrden() {
        jComboBox5.setModel(new DefaultComboBoxModel<>(new String[]{
            "Más recientes", "Más antiguos", "Total (mayor a menor)", "Total (menor a mayor)"}));
        jComboBox5.addActionListener(e -> mostrarLista(idSeleccionado()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private JList<Ticket> lista() {
        return (JList<Ticket>) (JList) lstTickets;
    }

    private void configurarLista() {
        JList<Ticket> lista = lista();
        lista.setModel(modeloTickets);
        lista.setCellRenderer(new TicketRenderer());
        lista.setFixedCellHeight(58);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setBackground(Color.WHITE);
        jScrollPane3.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, LINEA));
        jScrollPane3.getViewport().setBackground(Color.WHITE);
        jScrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.getVerticalScrollBar().setUnitIncrement(18);
        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarSeleccion();
            }
        });
    }

    private void configurarTablaDetalle() {
        tblProductos.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{"Producto", "Cant.", "Precio", "Total"}) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        });
        tblProductos.setFont(FUENTE);
        tblProductos.setForeground(TEXTO);
        tblProductos.setBackground(Color.WHITE);
        tblProductos.setRowHeight(34);
        tblProductos.setShowGrid(false);
        tblProductos.setShowHorizontalLines(true);
        tblProductos.setGridColor(LINEA);
        tblProductos.setIntercellSpacing(new Dimension(0, 1));
        tblProductos.setFocusable(false);
        tblProductos.setRowSelectionAllowed(false);
        tblProductos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        int[] anchos = {230, 70, 95, 95};
        for (int i = 0; i < anchos.length; i++) {
            tblProductos.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        tblProductos.getColumnModel().getColumn(0).setCellRenderer(new CeldaRenderer(SwingConstants.LEFT));
        tblProductos.getColumnModel().getColumn(1).setCellRenderer(new CeldaRenderer(SwingConstants.CENTER));
        tblProductos.getColumnModel().getColumn(2).setCellRenderer(new CeldaRenderer(SwingConstants.RIGHT));
        tblProductos.getColumnModel().getColumn(3).setCellRenderer(new CeldaRenderer(SwingConstants.RIGHT));

        JTableHeader encabezado = tblProductos.getTableHeader();
        encabezado.setReorderingAllowed(false);
        encabezado.setResizingAllowed(false);
        encabezado.setDefaultRenderer(new EncabezadoRenderer());
        encabezado.setPreferredSize(new Dimension(10, 32));

        jScrollPane2.setBorder(BorderFactory.createEmptyBorder());
        jScrollPane2.getViewport().setBackground(Color.WHITE);
        jScrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void alinearValores() {
        derecha(lblFecha, 970, 160);
        derecha(lblSubtotal, BORDE_DER, 180);
        derecha(lblIva, BORDE_DER, 180);
        derecha(lblTotal1, BORDE_DER, 180);
        derecha(lblMetodoPago, BORDE_DER, 180);
    }

    private void derecha(JLabel etiqueta, int borde, int ancho) {
        etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        int x = borde - ancho;
        int alto = Math.max(etiqueta.getHeight(), etiqueta.getPreferredSize().height);
        ((org.netbeans.lib.awtextra.AbsoluteLayout) jPanel1.getLayout()).addLayoutComponent(
                etiqueta, new org.netbeans.lib.awtextra.AbsoluteConstraints(x, etiqueta.getY(), ancho, alto));
        etiqueta.setBounds(x, etiqueta.getY(), ancho, alto);
    }

 
    public void recargar() {
        Integer id = idSeleccionado();
        try {
            cargados = new ArrayList<>(negocio.buscarTickets(txtBuscar.getText()));
        } catch (Exception | ExceptionInInitializerError ex) {
            cargados = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los tickets: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        mostrarLista(id);
    }

    private Integer idSeleccionado() {
        Ticket t = lista().getSelectedValue();
        return t == null ? null : t.getIdTicket();
    }

    private void mostrarLista(Integer idSeleccionado) {
        Comparator<Ticket> orden;
        switch (jComboBox5.getSelectedIndex()) {
            case 1:
                orden = Comparator.comparing(Ticket::getFechaExpedicion).thenComparing(Ticket::getIdTicket);
                break;
            case 2:
                orden = Comparator.comparing((Ticket t) -> t.getVenta().getTotal()).reversed();
                break;
            case 3:
                orden = Comparator.comparing((Ticket t) -> t.getVenta().getTotal());
                break;
            default:
                orden = Comparator.comparing(Ticket::getFechaExpedicion).thenComparing(Ticket::getIdTicket).reversed();
        }
        cargados.sort(orden);

        modeloTickets.clear();
        int indice = -1;
        for (int i = 0; i < cargados.size(); i++) {
            modeloTickets.addElement(cargados.get(i));
            if (idSeleccionado != null && idSeleccionado.equals(cargados.get(i).getIdTicket())) {
                indice = i;
            }
        }
        if (indice >= 0) {
            lista().setSelectedIndex(indice);
            lista().ensureIndexIsVisible(indice);
        }
    }

    private void mostrarSeleccion() {
        Ticket t = lista().getSelectedValue();
        if (t == null) {
            ocultarDetalle();
            return;
        }
        try {
            detalleActual = negocio.detallesDeVenta(t.getVenta().getIdVenta());
        } catch (Exception | ExceptionInInitializerError ex) {
            detalleActual = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "No se pudo cargar el ticket: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ocultarDetalle();
            return;
        }
        Venta v = t.getVenta();

        lblFolio.setText(t.getFolio());
        lblFecha.setText(formatoFecha(t.getFechaExpedicion(), "   "));

        DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();
        modelo.setRowCount(0);
        for (DetalleVentaProducto d : detalleActual) {
            double precio = d.getPrecioUnitario();
            modelo.addRow(new Object[]{d.getProducto().getNombre(), d.getCantidad(),
                moneda.format(precio), moneda.format(precio * d.getCantidad())});
        }

        lblSubtotal.setText(moneda.format(v.getSubtotal()));
        lblIva.setText(moneda.format(DelegateVenta.iva(v)));
        lblTotal1.setText(moneda.format(v.getTotal()));
        lblMetodoPago.setText(capitalizar(v.getMetodoPago()));

        for (Component c : grupoDetalle) {
            c.setVisible(true);
        }
        lblSinSeleccion.setVisible(false);
        jPanel1.repaint();
    }

    private void ocultarDetalle() {
        if (grupoDetalle == null) {
            return;
        }
        for (Component c : grupoDetalle) {
            c.setVisible(false);
        }
        lblSinSeleccion.setVisible(true);
        jPanel1.repaint();
    }

    private void abrirTicket(boolean pdf) {
        Ticket t = lista().getSelectedValue();
        if (t == null) {
            return;
        }
        Venta v = t.getVenta();
        List<TicketDialog.Linea> lineas = new ArrayList<>();
        for (DetalleVentaProducto d : detalleActual) {
            lineas.add(new TicketDialog.Linea(d.getProducto().getNombre(), d.getCantidad(),
                    d.getPrecioUnitario() * d.getCantidad()));
        }
        java.awt.Window ventana = SwingUtilities.getWindowAncestor(this);
        java.awt.Frame dueno = ventana instanceof java.awt.Frame ? (java.awt.Frame) ventana : null;

        TicketDialog dialogo = new TicketDialog(dueno, t.getFolio(), v.getFecha(),
                v.getUsuario().getNombrePersona(), capitalizar(v.getMetodoPago()),
                lineas, DelegateVenta.iva(v), v.getTotal());
        if (pdf) {
            dialogo.imprimirPdf();
            dialogo.dispose();
        } else {
            dialogo.setVisible(true);
        }
    }

    private static String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    private static String formatoFecha(LocalDateTime f, String separador) {
        return String.format("%d-%s-%02d%s%02d:%02d", f.getYear(), MESES[f.getMonthValue() - 1],
                f.getDayOfMonth(), separador, f.getHour(), f.getMinute());
    }

    // ---------------------------------------------------------------------
    //  Como se dibuja cada cosa
    // ---------------------------------------------------------------------

 
    private static class TicketRenderer extends JPanel implements ListCellRenderer<Ticket> {

        private final NumberFormat dinero = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
        private Ticket ticket;
        private boolean seleccionado;

        @Override
        public Component getListCellRendererComponent(JList<? extends Ticket> lista, Ticket valor,
                int indice, boolean seleccion, boolean foco) {
            this.ticket = valor;
            this.seleccionado = seleccion;
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int ancho = getWidth();
            int alto = getHeight();

            g2.setColor(seleccionado ? VERDE_CLARO : Color.WHITE);
            g2.fillRect(0, 0, ancho, alto);
            if (seleccionado) {
                g2.setColor(VERDE);
                g2.fillRect(0, 0, 4, alto);
            }
            g2.setColor(LINEA);
            g2.drawLine(12, alto - 1, ancho - 12, alto - 1);

            if (ticket != null) {
                g2.setColor(TEXTO);
                g2.setFont(FUENTE.deriveFont(Font.BOLD, 16f));
                g2.drawString(ticket.getFolio(), 16, 25);

                g2.setColor(new Color(110, 110, 110));
                g2.setFont(FUENTE.deriveFont(12f));
                g2.drawString(formatoFecha(ticket.getFechaExpedicion(), "  "), 16, 45);

                String total = dinero.format(ticket.getVenta().getTotal());
                g2.setColor(TEXTO);
                g2.setFont(FUENTE.deriveFont(Font.BOLD, 14f));
                g2.drawString(total, ancho - 16 - g2.getFontMetrics().stringWidth(total), 25);
            }
            g2.dispose();
        }
    }

    private static class CeldaRenderer extends DefaultTableCellRenderer {

        private final int alineacion;

        CeldaRenderer(int alineacion) {
            this.alineacion = alineacion;
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccion,
                boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, false, false, fila, columna);
            setHorizontalAlignment(alineacion);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            return this;
        }
    }

    private static class EncabezadoRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccion,
                boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, false, false, fila, columna);
            setFont(FUENTE.deriveFont(13f));
            setForeground(new Color(130, 130, 130));
            setBackground(Color.WHITE);
            setOpaque(true);
            setHorizontalAlignment(columna == 0 ? SwingConstants.LEFT
                    : columna == 1 ? SwingConstants.CENTER : SwingConstants.RIGHT);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, LINEA),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)));
            return this;
        }
    }


}
