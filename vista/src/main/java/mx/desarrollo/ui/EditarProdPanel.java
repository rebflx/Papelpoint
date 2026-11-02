package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.facade.FacadeProducto;
import mx.desarrollo.integration.ServiceFacadeLocator;

/**
 *
 * @author erick
 */
public class EditarProdPanel extends javax.swing.JPanel {

private List<Producto> todos = new ArrayList<>();
private final List<Producto> visibles = new ArrayList<>();
private Producto seleccionado;

   public EditarProdPanel() {
    initComponents();
    placeholder();
    tablaFormato();
    iniciarEventos();
    cargarProductos();
    soloPrecio(precioTxt);

}

private void iniciarEventos() {
    buscarNom.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { aplicarFiltro(); }
        public void removeUpdate(DocumentEvent e) { aplicarFiltro(); }
        public void changedUpdate(DocumentEvent e) { aplicarFiltro(); }
    });

    tablaProductos.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            productoSeleccionadoEnTabla();
        }
    });
}
  
    
    public void tablaFormato(){
tablaProductos.setShowVerticalLines(false);
tablaProductos.setShowHorizontalLines(true);
tablaProductos.setGridColor(new Color(220, 220, 220));
tablaProductos.setColumnSelectionAllowed(false);
tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
tablaProductos.setRowHeight(30);
tablaProductos.setSelectionBackground(new Color(232, 240, 229));
tablaProductos.setSelectionForeground(Color.BLACK);
    }
    public void placeholder(){
TextPrompt placeholder = new TextPrompt("Buscar Producto", buscarNom);
placeholder.changeAlpha(0.75f); // Transparencia del texto (0.0f a 1.0f)
placeholder.changeStyle(Font.BOLD); // Estilo de fuente (ej. Itálica)
    } 
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        buscarNom = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        nombreTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        precioTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        existenciasTxt = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        guardarBoton = new javax.swing.JButton();
        stockTxt = new javax.swing.JSpinner();
        guardarBoton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblRegistrados = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1280, 770));
        setMinimumSize(new java.awt.Dimension(1280, 770));
        setPreferredSize(new java.awt.Dimension(1280, 770));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1010, 20));

        jLabel2.setBackground(new java.awt.Color(136, 180, 123));
        jLabel2.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(136, 180, 123));
        jLabel2.setText("Editar  Productos");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel3.setText("Existencias*");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 130, -1, -1));
        add(buscarNom, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 330, 30));

        jLabel4.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel4.setText("Stock Minimo*");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, -1, -1));
        add(nombreTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 180, 30));

        jLabel5.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel5.setText("Precio*");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, -1, -1));
        add(precioTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 180, 30));

        jLabel6.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel6.setText("Nombre*");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, -1, -1));

        existenciasTxt.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        add(existenciasTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 160, 180, 30));

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Imagen", "Nombre", "Precio", "Existencias", "Stock Minimo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProductos.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(tablaProductos);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 900, 310));

        jLabel7.setBackground(new java.awt.Color(136, 180, 123));
        jLabel7.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel7.setText("Modificar un producto");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, -1, -1));

        guardarBoton.setBackground(new java.awt.Color(136, 180, 123));
        guardarBoton.setFont(new java.awt.Font("Instrument Sans", 0, 12)); // NOI18N
        guardarBoton.setForeground(new java.awt.Color(255, 255, 255));
        guardarBoton.setText("Eliminar");
        guardarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBotonActionPerformed(evt);
            }
        });
        add(guardarBoton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, 150, 30));

        stockTxt.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        add(stockTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 240, 180, 30));

        guardarBoton1.setBackground(new java.awt.Color(136, 180, 123));
        guardarBoton1.setFont(new java.awt.Font("Instrument Sans", 0, 12)); // NOI18N
        guardarBoton1.setForeground(new java.awt.Color(255, 255, 255));
        guardarBoton1.setText("Modificar");
        guardarBoton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBoton1ActionPerformed(evt);
            }
        });
        add(guardarBoton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 150, 30));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Instrument Sans", 0, 16))); // NOI18N
        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 820, 220));

        lblRegistrados.setBackground(new java.awt.Color(232, 240, 229));
        lblRegistrados.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblRegistrados.setText("  Usuarios Registrados");
        lblRegistrados.setOpaque(true);
        add(lblRegistrados, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, 900, 40));
    }// </editor-fold>//GEN-END:initComponents

    private void guardarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBotonActionPerformed
     if (seleccionado == null) {
        aviso("Selecciona un producto de la tabla para eliminarlo.");
        return;
    }
    int r = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el producto \"" + seleccionado.getNombre() + "\"?",
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (r != JOptionPane.YES_OPTION) {
        return;
    }
    try {
        ServiceFacadeLocator.getInstanceFacadeProducto().eliminarProducto(copiar(seleccionado));
    } catch (Throwable ex) {
        errorBD(ex);
        return;
    }
    limpiarFormulario();
    cargarProductos();
    
    }//GEN-LAST:event_guardarBotonActionPerformed

    private void guardarBoton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBoton1ActionPerformed
if (seleccionado == null) {
        aviso("Selecciona un producto de la tabla para modificarlo.");
        return;
    }
    Producto p = copiar(seleccionado);

    String nombre = nombreTxt.getText().trim();
if (nombre.isEmpty() || nombre.matches("\\d+")) {
    aviso("El nombre no es válido.");
    return;
}
if (nombreRepetido(nombre, seleccionado)) {
    aviso("Ya existe otro producto con ese nombre.");
    nombreTxt.requestFocus();
    return;
}
p.setNombre(nombre);

    try {
        double precio = Double.parseDouble(precioTxt.getText().trim());
        if (precio <= 0) {
            aviso("El precio debe ser mayor que 0.");
            return;
        }
        p.setPrecio(precio);
    } catch (NumberFormatException e) {
        aviso("El precio debe ser un número.");
        return;
    }

    p.setExistencias((Integer) existenciasTxt.getValue());
    p.setStockMinimo((Integer) stockTxt.getValue());

    try {
        ServiceFacadeLocator.getInstanceFacadeProducto().modificarProducto(p);
    } catch (Throwable ex) {
        errorBD(ex);
        return;
    }
    limpiarFormulario();
    cargarProductos();
    JOptionPane.showMessageDialog(this, "Producto modificado correctamente.",
            "Listo", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_guardarBoton1ActionPerformed

  
private void cargarProductos() {
    try {
        List<Producto> lista = ServiceFacadeLocator.getInstanceFacadeProducto().obtenerProductosActualizados();
        todos = new ArrayList<>();
        for (Producto p : lista) {
            if (Boolean.TRUE.equals(p.getActivo())) {
                todos.add(p);
            }
        }
    } catch (Throwable ex) {
        errorBD(ex);
        todos = new ArrayList<>();
    }
    aplicarFiltro();
}

private void aplicarFiltro() {
    String q = buscarNom.getText() == null ? "" : buscarNom.getText().trim().toLowerCase();
    DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();

    modelo.setRowCount(0);
    visibles.clear();
    for (Producto p : todos) {
        boolean coincide = q.isEmpty()
                || (p.getNombre() != null && p.getNombre().toLowerCase().contains(q));
        if (coincide) {
            visibles.add(p);
            modelo.addRow(new Object[]{
                p.getImagen(),
                p.getNombre(),
                p.getPrecio(),
                p.getExistencias(),
                p.getStockMinimo()
            });
        }
    }
    seleccionado = null;
}

private void productoSeleccionadoEnTabla() {
    int fila = tablaProductos.getSelectedRow();
    if (fila < 0 || fila >= visibles.size()) {
        seleccionado = null;
        return;
    }
    seleccionado = visibles.get(fila);
    nombreTxt.setText(seleccionado.getNombre());
    precioTxt.setText(String.valueOf(seleccionado.getPrecio()));
    existenciasTxt.setValue(seleccionado.getExistencias());
    stockTxt.setValue(seleccionado.getStockMinimo());
}

private void limpiarFormulario() {
    nombreTxt.setText("");
    precioTxt.setText("");
    existenciasTxt.setValue(0);
    stockTxt.setValue(0);
    tablaProductos.clearSelection();
    seleccionado = null;
}

private Producto copiar(Producto o) {
    Producto c = new Producto();
    c.setIdProducto(o.getIdProducto());
    c.setNombre(o.getNombre());
    c.setPrecio(o.getPrecio());
    c.setExistencias(o.getExistencias());
    c.setStockMinimo(o.getStockMinimo());
    c.setImagen(o.getImagen());
    c.setActivo(o.getActivo());
    return c;
}

private void aviso(String mensaje) {
    JOptionPane.showMessageDialog(this, mensaje, "Revisa los datos", JOptionPane.WARNING_MESSAGE);
}

private void errorBD(Throwable ex) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(this,
            "No se pudo completar la operación con la base de datos.",
            "Error", JOptionPane.ERROR_MESSAGE);
}

private boolean nombreRepetido(String nombre, Producto actual) {
    for (Producto p : todos) {
        boolean esElMismo = java.util.Objects.equals(p.getIdProducto(), actual.getIdProducto());
        if (!esElMismo && p.getNombre() != null
                && p.getNombre().trim().equalsIgnoreCase(nombre)) {
            return true;
        }
    }
    return false;
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
    private javax.swing.JTextField buscarNom;
    private javax.swing.JSpinner existenciasTxt;
    private javax.swing.JButton guardarBoton;
    private javax.swing.JButton guardarBoton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblRegistrados;
    private javax.swing.JTextField nombreTxt;
    private javax.swing.JTextField precioTxt;
    private javax.swing.JSpinner stockTxt;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables

}
