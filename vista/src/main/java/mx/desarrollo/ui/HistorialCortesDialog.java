/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import mx.desarrollo.delegate.DelegateCorte;
import mx.desarrollo.delegate.DelegateVenta;
import mx.desarrollo.delegate.ResumenCorte;
import mx.desarrollo.entity.CorteCaja;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Venta;
import mx.desarrollo.facade.FacadeCorteCaja;
import mx.desarrollo.integration.ServiceFacadeLocator;


public class HistorialCortesDialog extends javax.swing.JDialog {

    private static final Color VERDE = new Color(46, 125, 50);
    private static final Color ROJO = new Color(200, 40, 40);
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Usuario usuarioActual;
    private final FacadeCorteCaja facade = ServiceFacadeLocator.getInstanceFacadeCorteCaja();
    private final List<Usuario> usuarios = new ArrayList<>();
    private List<CorteCaja> cortes = new ArrayList<>();
    private ResumenCorte seleccionado;

    public HistorialCortesDialog(java.awt.Frame parent, boolean modal) {
        this(parent, modal, null);
    }

    /**
     * Creates new form HistorialCortesDialog
     */
    public HistorialCortesDialog(java.awt.Frame parent, boolean modal, Usuario usuarioActual) {
        super(parent, modal);
        this.usuarioActual = usuarioActual;
        initComponents();
        formatoTablas();
        if (usuarioActual == null) {
            return;
        }
        new TextPrompt("dd/MM/yyyy", txtDesde);
        new TextPrompt("dd/MM/yyyy", txtHasta);
        tablaCortes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDetalle();
            }
        });
        cargarUsuarios();
        buscar();
        setLocationRelativeTo(parent);
    }



    private void formatoTablas() {
        estilizar(tablaCortes, 28);
        estilizar(tablaVentas, 26);

        for (int i = 0; i < tablaCortes.getColumnCount(); i++) {
            tablaCortes.getColumnModel().getColumn(i).setCellRenderer(i >= 4 ? renderMonto(i == 6) : centro());
        }
        anchos(tablaCortes, 60, 200, 140, 140, 130, 130, 120);

        for (int i = 0; i < tablaVentas.getColumnCount(); i++) {
            tablaVentas.getColumnModel().getColumn(i).setCellRenderer(i == 4 ? derecha() : centro());
        }
        anchos(tablaVentas, 120, 200, 200, 200, 200);
        btnReimprimir.setEnabled(false);
    }

    private static void estilizar(JTable t, int alto) {
        t.setRowHeight(alto);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(new Color(220, 220, 220));
        t.setSelectionBackground(new Color(232, 240, 229));
        t.setSelectionForeground(Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setFont(new Font("Instrument Sans", Font.BOLD, 13));
    }

    private static void anchos(JTable t, int... anchos) {
        for (int i = 0; i < anchos.length; i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private static DefaultTableCellRenderer centro() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        return r;
    }

    private static DefaultTableCellRenderer derecha() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        return r;
    }

    private static DefaultTableCellRenderer renderMonto(boolean diferencia) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String s = String.valueOf(v);
                if (diferencia && !sel) {
                    c.setForeground(s.startsWith("-") ? ROJO : s.startsWith("+") ? VERDE : Color.BLACK);
                } else if (!sel) {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        return r;
    }

    private void cargarUsuarios() {
        try {
            usuarios.clear();
            usuarios.addAll(facade.usuariosParaCorte(usuarioActual));
            List<String> nombres = new ArrayList<>();
            nombres.add("Todos");
            for (Usuario u : usuarios) {
                nombres.add(u.getNombrePersona() + " (" + u.getNombreUsuario() + ")");
            }
            cmbUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(nombres.toArray(new String[0])));
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
        }
    }



    private void buscar() {
        LocalDate desde = null;
        LocalDate hasta = null;
        if (!txtDesde.getText().isBlank()) {
            desde = CortesCajaPanel.leerDia(txtDesde.getText());
            if (desde == null) {
                aviso("La fecha inicial no es válida. Usa el formato dd/MM/yyyy.");
                return;
            }
        }
        if (!txtHasta.getText().isBlank()) {
            hasta = CortesCajaPanel.leerDia(txtHasta.getText());
            if (hasta == null) {
                aviso("La fecha final no es válida. Usa el formato dd/MM/yyyy.");
                return;
            }
        }
        int i = cmbUsuario.getSelectedIndex();
        Usuario filtro = (i > 0 && i - 1 < usuarios.size()) ? usuarios.get(i - 1) : null;

        try {
            cortes = facade.historial(usuarioActual, desde, hasta, filtro);
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
            return;
        }

        DefaultTableModel m = (DefaultTableModel) tablaCortes.getModel();
        m.setRowCount(0);
        for (CorteCaja c : cortes) {
            m.addRow(new Object[]{
                c.getIdCorte(),
                c.getUsuario().getNombrePersona(),
                c.getFechaInicio().format(FECHA_HORA),
                c.getFechaCierre().format(FECHA_HORA),
                CortesCajaPanel.dinero(c.getTotalCalculado()),
                CortesCajaPanel.dinero(c.getTotalContado()),
                conSigno(DelegateCorte.diferencia(c))
            });
        }
        lblContador.setText(cortes.size() + (cortes.size() == 1 ? " corte" : " cortes")
                + (cortes.size() >= DelegateCorte.LIMITE_HISTORIAL ? " (mostrando los más recientes)" : ""));
        limpiarDetalle();
    }

    private static String conSigno(double d) {
        if (d > 0) {
            return "+" + CortesCajaPanel.dinero(d);
        }
        return CortesCajaPanel.dinero(d);
    }

    private void limpiarDetalle() {
        seleccionado = null;
        ((DefaultTableModel) tablaVentas.getModel()).setRowCount(0);
        lblDetalleTitulo.setText("  Selecciona un corte para ver sus ventas");
        btnReimprimir.setEnabled(false);
    }

    private void cargarDetalle() {
        int fila = tablaCortes.getSelectedRow();
        if (fila < 0 || fila >= cortes.size()) {
            limpiarDetalle();
            return;
        }
        try {
            CorteCaja corte = cortes.get(tablaCortes.convertRowIndexToModel(fila));
            seleccionado = facade.detalleCorte(usuarioActual, corte.getIdCorte());
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            limpiarDetalle();
            mostrarError(ex);
            return;
        }
        DefaultTableModel m = (DefaultTableModel) tablaVentas.getModel();
        m.setRowCount(0);
        for (Venta v : seleccionado.getVentas()) {
            m.addRow(new Object[]{
                DelegateVenta.folio(v.getIdVenta()),
                v.getFecha().format(FECHA_HORA),
                CortesCajaPanel.capitalizar(v.getMetodoPago()),
                CortesCajaPanel.capitalizar(v.getEstado()),
                CortesCajaPanel.dinero(v.getTotal())
            });
        }
        lblDetalleTitulo.setText("  Ventas del corte #" + seleccionado.getIdCorte() + " - "
                + seleccionado.getUsuario().getNombrePersona());
        btnReimprimir.setEnabled(true);
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Historial de cortes", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarError(Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            aviso(ex.getMessage());
            return;
        }
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación con la base de datos.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoImpresora = new javax.swing.ButtonGroup();
        panelRaiz = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblDesde = new javax.swing.JLabel();
        txtDesde = new javax.swing.JTextField();
        lblHasta = new javax.swing.JLabel();
        txtHasta = new javax.swing.JTextField();
        lblUsuarioFiltro = new javax.swing.JLabel();
        cmbUsuario = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        lblCortesTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCortes = new javax.swing.JTable();
        lblDetalleTitulo = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        lblContador = new javax.swing.JLabel();
        rbTicket = new javax.swing.JRadioButton();
        rbLaser = new javax.swing.JRadioButton();
        btnReimprimir = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("Historial de cortes de caja");

        panelRaiz.setBackground(new java.awt.Color(255, 255, 255));
        panelRaiz.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(136, 180, 123));
        lblTitulo.setText("Historial de cortes de caja");
        panelRaiz.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        panelRaiz.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1000, 20));

        lblDesde.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblDesde.setText("Desde:");
        panelRaiz.add(lblDesde, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 98, -1, -1));

        txtDesde.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        panelRaiz.add(txtDesde, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 92, 110, 30));

        lblHasta.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblHasta.setText("Hasta:");
        panelRaiz.add(lblHasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 98, -1, -1));

        txtHasta.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        panelRaiz.add(txtHasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 92, 110, 30));

        lblUsuarioFiltro.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblUsuarioFiltro.setText("Usuario:");
        panelRaiz.add(lblUsuarioFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(415, 98, -1, -1));

        cmbUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        panelRaiz.add(cmbUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 92, 230, 30));

        btnBuscar.setBackground(new java.awt.Color(136, 180, 123));
        btnBuscar.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        panelRaiz.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 92, 100, 30));

        btnLimpiar.setBackground(new java.awt.Color(136, 180, 123));
        btnLimpiar.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        panelRaiz.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(845, 92, 100, 30));

        lblCortesTitulo.setBackground(new java.awt.Color(232, 240, 229));
        lblCortesTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblCortesTitulo.setOpaque(true);
        lblCortesTitulo.setText("  Cortes registrados");
        panelRaiz.add(lblCortesTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 135, 920, 34));

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        tablaCortes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Usuario", "Inicio", "Cierre", "Total calculado", "Total contado", "Diferencia"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaCortes);

        panelRaiz.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 169, 920, 230));

        lblDetalleTitulo.setBackground(new java.awt.Color(232, 240, 229));
        lblDetalleTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblDetalleTitulo.setOpaque(true);
        lblDetalleTitulo.setText("  Selecciona un corte para ver sus ventas");
        panelRaiz.add(lblDetalleTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 920, 34));

        jScrollPane2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Fecha y hora", "Método", "Estado", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaVentas);

        panelRaiz.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 444, 920, 150));

        lblContador.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblContador.setText("0 cortes");
        panelRaiz.add(lblContador, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 614, 300, -1));

        grupoImpresora.add(rbTicket);
        rbTicket.setBackground(new java.awt.Color(255, 255, 255));
        rbTicket.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        rbTicket.setText("Ticket");
        rbTicket.setSelected(true);
        panelRaiz.add(rbTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 610, 70, 28));

        grupoImpresora.add(rbLaser);
        rbLaser.setBackground(new java.awt.Color(255, 255, 255));
        rbLaser.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        rbLaser.setText("Láser");
        panelRaiz.add(rbLaser, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 610, 70, 28));

        btnReimprimir.setBackground(new java.awt.Color(136, 180, 123));
        btnReimprimir.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnReimprimir.setForeground(new java.awt.Color(255, 255, 255));
        btnReimprimir.setText("Reimprimir");
        btnReimprimir.setFocusPainted(false);
        btnReimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReimprimirActionPerformed(evt);
            }
        });
        panelRaiz.add(btnReimprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 608, 130, 32));

        btnCerrar.setBackground(new java.awt.Color(136, 180, 123));
        btnCerrar.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnCerrar.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrar.setText("Cerrar");
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        panelRaiz.add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 608, 130, 32));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRaiz, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRaiz, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        txtDesde.setText("");
        txtHasta.setText("");
        cmbUsuario.setSelectedIndex(0);
        buscar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnReimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReimprimirActionPerformed
        if (seleccionado == null) {
            return;
        }
        try {
            boolean ticket = rbTicket.isSelected();
            ImpresionCorte.imprimir(this, facade.lineasImpresion(seleccionado, ticket ? 40 : 64), ticket,
                    "Corte #" + seleccionado.getIdCorte());
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
        }
    }//GEN-LAST:event_btnReimprimirActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnReimprimir;
    private javax.swing.JComboBox<String> cmbUsuario;
    private javax.swing.ButtonGroup grupoImpresora;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblContador;
    private javax.swing.JLabel lblCortesTitulo;
    private javax.swing.JLabel lblDesde;
    private javax.swing.JLabel lblDetalleTitulo;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuarioFiltro;
    private javax.swing.JPanel panelRaiz;
    private javax.swing.JRadioButton rbLaser;
    private javax.swing.JRadioButton rbTicket;
    private javax.swing.JTable tablaCortes;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextField txtDesde;
    private javax.swing.JTextField txtHasta;
    // End of variables declaration//GEN-END:variables

}
