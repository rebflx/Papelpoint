package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.facade.FacadeProducto;
import mx.desarrollo.integration.ServiceFacadeLocator;

/**
 *
 * @author erick
 */
public class AgregarProdPanel extends javax.swing.JPanel {

    /**
     * Creates new form AgregarProdPanel
     */
    private List<Producto> todos = new ArrayList<>();
    
    public AgregarProdPanel() {
        initComponents();
        placeholder();
        tablaFormato();
        cargarProductos();
        soloPrecio(precioTxt);

    }

    private mx.desarrollo.entity.Usuario usuarioActual;

    public AgregarProdPanel(mx.desarrollo.entity.Usuario usuario) {
        this();
        this.usuarioActual = usuario;
    }

    private void registrarHistorial(Producto p, String detalles) {
        try {
            ServiceFacadeLocator.getInstanceFacadeHistorial().registrar(usuarioActual, p, detalles);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
  
    
    public void tablaFormato(){
tablaProductos.setShowVerticalLines(false);
tablaProductos.setShowHorizontalLines(true);
tablaProductos.setGridColor(new Color(220, 220, 220));
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
        lblSubtitulo = new javax.swing.JLabel();
        buscarNom = new javax.swing.JTextField();
        lblRegistrados = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nombreTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        precioTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        existenciasTxt = new javax.swing.JSpinner();
        stockTxt = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 810, 20));

        jLabel2.setBackground(new java.awt.Color(136, 180, 123));
        jLabel2.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(136, 180, 123));
        jLabel2.setText("Inventario: Agregar Producto");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        lblSubtitulo.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        lblSubtitulo.setText("Registrar un nuevo Producto");
        add(lblSubtitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));
        add(buscarNom, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 300, 32));

        lblRegistrados.setBackground(new java.awt.Color(232, 240, 229));
        lblRegistrados.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblRegistrados.setText("  Productos Registrados");
        lblRegistrados.setOpaque(true);
        add(lblRegistrados, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, 920, 40));

        jLabel3.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel3.setText("Existencias*");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, -1, -1));

        jLabel4.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel4.setText("Stock Minimo*");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, -1, -1));
        add(nombreTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 180, 30));

        jLabel5.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel5.setText("Precio*");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, -1, -1));
        add(precioTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 180, 30));

        jLabel6.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel6.setText("Nombre*");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, -1));

        existenciasTxt.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        add(existenciasTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 180, 30));

        stockTxt.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        add(stockTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 180, 30));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Instrument Sans", 0, 16))); // NOI18N
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
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 150, 30));

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
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 150, 30));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 820, 220));

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Nombre", "Precio", "Existencias", "Stock Minimo"
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

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 920, 310));
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
 // Nombre
    String nombre = nombreTxt.getText() == null ? "" : nombreTxt.getText().trim();
    if (nombre.isEmpty()) {
        aviso("El nombre es obligatorio.");
        nombreTxt.requestFocus();
        return;
    }
    if (nombre.matches("\\d+")) {
        aviso("El nombre no puede ser solo números.");
        nombreTxt.requestFocus();
        return;
    }
    if (nombre.length() > 100) {
        aviso("El nombre es demasiado largo (máximo 100 caracteres).");
        nombreTxt.requestFocus();
        return;
    }
    if (nombreRepetido(nombre)) {
        aviso("Ya existe un producto con ese nombre.");
        nombreTxt.requestFocus();
        return;
    }

    // Precio
    double precio;
    try {
        precio = Double.parseDouble(precioTxt.getText().trim().replace(",", "."));
    } catch (NumberFormatException e) {
        aviso("El precio debe ser un número válido (ej. 149.90).");
        precioTxt.requestFocus();
        return;
    }
    if (Double.isNaN(precio) || Double.isInfinite(precio) || precio <= 0) {
        aviso("El precio debe ser mayor que 0.");
        precioTxt.requestFocus();
        return;
    }

    // Spinners (forzar a que se tome lo que el usuario escribió a mano)
    int existencias;
    int stock;
    try {
        existenciasTxt.commitEdit();
        stockTxt.commitEdit();
        existencias = (Integer) existenciasTxt.getValue();
        stock = (Integer) stockTxt.getValue();
    } catch (java.text.ParseException | ClassCastException e) {
        aviso("Existencias y stock mínimo deben ser números enteros.");
        return;
    }
    if (existencias < 0) {
        aviso("Las existencias no pueden ser negativas.");
        return;
    }
    if (stock < 0) {
        aviso("El stock mínimo no puede ser negativo.");
        return;
    }

    Producto prod = new Producto();
    prod.setNombre(nombre);
    prod.setPrecio(precio);
    prod.setExistencias(existencias);
    prod.setStockMinimo(stock);
    prod.setActivo(true);

    try {
        ServiceFacadeLocator.getInstanceFacadeProducto().guardarProducto(prod);
        registrarHistorial(prod, "Alta de producto");
    } catch (IllegalArgumentException ex) {
        aviso(ex.getMessage());
        return;
    } catch (Throwable ex) {
        errorBD(ex);
        return;
    }

    limpiarFormulario();
    cargarProductos();
    JOptionPane.showMessageDialog(this, "Producto guardado correctamente.",
            "Listo", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

  
private void cargarProductos() {
    DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
    modelo.setRowCount(0);
    todos = new ArrayList<>();

    try {
        List<Producto> productos = ServiceFacadeLocator.getInstanceFacadeProducto().obtenerProductosActualizados();
        for (Producto p : productos) {
            if (Boolean.TRUE.equals(p.getActivo())) {
                todos.add(p);
                modelo.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getExistencias(),
                    p.getStockMinimo()
                });
            }
        }
    } catch (Throwable ex) {
        errorBD(ex);
    }
}

private boolean nombreRepetido(String nombre) {
    for (Producto p : todos) {
        if (p.getNombre() != null && p.getNombre().trim().equalsIgnoreCase(nombre)) {
            return true;
        }
    }
    return false;
}

private void limpiarFormulario() {
    nombreTxt.setText("");
    precioTxt.setText("");
    existenciasTxt.setValue(1);
    stockTxt.setValue(0);
    nombreTxt.requestFocus();
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
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JTextField buscarNom;
    private javax.swing.JSpinner existenciasTxt;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblRegistrados;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JTextField nombreTxt;
    private javax.swing.JTextField precioTxt;
    private javax.swing.JSpinner stockTxt;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables

}
