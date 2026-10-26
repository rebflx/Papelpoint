package mx.desarrollo.ui;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.facade.FacadeProducto;
import mx.desarrollo.integration.ServiceFacadeLocator;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;
import java.util.Comparator;
/**
 *
 * @author erick y andre :c
 */
public class ConsultasProdPanel extends javax.swing.JPanel {



    /**
     * Creates new form AgregarProdPanel
     */
    public ConsultasProdPanel() {
        initComponents();
        placeholder();
        tablaFormato();
        cargarProductos();
    }

  public void placeholder(){
        TextPrompt p= new TextPrompt("Buscar productos o servicios",jTextField1);
        p.changeAlpha(0.75f);
        p.changeStyle(Font.PLAIN);
  }

    public void tablaFormato() {
        tablaProductos.setShowVerticalLines(false);
        tablaProductos.setShowHorizontalLines(true);
        tablaProductos.setGridColor(new Color(220, 220, 220));

        // Solo la columna del estado es la que lleva color; el resto de la tabla queda normal
        tablaProductos.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, false, false, r, c);
                l.setOpaque(true);
                l.setForeground(Color.BLACK);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                String e = String.valueOf(v).trim();
                if (e.equalsIgnoreCase("Sin Existencias"))        l.setBackground(new Color(255, 170, 170));
                else if (e.equalsIgnoreCase("Bajas Existencias")) l.setBackground(new Color(255, 235, 160));
                else                                              l.setBackground(new Color(170, 255, 150));
                return l;
            }
        });
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

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1010, 20));

        jLabel2.setBackground(new java.awt.Color(136, 180, 123));
        jLabel2.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(136, 180, 123));
        jLabel2.setText("Consultas");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel4.setFont(new java.awt.Font("Instrument Sans", 0, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(94, 138, 84));
        jLabel4.setText("Estado:");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 278, -1, -1));

        jLabel6.setFont(new java.awt.Font("Instrument Sans", 0, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(94, 138, 84));
        jLabel6.setText("Ordenar por");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 208, -1, -1));

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
        tablaProductos.getAccessibleContext().setAccessibleName("");
        tablaProductos.getAccessibleContext().setAccessibleDescription("");

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 350, 900, 360));

        jLabel7.setBackground(new java.awt.Color(136, 180, 123));
        jLabel7.setFont(new java.awt.Font("Instrument Sans", 0, 16)); // NOI18N
        jLabel7.setText("Filtrar por:");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, -1, 30));

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
        add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 228, 250, 40));

        jComboBox2.setBackground(new java.awt.Color(217, 217, 217));
        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Disponible", "Bajas Existencias", "Sin Existencias" }));
        jComboBox2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 298, 230, 40));

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
        jLabel1.setText("1 producto con pocas existencias");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 225, 150)));
        jLabel1.setName("lblAlerta"); // NOI18N
        jLabel1.setOpaque(true);
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 900, 36));
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        aplicarFiltros();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        aplicarFiltros();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        aplicarFiltros();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private List<Producto> listaProductos = new ArrayList<>();

    private String calcularEstado(Producto p) {
        if (p.getExistencias() <= 0) return "Sin Existencias";
        if (p.getExistencias() <= p.getStockMinimo()) return "Bajas Existencias";
        return "Disponible";
    }

private void cargarProductos() {
    FacadeProducto facade = ServiceFacadeLocator.getInstanceFacadeProducto();
    listaProductos= new ArrayList<>(facade.obtenerProductos());

    int poco = 0;
    for (Producto p : listaProductos) {
            if(!calcularEstado(p).equals("Disponible")){
                poco++;
        }
    }
    jLabel1.setText(poco == 1 ? "1 producto con pocas existencias que necesita reabastecimiento." : poco + " productos con pocas existencias que necesitan reabastecimiento.");
    jLabel1.setVisible(poco > 0);
    aplicarFiltros();
}

    private void aplicarFiltros() {
        String texto = jTextField1.getText().trim().toLowerCase();
        String estadoSel = String.valueOf(jComboBox2.getSelectedItem());

        //Busqueda por nombre y filtrar por estado
        List<Producto> vista = new ArrayList<>();
        for (Producto p : listaProductos) {
            boolean coincideNombre = p.getNombre().toLowerCase().contains(texto);
            boolean coincideEstado = jComboBox2.getSelectedIndex() == 0
                    || calcularEstado(p).equalsIgnoreCase(estadoSel);
            if (coincideNombre && coincideEstado) vista.add(p);
        }

        // Ordenar por numero posicion de filtro en el combobox
        switch (jComboBox1.getSelectedIndex()) {
            case 0: vista.sort(Comparator.comparing((Producto p) -> p.getNombre().toLowerCase())); break; //A-Z
            case 1: vista.sort(Comparator.comparing((Producto p) -> p.getNombre().toLowerCase()).reversed()); break; //Z-A
            case 2: vista.sort(Comparator.comparing(Producto::getPrecio)); break; //Precio menor a mayor
            case 3: vista.sort(Comparator.comparing(Producto::getPrecio).reversed()); break; //Precio mayor a menor
            case 4: vista.sort(Comparator.comparing(Producto::getExistencias)); break; //Existencia menor a mayor
            case 5: vista.sort(Comparator.comparing(Producto::getExistencias).reversed()); break; //Existencia mayor a menor
        }

        //LLenar la tabla con la informacion de la bd
        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        modelo.setRowCount(0);
        for (Producto p : vista) {
            modelo.addRow(new Object[]{
                    String.format("%03d", p.getIdProducto()), // Ingresa el id del producto
                    p.getNombre(), //Ingresa el nombre del producto
                    String.format("$%,.2f", p.getPrecio()), //Ingresa el precio del producto
                    p.getExistencias(), //Ingresa las existencias del producto
                    "-",
                    calcularEstado(p) //Es el que ingresa si es hay bajas existencias, no hay existencias o se encuentra disponible sin mas, tambien es en el que se hace la comparacion para cambiar el color por estado
            });
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables

}
