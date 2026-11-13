package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultComboBoxModel;
import mx.desarrollo.entity.DetalleVentaServicio;
import mx.desarrollo.integration.ServiceFacadeLocator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Andre :c
 */

/** Consultas de los servicios cobrados, con estado de trabajo editable. */
public class ConsultasServicios extends javax.swing.JPanel {

    private final List<ServicioFila> listaServicios = new ArrayList<>();

    public ConsultasServicios() {
        initComponents();
        configurarBusqueda();
        configurarTabla();
        jComboBox2.setModel(new DefaultComboBoxModel<>(
                new String[]{"Todos", "Pendiente", "Realizado"}));
        // El formulario NetBeans deja la seleccion deshabilitada.
        tablaProductos.setRowSelectionAllowed(true);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setFocusable(true);
        jButton1.addActionListener(e -> marcarSeleccionadoRealizado());
        cargarTrabajosDesdeBD();
    }

    /** Recarga los trabajos cobrados de MySQL. */
    public void cargarTrabajosDesdeBD() {
        try {
            List<ServicioFila> filas = new ArrayList<>();
            for (DetalleVentaServicio detalle :
                    ServiceFacadeLocator.getInstanceFacadeVenta().listarTrabajosServicio()) {
                filas.add(new ServicioFila(
                        detalle.getIdDetalleServicio(),
                        detalle.getServicio().getNombre(),
                        detalle.getPrecioUnitario() == null ? 0 : detalle.getPrecioUnitario(),
                        detalle.getNombreCliente(),
                        detalle.getCantidad(),
                        detalle.getEstadoTrabajo()));
            }
            mostrarServicios(filas);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los trabajos de servicios: " + ex.getMessage(),
                    "Error de consulta", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** El boton actualiza solo el trabajo seleccionado, nunca el catalogo. */
    private void marcarSeleccionadoRealizado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un trabajo de la tabla.");
            return;
        }
        int id = Integer.parseInt(tablaProductos.getValueAt(fila, 0).toString());
        String estado = String.valueOf(tablaProductos.getValueAt(fila, 5));
        if ("Realizado".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "Este trabajo ya esta realizado.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Marcar el trabajo #" + id + " como Realizado?",
                "Confirmar cambio", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) return;
        try {
            ServiceFacadeLocator.getInstanceFacadeVenta().marcarTrabajoRealizado(id);
            cargarTrabajosDesdeBD();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el estado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarBusqueda() {
        TextPrompt p = new TextPrompt("Buscar servicio, cliente o ID...", jTextField1);
        p.changeAlpha(0.75f);
        p.changeStyle(Font.PLAIN);
    }

    private void configurarTabla() {
        tablaProductos.setShowVerticalLines(false);
        tablaProductos.setShowHorizontalLines(true);
        tablaProductos.setGridColor(new Color(220, 220, 220));
        tablaProductos.setRowHeight(46);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        tablaProductos.setAutoCreateRowSorter(false);

        // Solamente se colorea la columna Estado (índice 5).
        tablaProductos.getColumnModel().getColumn(5).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable tabla, Object valor,
                                                                   boolean seleccionado, boolean tieneFoco, int fila, int columna) {
                        JLabel celda = (JLabel) super.getTableCellRendererComponent(
                                tabla, valor, seleccionado, tieneFoco, fila, columna);
                        celda.setHorizontalAlignment(SwingConstants.CENTER);
                        celda.setOpaque(true);
                        String estado = valor == null ? "" : valor.toString().trim();
                        if (estado.equalsIgnoreCase("Pendiente")) {
                            celda.setBackground(new Color(255, 224, 178)); // naranja suave
                            celda.setForeground(new Color(133, 73, 0));
                        } else if (estado.equalsIgnoreCase("Realizado")) {
                            celda.setBackground(new Color(195, 225, 195)); // verde suave
                            celda.setForeground(new Color(35, 95, 45));
                        } else {
                            celda.setBackground(Color.WHITE);
                            celda.setForeground(Color.BLACK);
                        }
                        return celda;
                    }
                }
        );

        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(65);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(190);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(200);
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(90);
        tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(130);
    }

    /**
     * Reemplaza los registros que muestra la pantalla.
     * Llama a este método desde la capa que consulte los servicios de la BD.
     */
    public void mostrarServicios(List<ServicioFila> servicios) {
        listaServicios.clear();
        if (servicios != null) {
            listaServicios.addAll(servicios);
        }
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String busqueda = jTextField1.getText().trim().toLowerCase(Locale.ROOT);
        String filtroEstado = String.valueOf(jComboBox2.getSelectedItem());

        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        modelo.setRowCount(0);

        int pendientes = 0;
        for (ServicioFila s : listaServicios) {
            if ("Pendiente".equalsIgnoreCase(s.estado)) {
                pendientes++;
            }

            boolean coincideTexto = String.valueOf(s.id).contains(busqueda)
                    || s.servicio.toLowerCase(Locale.ROOT).contains(busqueda)
                    || s.nombreCliente.toLowerCase(Locale.ROOT).contains(busqueda);
            boolean coincideEstado = "Todos".equals(filtroEstado)
                    || s.estado.equalsIgnoreCase(filtroEstado);

            if (coincideTexto && coincideEstado) {
                modelo.addRow(new Object[]{
                        String.format("%03d", s.id),
                        s.servicio,
                        String.format(Locale.forLanguageTag("es-MX"), "$%,.2f", s.precio),
                        s.nombreCliente,
                        s.cantidad,
                        s.estado
                });
            }
        }

        jLabel1.setText(pendientes == 1
                ? "1 servicio pendiente"
                : pendientes + " servicios pendientes");
        jLabel1.setVisible(pendientes > 0);
    }

    /**
     * Representa una fila de la consulta. No es una entidad de base de datos.
     * El ID debe ser el identificador del registro del servicio realizado/solicitado,
     * no necesariamente el ID del catálogo de tipos de servicio.
     */
    public static class ServicioFila {
        public final int id;
        public final String servicio;
        public final double precio;
        public final String nombreCliente;
        public final int cantidad;
        public final String estado;

        public ServicioFila(int id, String servicio, double precio,
                            String nombreCliente, int cantidad, String estado) {
            this.id = id;
            this.servicio = servicio == null ? "" : servicio;
            this.precio = precio;
            this.nombreCliente = nombreCliente == null ? "" : nombreCliente;
            this.cantidad = cantidad;
            this.estado = estado == null ? "" : estado.trim();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1010, 20));

        jLabel2.setBackground(new java.awt.Color(136, 180, 123));
        jLabel2.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(136, 180, 123));
        jLabel2.setText("Consultas: Servicios");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel4.setFont(new java.awt.Font("Instrument Sans", 0, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(94, 138, 84));
        jLabel4.setText("Estado:");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "ID", "Servicio", "Precio", "Nombre Cliente", "Cantidad", "Estado"
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
        tablaProductos.getAccessibleContext().setAccessibleName("");
        tablaProductos.getAccessibleContext().setAccessibleDescription("");

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 275, 900, 350));

        jLabel7.setBackground(new java.awt.Color(136, 180, 123));
        jLabel7.setFont(new java.awt.Font("Instrument Sans", 0, 16)); // NOI18N
        jLabel7.setText("Filtrar por:");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, -1, 30));

        jComboBox2.setBackground(new java.awt.Color(217, 217, 217));
        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Disponible", "Bajas Existencias", "Sin Existencias" }));
        jComboBox2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 230, 40));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        jTextField1.setName("txtBuscar"); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 520, 40));

        jLabel1.setBackground(new java.awt.Color(255, 255, 190));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(165, 75, 29));
        jLabel1.setText("1 servicio pendiente");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 225, 150)));
        jLabel1.setName("lblAlerta"); // NOI18N
        jLabel1.setOpaque(true);
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 900, 36));

        jButton1.setBackground(new java.awt.Color(136, 180, 123));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Actualizar estado");
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 670, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        aplicarFiltros();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        aplicarFiltros();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables
}
