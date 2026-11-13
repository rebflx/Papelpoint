package mx.desarrollo.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import mx.desarrollo.entity.Servicio;
import mx.desarrollo.entity.DetalleVentaServicio;
import mx.desarrollo.integration.ServiceFacadeLocator;

/**
 *
 * @author erick
 */
public class Servicios extends javax.swing.JPanel {

    // Servicios obtenidos de la base de datos.
    private List<Servicio> todos = new ArrayList<>();
    private final List<Servicio> disponibles = new ArrayList<>();
    private java.util.function.Consumer<DetalleVentaServicio> receptorCarrito;

    public void configurarCarrito(java.util.function.Consumer<DetalleVentaServicio> receptor) {
        this.receptorCarrito = receptor;
    }

    public Servicios() {
        initComponents();
        soloPrecio(precioTxt);
        cargarServicios();
    }

    // Conservamos este constructor por compatibilidad con MenuPrincipalFrame.
    private mx.desarrollo.entity.Usuario usuarioActual;

    public Servicios(mx.desarrollo.entity.Usuario usuario) {
        this();
        this.usuarioActual = usuario;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        lblRegistrados = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        precioTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        nombreTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnGuardar1 = new javax.swing.JButton();
        btnLimpiar1 = new javax.swing.JButton();
        stockTxt1 = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        precioTxt1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnEditarPrecio = new javax.swing.JButton();
        precioTxt2 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1010, 710));
        setPreferredSize(new java.awt.Dimension(1010, 710));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1010, 2));

        jLabel2.setBackground(new java.awt.Color(136, 180, 123));
        jLabel2.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(136, 180, 123));
        jLabel2.setText("Ventas: Servicios");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 15, 500, 35));

        lblSubtitulo.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        lblSubtitulo.setText("Registrar un nuevo Servicio");
        add(lblSubtitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 85, 500, 30));

        lblRegistrados.setBackground(new java.awt.Color(232, 240, 229));
        lblRegistrados.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblRegistrados.setText("Registrar servicio a ventas");
        lblRegistrados.setOpaque(true);
        add(lblRegistrados, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 930, 40));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del servicio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Instrument Sans", 0, 16))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuardar.setBackground(new java.awt.Color(136, 180, 123));
        btnGuardar.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(755, 130, 140, 36));

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
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 140, 36));
        jPanel1.add(precioTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 58, 310, 35));

        jLabel5.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel5.setText("Precio *");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 35, 150, 20));
        jPanel1.add(nombreTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 60, 310, 35));

        jLabel6.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel6.setText("Nombre del servicio*");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 35, 200, 20));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 125, 930, 190));
        jPanel1.getAccessibleContext().setAccessibleName("Datos del Servicio");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Venta servicio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Instrument Sans", 0, 16))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuardar1.setBackground(new java.awt.Color(136, 180, 123));
        btnGuardar1.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnGuardar1.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar1.setText("Agregar al carrito");
        btnGuardar1.setFocusPainted(false);
        btnGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardar1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnGuardar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(755, 130, 140, 36));

        btnLimpiar1.setBackground(new java.awt.Color(136, 180, 123));
        btnLimpiar1.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnLimpiar1.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar1.setText("Limpiar");
        btnLimpiar1.setFocusPainted(false);
        btnLimpiar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiar1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnLimpiar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 140, 36));

        stockTxt1.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jPanel2.add(stockTxt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(755, 60, 135, 35));

        jLabel11.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel11.setText("Cantidad*");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(755, 35, 130, 20));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox1.setToolTipText("Elegir un servicio\n");
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 60, 350, 35));

        jLabel10.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel10.setText("Servicio*");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 35, 150, 20));
        jPanel2.add(precioTxt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 60, 290, 35));

        jLabel7.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel7.setText("Nombre del cliente*");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 35, 200, 20));

        btnEditarPrecio.setBackground(new java.awt.Color(136, 180, 123));
        btnEditarPrecio.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarPrecio.setText("Editar precio");
        btnEditarPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarPrecioActionPerformed(evt);
            }
        });
        jPanel2.add(btnEditarPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 930, 190));
        add(precioTxt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 490, 180, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String nombre = nombreTxt.getText().trim();
        if (nombre.isEmpty()) {
            aviso("El nombre del servicio es obligatorio.");
            nombreTxt.requestFocus();
            return;
        }
        if (nombre.matches("\\d+")) {
            aviso("El nombre no puede contener solamente números.");
            nombreTxt.requestFocus();
            return;
        }
        if (nombre.length() > 100) {
            aviso("El nombre no puede superar los 100 caracteres.");
            return;
        }
        if (nombreRepetido(nombre)) {
            aviso("Ya existe un servicio con ese nombre.");
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioTxt.getText().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            aviso("Escribe un precio válido.");
            precioTxt.requestFocus();
            return;
        }
        if (!Double.isFinite(precio) || precio <= 0) {
            aviso("El precio debe ser mayor que cero.");
            return;
        }

        Servicio servicio = new Servicio();
        servicio.setNombre(nombre);
        servicio.setPrecio(precio);
        servicio.setDisponible(true);

        try {
            ServiceFacadeLocator.getInstanceFacadeServicio().guardarServicio(servicio);
            limpiarFormulario();
            cargarServicios();
            JOptionPane.showMessageDialog(this, "Servicio guardado correctamente.",
                    "Listo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            aviso(ex.getMessage());
        } catch (Exception ex) {
            errorBD(ex);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar1ActionPerformed
        if (receptorCarrito == null) {
            aviso("Abre Servicios desde el boton Servicios de la ventana Ventas.");
            return;
        }
        int indice = jComboBox1.getSelectedIndex();
        if (indice < 0 || indice >= disponibles.size()) {
            aviso("Selecciona un servicio disponible.");
            return;
        }
        String cliente = precioTxt1.getText().trim();
        if (cliente.isEmpty() || cliente.length() > 100) {
            aviso("Escribe el nombre del cliente (maximo 100 caracteres).");
            return;
        }
        int cantidad;
        try {
            stockTxt1.commitEdit();
            cantidad = ((Number) stockTxt1.getValue()).intValue();
        } catch (java.text.ParseException | ClassCastException ex) {
            aviso("La cantidad debe ser un numero entero valido.");
            return;
        }
        if (cantidad < 1) {
            aviso("La cantidad debe ser mayor que cero.");
            return;
        }

        DetalleVentaServicio detalle = new DetalleVentaServicio();
        detalle.setServicio(disponibles.get(indice));
        detalle.setNombreCliente(cliente);
        detalle.setCantidad(cantidad);
        try {
            receptorCarrito.accept(detalle);
            // El receptor vuelve a Ventas conservando el carrito.
        } catch (IllegalArgumentException ex) {
            aviso(ex.getMessage());
        }
    }//GEN-LAST:event_btnGuardar1ActionPerformed

    private void btnLimpiar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiar1ActionPerformed
        jComboBox1.setSelectedIndex(jComboBox1.getItemCount() > 0 ? 0 : -1);
        precioTxt1.setText(""); // Campo: nombre del cliente
        stockTxt1.setValue(1); // Campo: cantidad
    }//GEN-LAST:event_btnLimpiar1ActionPerformed

    private void btnEditarPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarPrecioActionPerformed
        // TODO add your handling code here:
            int indice = jComboBox1.getSelectedIndex();

    if (indice < 0 || indice >= disponibles.size()) {
        aviso("Selecciona un servicio.");
        return;
    }

    Servicio servicio = disponibles.get(indice);

    double precioAnterior = servicio.getPrecio();

    String entrada = JOptionPane.showInputDialog(
            this,
            "Servicio: " + servicio.getNombre()
            + "\nPrecio actual: $" + precioAnterior
            + "\n\nEscribe el nuevo precio:",
            String.format(java.util.Locale.US, "%.2f", precioAnterior)
    );

    if (entrada == null) {
        return;
    }

    double nuevoPrecio;

    try {
        nuevoPrecio = Double.parseDouble(
                entrada.trim().replace(",", ".")
        );
    } catch (NumberFormatException ex) {
        aviso("Escribe un precio válido.");
        return;
    }

    if (!Double.isFinite(nuevoPrecio) || nuevoPrecio <= 0) {
        aviso("El precio debe ser mayor que cero.");
        return;
    }

    if (Math.round(nuevoPrecio * 100) != nuevoPrecio * 100) {
        aviso("El precio solo puede tener dos decimales.");
        return;
    }

    if (Double.compare(nuevoPrecio, precioAnterior) == 0) {
        aviso("El precio es igual al actual.");
        return;
    }

    int confirmar = JOptionPane.showConfirmDialog(
            this,
            "¿Actualizar el precio de " + servicio.getNombre()
            + "\nde $" + precioAnterior
            + " a $" + nuevoPrecio + "?",
            "Confirmar cambio",
            JOptionPane.YES_NO_OPTION
    );

    if (confirmar != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        servicio.setPrecio(nuevoPrecio);

        ServiceFacadeLocator.getInstanceFacadeServicio()
                .modificarServicio(servicio);

        cargarServicios();

        JOptionPane.showMessageDialog(
                this,
                "Precio actualizado correctamente.",
                "Listo",
                JOptionPane.INFORMATION_MESSAGE
        );

    } catch (Exception ex) {
        servicio.setPrecio(precioAnterior);
        errorBD(ex);
    }
    }//GEN-LAST:event_btnEditarPrecioActionPerformed


    private void cargarServicios() {
        jComboBox1.removeAllItems();
        disponibles.clear();
        todos = new ArrayList<>();
        try {
            todos = ServiceFacadeLocator.getInstanceFacadeServicio()
                    .obtenerServiciosActualizados();
            for (Servicio servicio : todos) {
                if (Boolean.TRUE.equals(servicio.getDisponible())) {
                    disponibles.add(servicio);
                    jComboBox1.addItem(servicio.getNombre());
                }
            }
        } catch (Exception ex) {
            errorBD(ex);
        }
    }

    private boolean nombreRepetido(String nombre) {
        for (Servicio servicio : todos) {
            if (servicio.getNombre() != null &&
                    servicio.getNombre().trim().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    private void limpiarFormulario() {
        nombreTxt.setText("");
        precioTxt.setText("");
        nombreTxt.requestFocus();
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje,
                "Revisa los datos", JOptionPane.WARNING_MESSAGE);
    }

    private void errorBD(Throwable ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación con la base de datos: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void soloPrecio(javax.swing.JTextField campo) {
        ((javax.swing.text.AbstractDocument) campo.getDocument()).setDocumentFilter(
                new javax.swing.text.DocumentFilter() {
                    private final java.util.regex.Pattern patron =
                            java.util.regex.Pattern.compile("\\d{0,7}(\\.\\d{0,2})?");

                    @Override
                    public void insertString(FilterBypass fb, int offset, String text,
                                             javax.swing.text.AttributeSet attr)
                            throws javax.swing.text.BadLocationException {
                        replace(fb, offset, 0, text, attr);
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text,
                                        javax.swing.text.AttributeSet attrs)
                            throws javax.swing.text.BadLocationException {
                        String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
                        String nuevo = new StringBuilder(actual)
                                .replace(offset, offset + length, text == null ? "" : text)
                                .toString();
                        if (patron.matcher(nuevo).matches()) {
                            super.replace(fb, offset, length, text, attrs);
                        }
                    }
                });
        campo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditarPrecio;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardar1;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLimpiar1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblRegistrados;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JTextField nombreTxt;
    private javax.swing.JTextField precioTxt;
    private javax.swing.JTextField precioTxt1;
    private javax.swing.JTextField precioTxt2;
    private javax.swing.JSpinner stockTxt1;
    // End of variables declaration//GEN-END:variables

}

