package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Font;
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
    public AgregarProdPanel() {
        initComponents();
        placeholder();
        tablaFormato();
        cargarProductos();
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
        jLabel1 = new javax.swing.JLabel();
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
        jButton2 = new javax.swing.JButton();
        stockTxt = new javax.swing.JSpinner();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 810, 20));

        jLabel1.setBackground(new java.awt.Color(136, 180, 123));
        jLabel1.setFont(new java.awt.Font("Instrument Sans", 0, 20)); // NOI18N
        jLabel1.setText("Productos  Registrados");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, -1, -1));

        jLabel2.setBackground(new java.awt.Color(136, 180, 123));
        jLabel2.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(136, 180, 123));
        jLabel2.setText("Inventario: Agregar Producto");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel3.setText("Existencias*");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, -1, -1));
        add(buscarNom, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 280, 180, 30));

        jLabel4.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel4.setText("Stock Minimo*");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, -1, -1));
        add(nombreTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 180, 30));

        jLabel5.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel5.setText("Precio*");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, -1, -1));
        add(precioTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, 180, 30));

        jLabel6.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel6.setText("Nombre*");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, -1, -1));

        existenciasTxt.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        add(existenciasTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 160, 180, 30));

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

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, 900, 300));

        jLabel7.setBackground(new java.awt.Color(136, 180, 123));
        jLabel7.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel7.setText("Registrar un nuevo producto");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        guardarBoton.setBackground(new java.awt.Color(136, 180, 123));
        guardarBoton.setFont(new java.awt.Font("Instrument Sans", 0, 12)); // NOI18N
        guardarBoton.setForeground(new java.awt.Color(255, 255, 255));
        guardarBoton.setText("Guardar");
        guardarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBotonActionPerformed(evt);
            }
        });
        add(guardarBoton, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 150, 30));

        jButton2.setBackground(new java.awt.Color(136, 180, 123));
        jButton2.setForeground(new java.awt.Color(136, 180, 123));
        jButton2.setBorderPainted(false);
        jButton2.setEnabled(false);
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 900, 50));

        stockTxt.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        add(stockTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 180, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void guardarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBotonActionPerformed
        Producto prod = new Producto();
        
        String nombre = nombreTxt.getText();
        if(nombre!=null || !(nombre.isBlank()) || !(nombre.matches("\\d+"))){
            prod.setNombre(nombre);
        }else{
        JOptionPane.showMessageDialog(null, "Ocurrió un error en nombre", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        double precio=0;
        try{
        precio = Double.parseDouble(precioTxt.getText());
        }catch(NumberFormatException e){
        JOptionPane.showMessageDialog(null, "Ocurrió un error en precio", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if(precio> 0.0 || !(precioTxt.getText().isBlank())){
            prod.setPrecio(precio);   
        }
        
        int existencias=0;
        try{
         existencias = (Integer) existenciasTxt.getValue();
        }catch(NumberFormatException e){
        JOptionPane.showMessageDialog(null, "Ocurrió un error en existencias", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if(existencias>-1 || !(precioTxt.getText().isBlank())){
            prod.setExistencias(existencias);   
        }
        
        int stock=0;
        try{
         stock = (Integer) stockTxt.getValue();
        }catch(NumberFormatException e){
        JOptionPane.showMessageDialog(null, "Ocurrió un error en stock", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if(existencias>-1 || !(precioTxt.getText().isBlank())){
            prod.setStockMinimo(stock);   
        }
        
        FacadeProducto facade = ServiceFacadeLocator.getInstanceFacadeProducto();
    //llamar a facade
    facade.guardarProducto(prod);
    cargarProductos();
    
    }//GEN-LAST:event_guardarBotonActionPerformed

  
private void cargarProductos() {
    FacadeProducto facade = ServiceFacadeLocator.getInstanceFacadeProducto();

    List<Producto> productos = facade.obtenerProductos();

    DefaultTableModel modelo =
        (DefaultTableModel) tablaProductos.getModel();

    modelo.setRowCount(0);

    for (Producto producto : productos) {
        modelo.addRow(new Object[]{
            producto.getImagen(),
            producto.getNombre(),
            producto.getPrecio(),
            producto.getExistencias(),
            producto.getStockMinimo()
        });
    }
} 



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField buscarNom;
    private javax.swing.JSpinner existenciasTxt;
    private javax.swing.JButton guardarBoton;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField nombreTxt;
    private javax.swing.JTextField precioTxt;
    private javax.swing.JSpinner stockTxt;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables

}
